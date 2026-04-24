package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import com.tienda.tienda.product.domain.repository.UpdateProductRepository;
import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import com.tienda.tienda.product.domain.service.PriceCalculator;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.lineacarrito.application.service.LineaCarritoService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddPromotionUseCase {

    private final GetProductRepository getProductRepository;
    private final UpdateProductRepository updateProductRepository;
    private final ProductPromotionRepository productPromotionRepository;
    private final PromotionLoader promotionLoader;
    private final LineaCarritoService lineaCarritoService;
    private final PriceCalculator priceCalculator = new PriceCalculator();

    public AddPromotionUseCase(GetProductRepository getProductRepository, UpdateProductRepository updateProductRepository, ProductPromotionRepository productPromotionRepository, PromotionLoader promotionLoader, LineaCarritoService lineaCarritoService) {
        this.getProductRepository = getProductRepository;
        this.updateProductRepository = updateProductRepository;
        this.productPromotionRepository = productPromotionRepository;
        this.promotionLoader = promotionLoader;
        this.lineaCarritoService = lineaCarritoService;
    }

    public Mono<Product> execute(int productId, int promotionId) {
        return getProductRepository.findById(productId)
                .flatMap(product ->
                        productPromotionRepository.existsRelation(productId, promotionId)
                                .flatMap(count -> {
                                    if (count > 0) {
                                        return promotionLoader.loadPromotions(product);
                                    }
                                    return productPromotionRepository.insertRelation(productId, promotionId)
                                            .then(promotionLoader.loadPromotions(product))
                                            .flatMap(p -> {
                                                priceCalculator.recalculateFinalPrice(p);
                                                return updateProductRepository.save(p)
                                                        .then(lineaCarritoService.updateLineasCarrito(p))
                                                        .thenReturn(p);
                                            });
                                })
                );
    }
}