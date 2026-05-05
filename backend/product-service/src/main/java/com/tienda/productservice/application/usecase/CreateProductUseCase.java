package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.application.port.input.CreateProductInputPort;
import com.tienda.tienda.product.domain.model.Product;
import com.tienda.tienda.product.application.port.output.CreateProductOutputPort;
import com.tienda.tienda.product.application.service.PromotionLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateProductUseCase implements CreateProductInputPort {

    private final CreateProductOutputPort createProductOutputPort;
    private final PromotionLoader promotionLoader;

    public CreateProductUseCase (CreateProductOutputPort createProductOutputPort, PromotionLoader promotionLoader) {
        this.createProductOutputPort = createProductOutputPort;
        this.promotionLoader = promotionLoader;
    }

    public Mono<Product> execute(Product product) {
        return createProductOutputPort.save(product)
                .flatMap(promotionLoader::loadPromotions);
    }
}
