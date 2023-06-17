package com.capitole.price.integration;


import com.capitole.price.model.dto.PriceResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("provider")
    public void testGetEndpoint_PriceFound_Returns200AndPriceResponseDto(int brandId, int productId,
                                            String currentString, PriceResponseDto expected) throws Exception {
        //Given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // When
        mockMvc.perform(get("/prices/activePrice")
                        .param("dateTime", currentString)
                        .param("productId", String.valueOf(productId))
                        .param("brandId", String.valueOf(brandId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand_id", equalTo(expected.getBrandId())))
                .andExpect(jsonPath("$.product_id", equalTo(expected.getProductId())))
                .andExpect(jsonPath("$.price_list", equalTo(expected.getPriceList())))
                .andExpect(jsonPath("$.start_date", equalTo(expected.getStartDate().format(formatter))))
                .andExpect(jsonPath("$.end_date", equalTo(expected.getEndDate().format(formatter))))
                .andExpect(jsonPath("$.price", equalTo(expected.getPrice())))
                .andReturn();
    }

    private static Stream<Arguments> provider() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        return Stream.of(
                Arguments.of(1, 35455, "2020-06-14T10:00:00",PriceResponseDto.builder()
                        .brandId(1)
                        .productId(35455)
                        .price(35.50)
                        .startDate(LocalDateTime.parse("2020-06-14T00:00:00", formatter))
                        .endDate(LocalDateTime.parse("2020-12-31T23:59:59", formatter))
                        .priceList(1)
                        .build()),
                Arguments.of(1, 35455, "2020-06-14T16:00:00",PriceResponseDto.builder()
                        .brandId(1)
                        .productId(35455)
                        .price(25.45)
                        .startDate(LocalDateTime.parse("2020-06-14T15:00:00", formatter))
                        .endDate(LocalDateTime.parse("2020-06-14T18:30:00", formatter))
                        .priceList(2)
                        .build()),
                Arguments.of(1, 35455, "2020-06-14T21:00:00",PriceResponseDto.builder()
                        .brandId(1)
                        .productId(35455)
                        .price(35.50)
                        .startDate(LocalDateTime.parse("2020-06-14T00:00:00", formatter))
                        .endDate(LocalDateTime.parse("2020-12-31T23:59:59", formatter))
                        .priceList(1)
                        .build()),
                Arguments.of(1, 35455, "2020-06-15T10:00:00",PriceResponseDto.builder()
                        .brandId(1)
                        .productId(35455)
                        .price(30.50)
                        .startDate(LocalDateTime.parse("2020-06-15T00:00:00", formatter))
                        .endDate(LocalDateTime.parse("2020-06-15T11:00:00", formatter))
                        .priceList(3)
                        .build()),
                Arguments.of(1, 35455, "2020-06-16T10:00:00",PriceResponseDto.builder()
                        .brandId(1)
                        .productId(35455)
                        .price(38.95)
                        .startDate(LocalDateTime.parse("2020-06-15T16:00:00", formatter))
                        .endDate(LocalDateTime.parse("2020-12-31T23:59:59", formatter))
                        .priceList(4)
                        .build())
        );
    }

    @Test
    public void testGetEndpoint_PriceNotFound_Returns404AndErrorDto() throws Exception {
        // Given
        int brandId = 1;
        int productId = 35455;
        String currentString = "2021-06-17T21:00:00";

        // When
        mockMvc.perform(get("/prices/activePrice")
                        .param("dateTime", currentString)
                        .param("productId", String.valueOf(productId))
                        .param("brandId", String.valueOf(brandId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(String.format("There is not prices for brand %d, product %d and date %s",brandId,productId,"2021-06-17T21:00"))))
                .andReturn();
    }
}
