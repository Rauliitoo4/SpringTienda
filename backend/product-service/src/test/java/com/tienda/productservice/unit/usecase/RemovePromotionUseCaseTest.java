package com.tienda.productservice.unit.usecase;

import com.tienda.productservice.application.port.output.GetProductOutputPort;
import com.tienda.productservice.application.port.output.ProductPromotionOutputPort;
import com.tienda.productservice.application.port.output.UpdateProductOutputPort;
import com.tienda.productservice.application.service.PromotionLoader;
import com.tienda.productservice.application.usecase.RemovePromotionUseCase;
import com.tienda.productservice.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemovePromotionUseCaseTest {

    @Mock private GetProductOutputPort getProductOutputPort;
    @Mock private UpdateProductOutputPort updateProductOutputPort;
    @Mock private ProductPromotionOutputPort productPromotionOutputPort;
    @Mock private PromotionLoader promotionLoader;

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
    void execute_shouldRemovePromotionAndUpdateProduct() {
        Product product = testProduct();

        when(getProductOutputPort.findById(1)).thenReturn(Mono.just(product));
        when(productPromotionOutputPort.deleteByProductIdAndPromotionId(1, 1)).thenReturn(Mono.empty());
        when(promotionLoader.loadPromotions(product)).thenReturn(Mono.just(product));
        when(updateProductOutputPort.save(product)).thenReturn(Mono.just(product));

        StepVerifier.create(removePromotionUseCase.execute(1, 1))
                .expectNextMatches(result -> result.getId() == 1)
                .verifyComplete();

        verify(productPromotionOutputPort, times(1)).deleteByProductIdAndPromotionId(1, 1);
        verify(updateProductOutputPort, times(1)).save(product);
    }

    @Test
    void execute_ifProductNotExists_shouldReturnEmpty() {
        when(getProductOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(removePromotionUseCase.execute(99, 1))
                .verifyComplete();

        verify(productPromotionOutputPort, never()).deleteByProductIdAndPromotionId(anyInt(), anyInt());
    }
}