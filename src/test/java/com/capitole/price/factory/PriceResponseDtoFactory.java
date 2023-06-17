package com.capitole.price.factory;

import com.capitole.price.model.dto.PriceResponseDto;
import java.time.LocalDateTime;

public class PriceResponseDtoFactory {

    public static PriceResponseDto getDefaultPriceResponseDto(){
        return PriceResponseDto.builder()
                .brandId(1)
                .productId(1)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .priceList(1)
                .price(25.25)
                .build();
    }
}
