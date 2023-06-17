package com.capitole.price.service.impl;

import com.capitole.price.exception.BusinessException;
import com.capitole.price.model.dto.PriceResponseDto;
import com.capitole.price.model.entity.PriceEntity;
import com.capitole.price.respository.PriceRepository;
import com.capitole.price.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public PriceResponseDto findActivePrices(int brandId, int productId, LocalDateTime currentDate) {
        log.info("Searching price list");
        List<PriceEntity> prices = priceRepository.findActivePrices(brandId, productId, currentDate);
        return prices.stream().findFirst().map(p -> {
            log.info("Prices found: {}. " + (prices.size() > 1 ? "The one with priority {} will be returned" : ""));
            return PriceResponseDto.fromEntity(p);
        }).orElseThrow (() -> {
            log.error("Prices not found, BussinesException will be returned");
            return new BusinessException("E200", HttpStatus.NOT_FOUND
                    , String.format("There is not prices for brand %d, product %d and date %s", brandId, productId, currentDate));
        } );
    }

}
