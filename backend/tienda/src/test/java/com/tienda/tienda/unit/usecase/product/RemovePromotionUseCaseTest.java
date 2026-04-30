package com.tienda.tienda.unit.usecase.product;

import com.tienda.tienda.carrito.application.service.LineasCarritoUpdater;
import com.tienda.tienda.product.application.service.PriceCalculator;
import com.tienda.tienda.product.application.service.PromotionLoader;
import com.tienda.tienda.product.application.usecase.RemovePromotionUseCase;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemovePromotionUseCaseTest {

    @Mock private GetProductOutputPort getProductOutputPort;
    @Mock private UpdateProductOutputPort updateProductOutputPort;
    @Mock private ProductPromotionOutputPort productPromotionOutputPort;
    @Mock private PromotionLoader promotionLoader;
    @Mock private LineasCarritoUpdater lineasCarritoUpdater;
    @Mock private PriceCalculator priceCalculator;

    @InjectMocks
    private RemovePromotionUseCase removePromotionUseCase;

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
    void execute_shouldRemovePromotionAndRecalculate() {
        Product product = testProduct();
        when(getProductOutputPort.findById(1)).thenReturn(Mono.just(product));
        when(productPromotionOutputPort.deleteByProductIdAndPromotionId(1, 1)).thenReturn(Mono.empty());
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(product));
        when(updateProductOutputPort.save(any(Product.class))).thenReturn(Mono.just(product));
        when(lineasCarritoUpdater.updateLineas(any(Product.class))).thenReturn(Mono.empty());

        StepVerifier.create(removePromotionUseCase.execute(1, 1))
                .expectNextMatches(result -> result.getName().equals("Camiseta"))
                .verifyComplete();

        verify(productPromotionOutputPort, times(1)).deleteByProductIdAndPromotionId(1, 1);
        verify(updateProductOutputPort, times(1)).save(any(Product.class));
    }

    @Test
    void execute_ifProductNotExists_shouldReturnEmpty() {
        when(getProductOutputPort.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(removePromotionUseCase.execute(999, 1))
                .verifyComplete();
    }
}