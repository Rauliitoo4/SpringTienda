package com.tienda.tienda.product.application.service;

import com.tienda.tienda.product.application.dto.ProductDTO;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;


public interface ProductService {
    Mono<ProductDTO> createProduct(ProductDTO dto);
    Mono<ProductDTO> updateProduct(int id, ProductDTO dto);
    Mono<Boolean> deleteProduct(int id);
    Mono<ProductDTO> getProductById(int id);
    Flux<ProductDTO> getAllProducts();
    Mono<ProductDTO> addPromotion(int productId, int promotionId);
    Mono<ProductDTO> removePromotion(int productId, int promotionId);
}
