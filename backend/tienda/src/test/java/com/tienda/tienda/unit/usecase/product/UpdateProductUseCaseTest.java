package com.tienda.tienda.unit.usecase.product;

import com.tienda.tienda.product.application.helper.PriceCalculator;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.product.application.usecase.UpdateProductUseCase;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import com.tienda.tienda.product.domain.repository.UpdateProductRepository;
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
class UpdateProductUseCaseTest {

    @Mock private GetProductRepository getProductRepository;
    @Mock private UpdateProductRepository updateProductRepository;
    @Mock private PromotionLoader promotionLoader;
    @Mock private PriceCalculator priceCalculator;

    @InjectMocks
    private UpdateProductUseCase updateProductUseCase;

    private Product testProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(20.0);
        return product;
    }

    @Test
    void execute_shouldUpdateAndReturnProduct() {
        Product product = testProduct();
        when(getProductRepository.findById(1)).thenReturn(Mono.just(product));
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(product));
        when(updateProductRepository.save(any(Product.class))).thenReturn(Mono.just(product));

        Product changes = new Product();
        changes.setPrice(200.0);

        StepVerifier.create(updateProductUseCase.execute(1, changes))
                .expectNextMatches(result -> result.getPrice() == 200.0)
                .verifyComplete();

        verify(updateProductRepository, times(1)).save(any(Product.class));
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getProductRepository.findById(999)).thenReturn(Mono.empty());

        Product changes = new Product();
        changes.setPrice(200.0);

        StepVerifier.create(updateProductUseCase.execute(999, changes))
                .verifyComplete();
    }
}