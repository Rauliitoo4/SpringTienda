package com.tienda.tienda.unit.usecase.product;

import com.tienda.tienda.carrito.application.helper.LineasCarritoUpdater;
import com.tienda.tienda.product.application.helper.PriceCalculator;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.product.application.usecase.AddPromotionUseCase;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import com.tienda.tienda.product.domain.repository.UpdateProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddPromotionUseCaseTest {

    @Mock private GetProductRepository getProductRepository;
    @Mock private UpdateProductRepository updateProductRepository;
    @Mock private ProductPromotionRepository productPromotionRepository;
    @Mock private PromotionLoader promotionLoader;
    @Mock private LineasCarritoUpdater lineasCarritoUpdater;
    @Mock private PriceCalculator priceCalculator;

    @InjectMocks
    private AddPromotionUseCase addPromotionUseCase;

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
    void execute_ifRelationNotExists_shouldAddPromotionAndRecalculate() {
        Product product = testProduct();
        when(getProductRepository.findById(1)).thenReturn(Mono.just(product));
        when(productPromotionRepository.existsRelation(1, 1)).thenReturn(Mono.just(0));
        when(productPromotionRepository.insertRelation(1, 1)).thenReturn(Mono.empty());
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(product));
        when(updateProductRepository.save(any(Product.class))).thenReturn(Mono.just(product));
        when(lineasCarritoUpdater.updateLineas(any(Product.class))).thenReturn(Mono.empty());

        StepVerifier.create(addPromotionUseCase.execute(1, 1))
                .expectNextMatches(result -> result.getName().equals("Camiseta"))
                .verifyComplete();

        verify(productPromotionRepository, times(1)).insertRelation(1, 1);
        verify(updateProductRepository, times(1)).save(any(Product.class));
    }

    @Test
    void execute_ifRelationExists_shouldReturnProductWithPromotions() {
        Product product = testProduct();
        when(getProductRepository.findById(1)).thenReturn(Mono.just(product));
        when(productPromotionRepository.existsRelation(1, 1)).thenReturn(Mono.just(1));
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(addPromotionUseCase.execute(1, 1))
                .expectNextMatches(result -> result.getName().equals("Camiseta"))
                .verifyComplete();

        verify(productPromotionRepository, never()).insertRelation(anyInt(), anyInt());
        verify(updateProductRepository, never()).save(any(Product.class));
    }

    @Test
    void execute_ifProductNotExists_shouldReturnEmpty() {
        when(getProductRepository.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(addPromotionUseCase.execute(999, 1))
                .verifyComplete();
    }
}