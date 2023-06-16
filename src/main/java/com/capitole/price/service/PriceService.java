package com.capitole.price.service;

import com.capitole.price.model.dto.PriceResponseDto;

import java.time.LocalDateTime;

public interface PriceService {

    public PriceResponseDto findActivePrices(int brandId, int productId, LocalDateTime currentDate);
}
