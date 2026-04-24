package com.tienda.tienda.unit.service;

import com.tienda.tienda.product.application.dto.ProductResponse;
import com.tienda.tienda.product.application.mapper.ProductResponseMapper;
import com.tienda.tienda.product.infraestructure.output.persistence.entity.ProductEntity;
import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import com.tienda.tienda.product.domain.repository.ProductRepository;
import com.tienda.tienda.promotion.application.port.PromotionRepositoryPort;
import com.tienda.tienda.lineacarrito.application.service.LineaCarritoService;
import com.tienda.tienda.product.application.service.ProductServiceImpl;
import com.tienda.tienda.product.domain.service.PriceCalculator;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductEntityServiceImplTest {

    @Mock private ProductRepository productRepo;
    @Mock private PromotionRepositoryPort promotionRepo;
    @Mock private ProductPromotionRepository productPromotionRepo;
    @Mock private ProductResponseMapper productResponseMapper;
    @Mock private PromotionLoader promotionLoader;
    @Mock private LineaCarritoService lineaCarritoService;
    @Mock private PriceCalculator priceCalculator;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private ProductEntity testingProduct() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1);
        productEntity.setName("Camiseta");
        productEntity.setPrice(20.0);
        productEntity.setFinalPrice(20.0);
        productEntity.setPromotions(List.of());
        return productEntity;
    }

    private ProductResponse testingDto() {
        ProductResponse dto = new ProductResponse();
        dto.setId(1);
        dto.setName("Camiseta");
        dto.setPrice(20.0);
        dto.setFinalPrice(20.0);
        return dto;
    }

    private void mockLoadPromotions() {
        when(promotionLoader.loadPromotions(any(ProductEntity.class)))
                .thenReturn(Mono.just(testingProduct()));
    }

    @Test
    void getProductById_shouldReturn_Product() {
        when(productRepo.findById(1)).thenReturn(Mono.just(testingProduct()));
        mockLoadPromotions();
        when(productResponseMapper.toDTO(any(ProductEntity.class))).thenReturn(testingDto());

        StepVerifier.create(productServiceImpl.getProductById(1))
                .expectNextMatches(dto ->
                        dto.getName().equals("Camiseta") &&
                        dto.getPrice() == 20.0)
                .verifyComplete();
    }

    @Test
    void getProductById_ifNotExists_shouldReturnNull() {
        when(productRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(productServiceImpl.getProductById(999))
                .verifyComplete();
    }

    @Test
    void createProduct_shouldSaveAndReturnDTO() {
        ProductEntity savedProductEntity = testingProduct();
        when(productResponseMapper.toEntity(any(ProductResponse.class))).thenReturn(savedProductEntity);
        when(productRepo.save(any(ProductEntity.class))).thenReturn(Mono.just(savedProductEntity));
        mockLoadPromotions();
        when(productResponseMapper.toDTO(any(ProductEntity.class))).thenReturn(testingDto());

        ProductResponse dto = new ProductResponse();
        dto.setName("Camiseta");
        dto.setPrice(20.0);

        StepVerifier.create(productServiceImpl.createProduct(dto))
                .expectNextMatches(result -> result.getName().equals("Camiseta"))
                .verifyComplete();
        verify(productRepo, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void deleteProduct_ifExists_shouldReturnTrue() {
        when(productRepo.existsById(1)).thenReturn(Mono.just(true));
        when(productRepo.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(productServiceImpl.deleteProduct(1))
                .expectNext(true)
                .verifyComplete();
        verify(productRepo, times(1)).deleteById(1);
    }

    @Test
    void deleteProduct_ifNotExists_shouldReturnFalse() {
        when(productRepo.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(productServiceImpl.deleteProduct(999))
                .expectNext(false)
                .verifyComplete();
        verify(productRepo, never()).deleteById(anyInt());
    }

    @Test
    void updateProduct_shouldUpdate_Product() {
        ProductEntity productEntity = testingProduct();
        when(productRepo.findById(1)).thenReturn(Mono.just(productEntity));
        mockLoadPromotions();
        when(productRepo.save(any(ProductEntity.class))).thenReturn(Mono.just(productEntity));

        ProductResponse updatedDto = new ProductResponse();
        updatedDto.setId(1);
        updatedDto.setName("Camiseta");
        updatedDto.setPrice(200.0);
        updatedDto.setFinalPrice(200.0);
        when(productResponseMapper.toDTO(any(ProductEntity.class))).thenReturn(updatedDto);

        ProductResponse dto = new ProductResponse();
        dto.setPrice(200.0);

        StepVerifier.create(productServiceImpl.updateProduct(1, dto))
                .expectNextMatches(result -> result.getPrice() == 200.00)
                .verifyComplete();
    }
}

