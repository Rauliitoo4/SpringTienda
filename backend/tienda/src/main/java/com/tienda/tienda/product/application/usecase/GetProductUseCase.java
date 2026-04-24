package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.repository.ProductRepository;
import com.tienda.tienda.product.application.dto.ProductResponse;
import com.tienda.tienda.product.application.mapper.ProductResponseMapper;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetProductUseCase {

    private final ProductRepository productRepository;
    private final PromotionLoader promotionLoader;
    private final ProductResponseMapper mapper;

    public GetProductUseCase (ProductRepository productRepository, PromotionLoader promotionLoader, ProductResponseMapper mapper) {
        this.productRepository = productRepository;
        this.promotionLoader = promotionLoader;
        this.mapper = mapper;
    }

    public Mono<ProductResponse> execute(int id) {
        return productRepository.findById(id)
                .flatMap(promotionLoader::loadPromotions)
                .map(mapper::toResponse);
    }

    public Flux<ProductResponse> executeAll() {
        return productRepository.findAll()
                .flatMap(promotionLoader::loadPromotions)
                .map(mapper::toResponse);
    }
}
