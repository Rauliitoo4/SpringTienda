package com.tienda.tienda.unit.usecase.product;

import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.product.application.usecase.CreateProductUseCase;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.CreateProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock private CreateProductRepository createProductRepository;
    @Mock private PromotionLoader promotionLoader;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    private Product testProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(20.0);
        return product;
    }

    @Test
    void execute_shouldSaveAndReturnProduct() {
        Product product = testProduct();
        when(createProductRepository.save(any(Product.class))).thenReturn(Mono.just(product));
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(createProductUseCase.execute(product))
                .expectNextMatches(result ->
                        result.getName().equals("Camiseta") &&
                                result.getPrice() == 20.0)
                .verifyComplete();

        verify(createProductRepository, times(1)).save(any(Product.class));
    }
}