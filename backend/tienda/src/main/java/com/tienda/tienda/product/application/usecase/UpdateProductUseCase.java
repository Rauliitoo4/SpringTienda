package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.UpdateProductRepository;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.product.application.helper.PriceCalculator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProductUseCase {

    private final UpdateProductRepository updateProductRepository;
    private final GetProductRepository getProductRepository;
    private final PromotionLoader promotionLoader;
    private final PriceCalculator priceCalculator;

    public UpdateProductUseCase (UpdateProductRepository updateProductRepository, GetProductRepository getProductRepository, PromotionLoader promotionLoader, PriceCalculator priceCalculator) {
        this.updateProductRepository = updateProductRepository;
        this.promotionLoader = promotionLoader;
        this.priceCalculator = priceCalculator;
        this.getProductRepository = getProductRepository;
    }

    public Mono<Product> execute(int id, Product updatedProduct) {
        return getProductRepository.findById(id)
                .flatMap(promotionLoader::loadPromotions)
                .flatMap(product -> {
                    if (updatedProduct.getName() != null) product.setName(updatedProduct.getName());
                    if (updatedProduct.getPrice() > 0) {
                        product.setPrice(updatedProduct.getPrice());
                        priceCalculator.recalculateFinalPrice(product);
                    }
                    if (updatedProduct.getDescription() != null) product.setDescription(updatedProduct.getDescription());
                    if (updatedProduct.getMaterial() != null) product.setMaterial(updatedProduct.getMaterial());
                    if (updatedProduct.getConsiderations() != null) product.setConsiderations(updatedProduct.getConsiderations());
                    if (updatedProduct.getImageUrl() != null) product.setImageUrl(updatedProduct.getImageUrl());
                    return updateProductRepository.save(product);
                });
    }
}
