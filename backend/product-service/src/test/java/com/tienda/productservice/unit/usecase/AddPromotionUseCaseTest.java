package com.tienda.productservice.unit.usecase;

import com.tienda.productservice.application.port.output.GetProductOutputPort;
import com.tienda.productservice.application.port.output.ProductPromotionOutputPort;
import com.tienda.productservice.application.port.output.UpdateProductOutputPort;
import com.tienda.productservice.application.service.PromotionLoader;
import com.tienda.productservice.application.usecase.AddPromotionUseCase;
import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.domain.model.Promotion;
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
class AddPromotionUseCaseTest {

    @Mock private GetProductOutputPort getProductOutputPort;
    @Mock private UpdateProductOutputPort updateProductOutputPort;
    @Mock private ProductPromotionOutputPort productPromotionOutputPort;
    @Mock private PromotionLoader promotionLoader;

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
    void execute_shouldAddPromotionAndUpdateProduct() {
        Product product = testProduct();
        Product productWithPromotion = testProduct();
        productWithPromotion.setPromotions(List.of(new Promotion(1, 10.0, "Descuento")));
        productWithPromotion.setFinalPrice(18.0);

        when(getProductOutputPort.findById(1)).thenReturn(Mono.just(product));
        when(productPromotionOutputPort.existsRelation(1, 1)).thenReturn(Mono.just(0));
        when(productPromotionOutputPort.insertRelation(1, 1)).thenReturn(Mono.empty());
        when(promotionLoader.loadPromotions(product)).thenReturn(Mono.just(productWithPromotion));
        when(updateProductOutputPort.save(productWithPromotion)).thenReturn(Mono.just(productWithPromotion));

        StepVerifier.create(addPromotionUseCase.execute(1, 1))
                .expectNextMatches(result -> result.getFinalPrice() == 18.0)
                .verifyComplete();

        verify(productPromotionOutputPort, times(1)).insertRelation(1, 1);
        verify(updateProductOutputPort, times(1)).save(productWithPromotion);
    }

    @Test
    void execute_ifRelationAlreadyExists_shouldNotInsertAgain() {
        Product product = testProduct();
        Product productWithPromotion = testProduct();
        productWithPromotion.setPromotions(List.of(new Promotion(1, 10.0, "Descuento")));

        when(getProductOutputPort.findById(1)).thenReturn(Mono.just(product));
        when(productPromotionOutputPort.existsRelation(1, 1)).thenReturn(Mono.just(1));
        when(promotionLoader.loadPromotions(product)).thenReturn(Mono.just(productWithPromotion));

        StepVerifier.create(addPromotionUseCase.execute(1, 1))
                .expectNextMatches(result -> !result.getPromotions().isEmpty())
                .verifyComplete();

        verify(productPromotionOutputPort, never()).insertRelation(anyInt(), anyInt());
        verify(updateProductOutputPort, never()).save(any());
    }

    @Test
    void execute_ifProductNotExists_shouldReturnEmpty() {
        when(getProductOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(addPromotionUseCase.execute(99, 1))
                .verifyComplete();

        verify(productPromotionOutputPort, never()).insertRelation(anyInt(), anyInt());
    }
}