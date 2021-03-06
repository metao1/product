package com.metao.product.checkout.service.impl;

import com.metao.product.checkout.clients.ProductCatalogRestClient;
import com.metao.product.models.ProductDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class PriceCalculatorServiceTest {

    @Mock
    private ProductCatalogRestClient productCatalogRestClient;

    @InjectMocks
    private PriceCalculatorService priceCalculatorService;

    @Test
    void calculateTotalPriceInCart() {
        @Pattern(regexp = "^[0-9]{10}") String ASIN = "1234567890";
        @Pattern(regexp = "^[0-9]{10}") String ASIN2 = "1234567891";
        ProductDTO productDTO = ProductDTO.builder()
                .asin(ASIN)
                .title("title")
                .price(109d)
                .build();
        ProductDTO productDTO2 = ProductDTO.builder()
                .asin(ASIN2)
                .title("title2")
                .price(200d)
                .build();
        when(productCatalogRestClient.getProductDetails(ASIN))
                .thenReturn(productDTO);
        when(productCatalogRestClient.getProductDetails(ASIN2))
                .thenReturn(productDTO2);
        Map<String, Integer> productKeyAndQuantityMap = new HashMap<>();
        productKeyAndQuantityMap.put(ASIN, 2);
        productKeyAndQuantityMap.put(ASIN2, 1);
        Double totalPrice = priceCalculatorService.calculateTotalPriceInCart(productKeyAndQuantityMap);
        assertThat(totalPrice).isEqualTo(2 * 109d + 200d);
    }
}