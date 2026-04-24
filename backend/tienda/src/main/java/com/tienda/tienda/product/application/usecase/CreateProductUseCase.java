package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.CreateProductRepository;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateProductUseCase {

    private final CreateProductRepository createProductRepository;
    private final PromotionLoader promotionLoader;

    public CreateProductUseCase (CreateProductRepository createProductRepository, PromotionLoader promotionLoader) {
        this.createProductRepository = createProductRepository;
        this.promotionLoader = promotionLoader;
    }

    public Mono<Product> execute(Product product) {
        return createProductRepository.save(product)
                .flatMap(promotionLoader::loadPromotions);
    }
}
