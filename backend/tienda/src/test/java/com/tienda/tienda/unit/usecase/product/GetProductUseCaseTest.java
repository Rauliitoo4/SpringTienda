package com.tienda.tienda.unit.usecase.product;

import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.product.application.usecase.GetProductUseCase;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductUseCaseTest {

    @Mock private GetProductRepository getProductRepository;
    @Mock private PromotionLoader promotionLoader;

    @InjectMocks
    private GetProductUseCase getProductUseCase;

    private Product testProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(20.0);
        return product;
    }

    @Test
    void execute_shouldReturnProduct() {
        Product product = testProduct();
        when(getProductRepository.findById(1)).thenReturn(Mono.just(product));
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(getProductUseCase.execute(1))
                .expectNextMatches(result ->
                        result.getName().equals("Camiseta") &&
                                result.getPrice() == 20.0)
                .verifyComplete();
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getProductRepository.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(getProductUseCase.execute(999))
                .verifyComplete();
    }

    @Test
    void executeAll_shouldReturnAllProducts() {
        Product product = testProduct();
        when(getProductRepository.findAll()).thenReturn(Flux.just(product));
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(getProductUseCase.executeAll())
                .expectNextMatches(result -> result.getName().equals("Camiseta"))
                .verifyComplete();
    }
}