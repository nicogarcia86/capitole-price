package com.capitole.price.controller;


import com.capitole.price.exception.BusinessException;
import com.capitole.price.exception.ExceptionManager;
import com.capitole.price.factory.PriceResponseDtoFactory;
import com.capitole.price.model.dto.PriceResponseDto;
import com.capitole.price.service.impl.PriceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.equalTo;



@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PriceControllerTest {

    @Mock
    private PriceServiceImpl priceService;

    @InjectMocks
    private PriceController priceController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(priceController)
                .setControllerAdvice(new ExceptionManager())
                .build();
    }

    @Test
    public void testGetEndpoint_PriceFound_Returns200AndPriceResponseDto() throws Exception {
        // Given
        int brandId = 1;
        int productId = 1;
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String currentString = currentDate.format(formatter);
        LocalDateTime dateTime = LocalDateTime.parse(currentString, formatter);

        PriceResponseDto expectedResponseDto = PriceResponseDtoFactory.getDefaultPriceResponseDto();

        when(priceService.findActivePrices(brandId, productId, dateTime)).thenReturn(expectedResponseDto);

        // When
        mockMvc.perform(get("/prices/activePrice")
                        .param("dateTime", currentString)
                        .param("productId", String.valueOf(productId))
                        .param("brandId", String.valueOf(brandId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand_id", equalTo(expectedResponseDto.getBrandId())))
                .andExpect(jsonPath("$.product_id", equalTo(expectedResponseDto.getProductId())))
                .andExpect(jsonPath("$.price_list", equalTo(expectedResponseDto.getPriceList())))
                .andExpect(jsonPath("$.start_date", equalTo(expectedResponseDto.getStartDate().format(formatter))))
                .andExpect(jsonPath("$.end_date", equalTo(expectedResponseDto.getEndDate().format(formatter))))
                .andExpect(jsonPath("$.price", equalTo(expectedResponseDto.getPrice())))
                .andReturn();

        // Then
        verify(priceService).findActivePrices(brandId, productId, dateTime);
    }

    @Test
    public void testGetEndpoint_PriceNotFound_Returns404AndErrorDto() throws Exception {
        // Given
        int brandId = 1;
        int productId = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String currentString = "2021-06-17T21:00:00";
        LocalDateTime dateTime = LocalDateTime.parse(currentString, formatter);


        // When
        when(priceService.findActivePrices(brandId, productId, dateTime))
                .thenThrow(new BusinessException("E200", HttpStatus.NOT_FOUND
                        , String.format("There is not prices for brand %d, product %d and date %s",brandId,productId,currentString)));


        mockMvc.perform(get("/prices/activePrice")
                        .param("dateTime", currentString)
                        .param("productId", String.valueOf(productId))
                        .param("brandId", String.valueOf(brandId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(String.format("There is not prices for brand %d, product %d and date %s",brandId,productId,currentString))))
                .andReturn();

        // Then
        verify(priceService).findActivePrices(brandId, productId, dateTime);
    }
}