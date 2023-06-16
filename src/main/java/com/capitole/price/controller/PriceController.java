package com.capitole.price.controller;

import com.capitole.price.model.dto.PriceResponseDto;
import com.capitole.price.service.PriceService;
import com.capitole.price.service.impl.PriceServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/prices")
public class PriceController {

    private PriceService priceService;
    public PriceController(PriceServiceImpl priceService) {
        this.priceService = priceService;
    }
    @GetMapping("/activePrice")
    public ResponseEntity<PriceResponseDto> getEndpoint(@RequestParam("dateTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime datetime,
                                                       @RequestParam("productId") int productId,
                                                       @RequestParam("brandId") int brandId) {

        log.info("Request received with parameters BrandID {}, ProductId {} and dateTime {} ",brandId,productId,datetime);
        PriceResponseDto priceResponseDto = priceService.findActivePrices(brandId,productId,datetime);
        log.info("Price founded with Price List {} will be returned ",priceResponseDto.getPriceList());
        return ResponseEntity.status(HttpStatus.OK).body(priceResponseDto);
    }

}

