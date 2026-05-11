package com.tienda.productservice.unit.usecase;

import com.tienda.productservice.application.port.output.GetProductOutputPort;
import com.tienda.productservice.application.port.output.UpdateProductOutputPort;
import com.tienda.productservice.application.service.PromotionLoader;
import com.tienda.productservice.application.usecase.UpdateProductUseCase;
import com.tienda.productservice.domain.model.Product;
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
class UpdateProductUseCaseTest {

    @Mock private UpdateProductOutputPort updateProductOutputPort;
    @Mock private GetProductOutputPort getProductOutputPort;
    @Mock private PromotionLoader promotionLoader;

    @InjectMocks
    private UpdateProductUseCase updateProductUseCase;

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
    void execute_shouldUpdateFieldsAndSave() {
        Product existing = testProduct();
        Product updated = new Product();
        updated.setName("Camiseta Actualizada");
        updated.setPrice(25.0);

        Product savedProduct = testProduct();
        savedProduct.setName("Camiseta Actualizada");
        savedProduct.setPrice(25.0);
        savedProduct.setFinalPrice(25.0);

        when(getProductOutputPort.findById(1)).thenReturn(Mono.just(existing));
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(savedProduct));
        when(updateProductOutputPort.save(any(Product.class))).thenReturn(Mono.just(savedProduct));

        StepVerifier.create(updateProductUseCase.execute(1, updated))
                .expectNextMatches(result ->
                        result.getName().equals("Camiseta Actualizada") && result.getPrice() == 25.0)
                .verifyComplete();

        verify(updateProductOutputPort, times(1)).save(any(Product.class));
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getProductOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(updateProductUseCase.execute(99, testProduct()))
                .verifyComplete();

        verify(updateProductOutputPort, never()).save(any());
    }
}