package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.ProductRepository;
import com.tienda.tienda.product.application.dto.ProductRequest;
import com.tienda.tienda.product.application.dto.ProductResponse;
import com.tienda.tienda.product.application.mapper.ProductResponseMapper;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final PromotionLoader promotionLoader;
    private final ProductResponseMapper mapper;

    public CreateProductUseCase (ProductRepository productRepository, PromotionLoader promotionLoader, ProductResponseMapper mapper) {
        this.productRepository = productRepository;
        this.promotionLoader = promotionLoader;
        this.mapper = mapper;
    }

    public Mono<ProductResponse> execute(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setFinalPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setMaterial(request.getMaterial());
        product.setConsiderations(request.getConsiderations());
        product.setImageUrl(request.getImageUrl());

        return productRepository.save(product)
                .flatMap(promotionLoader::loadPromotions)
                .map(mapper::toResponse);
    }
}
