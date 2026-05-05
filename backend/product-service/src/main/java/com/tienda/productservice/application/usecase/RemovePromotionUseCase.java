package com.tienda.productservice.application.usecase;

import com.tienda.productservice.application.port.input.RemovePromotionInputPort;
import com.tienda.productservice.application.port.output.GetProductOutputPort;
import com.tienda.productservice.application.port.output.ProductPromotionOutputPort;
import com.tienda.productservice.application.port.output.UpdateProductOutputPort;
import com.tienda.productservice.application.service.PriceCalculator;
import com.tienda.productservice.application.service.PromotionLoader;
import com.tienda.productservice.domain.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RemovePromotionUseCase implements RemovePromotionInputPort {

    private final GetProductOutputPort getProductOutputPort;
    private final UpdateProductOutputPort updateProductOutputPort;
    private final ProductPromotionOutputPort productPromotionOutputPort;
    private final PromotionLoader promotionLoader;
    private final PriceCalculator priceCalculator = new PriceCalculator();

    public RemovePromotionUseCase(GetProductOutputPort getProductOutputPort,
                                  UpdateProductOutputPort updateProductOutputPort,
                                  ProductPromotionOutputPort productPromotionOutputPort,
                                  PromotionLoader promotionLoader) {
        this.getProductOutputPort = getProductOutputPort;
        this.updateProductOutputPort = updateProductOutputPort;
        this.productPromotionOutputPort = productPromotionOutputPort;
        this.promotionLoader = promotionLoader;
    }

    public Mono<Product> execute(int productId, int promotionId) {
        return getProductOutputPort.findById(productId)
                .flatMap(product ->
                        productPromotionOutputPort.deleteByProductIdAndPromotionId(productId, promotionId)
                                .then(promotionLoader.loadPromotions(product))
                                .flatMap(p -> {
                                    priceCalculator.recalculateFinalPrice(p);
                                    return updateProductOutputPort.save(p)
                                            .thenReturn(p);
                                })
                );
    }
}