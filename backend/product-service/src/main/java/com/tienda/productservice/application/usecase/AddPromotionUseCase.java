package com.tienda.productservice.application.usecase;

import com.tienda.productservice.application.port.input.AddPromotionInputPort;
import com.tienda.productservice.application.port.output.GetProductOutputPort;
import com.tienda.productservice.application.port.output.ProductPromotionOutputPort;
import com.tienda.productservice.application.port.output.UpdateProductOutputPort;
import com.tienda.productservice.application.service.PromotionLoader;
import com.tienda.productservice.domain.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddPromotionUseCase implements AddPromotionInputPort {

    private final GetProductOutputPort getProductOutputPort;
    private final UpdateProductOutputPort updateProductOutputPort;
    private final ProductPromotionOutputPort productPromotionOutputPort;
    private final PromotionLoader promotionLoader;

    public AddPromotionUseCase(GetProductOutputPort getProductOutputPort,
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
                        productPromotionOutputPort.existsRelation(productId, promotionId)
                                .flatMap(count -> {
                                    if (count > 0) {
                                        return promotionLoader.loadPromotions(product);
                                    }
                                    return productPromotionOutputPort.insertRelation(productId, promotionId)
                                            .then(promotionLoader.loadPromotions(product))
                                            .flatMap(p -> updateProductOutputPort.save(p).thenReturn(p));
                                })
                );
    }
}