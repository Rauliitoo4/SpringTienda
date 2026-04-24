package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetProductUseCase {

    private final GetProductRepository getProductRepository;
    private final PromotionLoader promotionLoader;

    public GetProductUseCase (GetProductRepository getProductRepository, PromotionLoader promotionLoader) {
        this.getProductRepository = getProductRepository;
        this.promotionLoader = promotionLoader;
    }

    public Mono<Product> execute(int id) {
        return getProductRepository.findById(id)
                .flatMap(promotionLoader::loadPromotions);
    }

    public Flux<Product> executeAll() {
        return getProductRepository.findAll()
                .flatMap(promotionLoader::loadPromotions);
    }
}
