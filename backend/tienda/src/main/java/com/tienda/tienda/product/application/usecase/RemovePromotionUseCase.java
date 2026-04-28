package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import com.tienda.tienda.product.domain.repository.UpdateProductRepository;
import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import com.tienda.tienda.product.application.helper.PriceCalculator;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.carrito.application.helper.LineasCarritoUpdater;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RemovePromotionUseCase {

    private final GetProductRepository getProductRepository;
    private final UpdateProductRepository updateProductRepository;
    private final ProductPromotionRepository productPromotionRepository;
    private final PromotionLoader promotionLoader;
    private final LineasCarritoUpdater lineasCarritoUpdater;
    private final PriceCalculator priceCalculator = new PriceCalculator();

    public RemovePromotionUseCase(GetProductRepository getProductRepository, UpdateProductRepository updateProductRepository, ProductPromotionRepository productPromotionRepository, PromotionLoader promotionLoader, LineasCarritoUpdater lineasCarritoUpdater) {
        this.getProductRepository = getProductRepository;
        this.updateProductRepository = updateProductRepository;
        this.productPromotionRepository = productPromotionRepository;
        this.promotionLoader = promotionLoader;
        this.lineasCarritoUpdater = lineasCarritoUpdater;
    }

    public Mono<Product> execute(int productId, int promotionId) {
        return getProductRepository.findById(productId)
                .flatMap(product ->
                        productPromotionRepository.deleteByProductIdAndPromotionId(productId, promotionId)
                                .then(promotionLoader.loadPromotions(product))
                                .flatMap(p -> {
                                    priceCalculator.recalculateFinalPrice(p);
                                    return updateProductRepository.save(p)
                                            .then(lineasCarritoUpdater.updateLineas(p))
                                            .thenReturn(p);
                                })
                );
    }
}