package com.tienda.productservice.unit.usecase;

import com.tienda.productservice.application.port.output.CreateProductOutputPort;
import com.tienda.productservice.application.service.PromotionLoader;
import com.tienda.productservice.application.usecase.CreateProductUseCase;
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
class CreateProductUseCaseTest {

    @Mock private CreateProductOutputPort createProductOutputPort;
    @Mock private PromotionLoader promotionLoader;

    @InjectMocks
    private CreateProductUseCase createProductUseCase;

    @Test
    void execute_shouldCreateProductAndLoadPromotions() {
        Product product = new Product();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(20.0);
        product.setPromotions(List.of());

        when(createProductOutputPort.save(any(Product.class))).thenReturn(Mono.just(product));
        when(promotionLoader.loadPromotions(product)).thenReturn(Mono.just(product));

        StepVerifier.create(createProductUseCase.execute(product))
                .expectNextMatches(result -> result.getId() == 1 && result.getName().equals("Camiseta"))
                .verifyComplete();

        verify(createProductOutputPort, times(1)).save(product);
        verify(promotionLoader, times(1)).loadPromotions(product);
    }
}