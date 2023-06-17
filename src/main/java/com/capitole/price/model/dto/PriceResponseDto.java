package com.capitole.price.model.dto;

import com.capitole.price.model.entity.PriceEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PriceResponseDto {

    @JsonProperty("product_id")
    private int productId;

    @JsonProperty("brand_id")
    private int brandId;

    @JsonProperty("price_list")
    private int priceList;

    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @JsonProperty("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @JsonProperty("price")
    private Double price;


    public static PriceResponseDto fromEntity(PriceEntity price) {
         return PriceResponseDto.builder()
                .brandId(price.getBrandId())
                .productId(price.getProductId())
                .priceList(price.getPriceList())
                .startDate(price.getStartDate())
                .endDate(price.getEndDate())
                .price(price.getPrice()).build();
    }
}
