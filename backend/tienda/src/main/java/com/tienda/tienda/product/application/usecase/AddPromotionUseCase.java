package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.application.port.input.AddPromotionInputPort;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.application.port.output.GetProductOutputPort;
import com.tienda.tienda.product.application.port.output.UpdateProductOutputPort;
import com.tienda.tienda.product.application.port.output.ProductPromotionOutputPort;
import com.tienda.tienda.product.application.service.PriceCalculator;
import com.tienda.tienda.product.application.service.PromotionLoader;
import com.tienda.tienda.carrito.application.service.LineasCarritoUpdater;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddPromotionUseCase implements AddPromotionInputPort {

    private final GetProductOutputPort getProductOutputPort;
    private final UpdateProductOutputPort updateProductOutputPort;
    private final ProductPromotionOutputPort productPromotionOutputPort;
    private final PromotionLoader promotionLoader;
    private final LineasCarritoUpdater lineasCarritoUpdater;
    private final PriceCalculator priceCalculator = new PriceCalculator();

    public AddPromotionUseCase(GetProductOutputPort getProductOutputPort, UpdateProductOutputPort updateProductOutputPort, ProductPromotionOutputPort productPromotionOutputPort, PromotionLoader promotionLoader, LineasCarritoUpdater lineasCarritoUpdater) {
        this.getProductOutputPort = getProductOutputPort;
        this.updateProductOutputPort = updateProductOutputPort;
        this.productPromotionOutputPort = productPromotionOutputPort;
        this.promotionLoader = promotionLoader;
        this.lineasCarritoUpdater = lineasCarritoUpdater;
    }

    public Mono<Product> execute(int productId, int promotionId) {
        return getProductOutputPort.findById(productId)
                .flatMap(product ->
                        productPromotionOutputPort.existsRelation(productId, promotionId)
                                .flatMap(count -> {
                                    if (count > 0) {
                                        return promotionLoader.loadPromotions(product);
                                    }
                                    return productPromotionOutputPort.insertRelation(productId, promotionId)
                                            .then(promotionLoader.loadPromotions(product))
                                            .flatMap(p -> {
                                                priceCalculator.recalculateFinalPrice(p);
                                                return updateProductOutputPort.save(p)
                                                        .then(lineasCarritoUpdater.updateLineas(p))
                                                        .thenReturn(p);
                                            });
                                })
                );
    }
}