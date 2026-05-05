package com.tienda.productservice.application.usecase;

import com.tienda.productservice.application.port.input.UpdateProductInputPort;
import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.application.port.output.GetProductOutputPort;
import com.tienda.productservice.application.port.output.UpdateProductOutputPort;
import com.tienda.productservice.application.service.PromotionLoader;
import com.tienda.productservice.application.service.PriceCalculator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProductUseCase implements UpdateProductInputPort {

    private final UpdateProductOutputPort updateProductOutputPort;
    private final GetProductOutputPort getProductOutputPort;
    private final PromotionLoader promotionLoader;
    private final PriceCalculator priceCalculator;

    public UpdateProductUseCase (UpdateProductOutputPort updateProductOutputPort, GetProductOutputPort getProductOutputPort, PromotionLoader promotionLoader, PriceCalculator priceCalculator) {
        this.updateProductOutputPort = updateProductOutputPort;
        this.promotionLoader = promotionLoader;
        this.priceCalculator = priceCalculator;
        this.getProductOutputPort = getProductOutputPort;
    }

    public Mono<Product> execute(int id, Product updatedProduct) {
        return getProductOutputPort.findById(id)
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
                    return updateProductOutputPort.save(product);
                });
    }
}
