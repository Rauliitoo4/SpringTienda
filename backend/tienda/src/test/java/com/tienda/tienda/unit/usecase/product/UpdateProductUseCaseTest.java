package com.tienda.tienda.unit.usecase.product;

import com.tienda.tienda.product.application.helper.PriceCalculator;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.product.application.usecase.UpdateProductUseCase;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.application.port.output.GetProductOutputPort;
import com.tienda.tienda.product.application.port.output.UpdateProductOutputPort;
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

    @Mock private GetProductOutputPort getProductOutputPort;
    @Mock private UpdateProductOutputPort updateProductOutputPort;
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
        when(getProductOutputPort.findById(1)).thenReturn(Mono.just(product));
        when(promotionLoader.loadPromotions(any(Product.class))).thenReturn(Mono.just(product));
        when(updateProductOutputPort.save(any(Product.class))).thenReturn(Mono.just(product));

        Product changes = new Product();
        changes.setPrice(200.0);

        StepVerifier.create(updateProductUseCase.execute(1, changes))
                .expectNextMatches(result -> result.getPrice() == 200.0)
                .verifyComplete();

        verify(updateProductOutputPort, times(1)).save(any(Product.class));
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getProductOutputPort.findById(999)).thenReturn(Mono.empty());

        Product changes = new Product();
        changes.setPrice(200.0);

        StepVerifier.create(updateProductUseCase.execute(999, changes))
                .verifyComplete();
    }
}