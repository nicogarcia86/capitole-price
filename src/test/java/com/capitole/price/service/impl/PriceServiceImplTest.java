package com.capitole.price.service.impl;


import com.capitole.price.exception.BusinessException;
import com.capitole.price.factory.PriceEntityFactory;
import com.capitole.price.model.dto.PriceResponseDto;
import com.capitole.price.model.entity.PriceEntity;
import com.capitole.price.respository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PriceServiceImplTest {

    @Mock
    private PriceRepository priceRepository;
    @InjectMocks
    private PriceServiceImpl priceService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findActivePrices_PriceExists_ReturnsPriceResponseDto() {
        // Given
        int brandId = 1;
        int productId = 1;
        LocalDateTime currentDate = LocalDateTime.now();

        List<PriceEntity> prices = new ArrayList<>();
        PriceEntity priceEntity = PriceEntityFactory.defultPriceEntity();
        prices.add(priceEntity);

        Mockito.when(priceRepository.findActivePrices(brandId, productId, currentDate)).thenReturn(prices);

        // When
        PriceResponseDto result = priceService.findActivePrices(brandId, productId, currentDate);

        // Then
        assertNotNull(result);
        assertEquals(priceEntity.getBrandId(), result.getBrandId());
        assertEquals(priceEntity.getProductId(), result.getProductId());
        assertEquals(priceEntity.getPrice(), result.getPrice());
        assertEquals(priceEntity.getPriceList(), result.getPriceList());
        verify(priceRepository, times(1)).findActivePrices(brandId, productId, currentDate);
    }

    @Test
    void findActivePrices_PriceDoesNotExist_ThrowsBusinessException() {
        // Given
        int brandId = 1;
        int productId = 1;
        LocalDateTime currentDate = LocalDateTime.now();
        List<PriceEntity> prices = new ArrayList<>();

        Mockito.when(priceRepository.findActivePrices(brandId, productId, currentDate)).thenReturn(prices);

        // When
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            priceService.findActivePrices(brandId, productId, currentDate);
        });

        // Then
        assertEquals("E200", exception.getCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("There is not prices for brand 1, product 1 and date " + currentDate, exception.getMessage());
        verify(priceRepository, times(1)).findActivePrices(brandId, productId, currentDate);
    }
}
