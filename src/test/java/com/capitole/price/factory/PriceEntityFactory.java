package com.capitole.price.factory;

import com.capitole.price.model.entity.PriceEntity;
import java.time.LocalDateTime;

public class PriceEntityFactory {

    public static PriceEntity defultPriceEntity(){
        return PriceEntity.builder()
                .id(1)
                .brandId(1)
                .productId(1)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .price(25.25)
                .priceList(1)
                .currency("EUR")
                .build();
    }
}
