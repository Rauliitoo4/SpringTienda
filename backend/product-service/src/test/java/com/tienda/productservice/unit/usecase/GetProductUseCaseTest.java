package com.tienda.productservice.unit.usecase;

import com.tienda.productservice.application.port.output.GetProductOutputPort;
import com.tienda.productservice.application.service.PromotionLoader;
import com.tienda.productservice.application.usecase.GetProductUseCase;
import com.tienda.productservice.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductUseCaseTest {

    @Mock private GetProductOutputPort getProductOutputPort;
    @Mock private PromotionLoader promotionLoader;

    @InjectMocks
    private GetProductUseCase getProductUseCase;

    private Product testProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(20.0);
        product.setPromotions(List.of());
        return product;
    }

    @Test
    void execute_shouldReturnProductWithPromotions() {
        Product product = testProduct();

        when(getProductOutputPort.findById(1)).thenReturn(Mono.just(product));
        when(promotionLoader.loadPromotions(product)).thenReturn(Mono.just(product));

        StepVerifier.create(getProductUseCase.execute(1))
                .expectNextMatches(result -> result.getId() == 1)
                .verifyComplete();

        verify(promotionLoader, times(1)).loadPromotions(product);
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getProductOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(getProductUseCase.execute(99))
                .verifyComplete();

        verify(promotionLoader, never()).loadPromotions(any());
    }

    @Test
    void executeAll_shouldReturnAllProductsWithPromotions() {
        Product product = testProduct();

        when(getProductOutputPort.findAll()).thenReturn(Flux.just(product));
        when(promotionLoader.loadPromotions(product)).thenReturn(Mono.just(product));

        StepVerifier.create(getProductUseCase.executeAll())
                .expectNextCount(1)
                .verifyComplete();

        verify(promotionLoader, times(1)).loadPromotions(product);
    }
}