package com.tienda.tienda.unit.usecase.product;

import com.tienda.tienda.carrito.application.helper.LineasCarritoUpdater;
import com.tienda.tienda.product.application.helper.PriceCalculator;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.product.application.usecase.AddPromotionUseCase;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.application.port.output.GetProductOutputPort;
import com.tienda.tienda.product.application.port.output.ProductPromotionOutputPort;
import com.tienda.tienda.product.application.port.output.UpdateProductOutputPort;
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

    @Mock private GetProductOutputPort getProductOutputPort;
    @Mock private UpdateProductOutputPort updateProductOutputPort;
    @Mock private ProductPromotionOutputPort productPromotionOutputPort;
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
        when(getProductOutputPort.findById(1)).thenReturn(Mono.just(product));
        when(productPromotionOutputPort.existsRelation(1, 1)).thenReturn(Mono.just(0));
        when(productPromotionOutputPort.insertRelation(1, 1)).thenReturn(Mono.empty());
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(product));
        when(updateProductOutputPort.save(any(Product.class))).thenReturn(Mono.just(product));
        when(lineasCarritoUpdater.updateLineas(any(Product.class))).thenReturn(Mono.empty());

        StepVerifier.create(addPromotionUseCase.execute(1, 1))
                .expectNextMatches(result -> result.getName().equals("Camiseta"))
                .verifyComplete();

        verify(productPromotionOutputPort, times(1)).insertRelation(1, 1);
        verify(updateProductOutputPort, times(1)).save(any(Product.class));
    }

    @Test
    void execute_ifRelationExists_shouldReturnProductWithPromotions() {
        Product product = testProduct();
        when(getProductOutputPort.findById(1)).thenReturn(Mono.just(product));
        when(productPromotionOutputPort.existsRelation(1, 1)).thenReturn(Mono.just(1));
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(product));

        StepVerifier.create(addPromotionUseCase.execute(1, 1))
                .expectNextMatches(result -> result.getName().equals("Camiseta"))
                .verifyComplete();

        verify(productPromotionOutputPort, never()).insertRelation(anyInt(), anyInt());
        verify(updateProductOutputPort, never()).save(any(Product.class));
    }

    @Test
    void execute_ifProductNotExists_shouldReturnEmpty() {
        when(getProductOutputPort.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(addPromotionUseCase.execute(999, 1))
                .verifyComplete();
    }
}