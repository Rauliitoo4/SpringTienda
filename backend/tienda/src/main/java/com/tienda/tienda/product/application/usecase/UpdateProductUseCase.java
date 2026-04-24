package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.repository.ProductRepository;
import com.tienda.tienda.product.application.dto.ProductRequest;
import com.tienda.tienda.product.application.dto.ProductResponse;
import com.tienda.tienda.product.application.mapper.ProductResponseMapper;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.product.domain.service.PriceCalculator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProductUseCase {

    private final ProductRepository productRepository;
    private final PromotionLoader promotionLoader;
    private final ProductResponseMapper mapper;
    private final PriceCalculator priceCalculator;

    public UpdateProductUseCase (ProductRepository productRepository, PromotionLoader promotionLoader, ProductResponseMapper mapper, PriceCalculator priceCalculator) {
        this.productRepository = productRepository;
        this.promotionLoader = promotionLoader;
        this.mapper = mapper;
        this.priceCalculator = priceCalculator;
    }

    public Mono<ProductResponse> execute(int id, ProductRequest request) {
        return productRepository.findById(id)
                .flatMap(promotionLoader::loadPromotions)
                .flatMap(product -> {
                    if (request.getName() != null) product.setName(request.getName());
                    if (request.getPrice() > 0) {
                        product.setPrice(request.getPrice());
                        priceCalculator.recalculateFinalPrice(product);
                    }
                    if (request.getDescription() != null) product.setDescription(request.getDescription());
                    if (request.getMaterial() != null) product.setMaterial(request.getMaterial());
                    if (request.getConsiderations() != null) product.setConsiderations(request.getConsiderations());
                    if (request.getImageUrl() != null) product.setImageUrl(request.getImageUrl());
                    return productRepository.save(product);
                })
                .map(mapper::toResponse);
    }
}
