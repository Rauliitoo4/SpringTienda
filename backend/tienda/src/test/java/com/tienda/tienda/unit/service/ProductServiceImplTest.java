package com.tienda.tienda.unit.service;

import com.tienda.tienda.product.application.dto.ProductDTO;
import com.tienda.tienda.product.application.dto.mapper.ProductMapper;
import com.tienda.tienda.product.domain.Product;
import com.tienda.tienda.product.application.port.ProductPromotionRepositoryPort;
import com.tienda.tienda.product.application.port.ProductRepositoryPort;
import com.tienda.tienda.promotion.application.port.PromotionRepositoryPort;
import com.tienda.tienda.lineacarrito.application.service.LineaCarritoService;
import com.tienda.tienda.product.application.service.ProductServiceImpl;
import com.tienda.tienda.product.application.service.helper.PriceCalculator;
import com.tienda.tienda.product.application.service.helper.PromotionLoader;
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
class ProductServiceImplTest {

    @Mock private ProductRepositoryPort productRepo;
    @Mock private PromotionRepositoryPort promotionRepo;
    @Mock private ProductPromotionRepositoryPort productPromotionRepo;
    @Mock private ProductMapper productMapper;
    @Mock private PromotionLoader promotionLoader;
    @Mock private LineaCarritoService lineaCarritoService;
    @Mock private PriceCalculator priceCalculator;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private Product testingProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Camiseta");
        product.setPrice(20.0);
        product.setFinalPrice(20.0);
        product.setPromotions(List.of());
        return product;
    }

    private ProductDTO testingDto() {
        ProductDTO dto = new ProductDTO();
        dto.setId(1);
        dto.setName("Camiseta");
        dto.setPrice(20.0);
        dto.setFinalPrice(20.0);
        return dto;
    }

    private void mockLoadPromotions() {
        when(promotionLoader.loadPromotions(any(Product.class)))
                .thenReturn(Mono.just(testingProduct()));
    }

    @Test
    void getProductById_shouldReturn_Product() {
        when(productRepo.findById(1)).thenReturn(Mono.just(testingProduct()));
        mockLoadPromotions();
        when(productMapper.toDTO(any(Product.class))).thenReturn(testingDto());

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
        Product savedProduct = testingProduct();
        when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(savedProduct);
        when(productRepo.save(any(Product.class))).thenReturn(Mono.just(savedProduct));
        mockLoadPromotions();
        when(productMapper.toDTO(any(Product.class))).thenReturn(testingDto());

        ProductDTO dto = new ProductDTO();
        dto.setName("Camiseta");
        dto.setPrice(20.0);

        StepVerifier.create(productServiceImpl.createProduct(dto))
                .expectNextMatches(result -> result.getName().equals("Camiseta"))
                .verifyComplete();
        verify(productRepo, times(1)).save(any(Product.class));
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
        Product product = testingProduct();
        when(productRepo.findById(1)).thenReturn(Mono.just(product));
        mockLoadPromotions();
        when(productRepo.save(any(Product.class))).thenReturn(Mono.just(product));

        ProductDTO updatedDto = new ProductDTO();
        updatedDto.setId(1);
        updatedDto.setName("Camiseta");
        updatedDto.setPrice(200.0);
        updatedDto.setFinalPrice(200.0);
        when(productMapper.toDTO(any(Product.class))).thenReturn(updatedDto);

        ProductDTO dto = new ProductDTO();
        dto.setPrice(200.0);

        StepVerifier.create(productServiceImpl.updateProduct(1, dto))
                .expectNextMatches(result -> result.getPrice() == 200.00)
                .verifyComplete();
    }
}

