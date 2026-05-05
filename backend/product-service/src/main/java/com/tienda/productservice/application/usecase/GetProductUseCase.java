package com.tienda.productservice.application.usecase;

import com.tienda.productservice.application.port.input.GetProductInputPort;
import com.tienda.productservice.domain.model.Product;
import com.tienda.productservice.application.port.output.GetProductOutputPort;
import com.tienda.productservice.application.service.PromotionLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetProductUseCase implements GetProductInputPort {

    private final GetProductOutputPort getProductOutputPort;
    private final PromotionLoader promotionLoader;

    public GetProductUseCase (GetProductOutputPort getProductOutputPort, PromotionLoader promotionLoader) {
        this.getProductOutputPort = getProductOutputPort;
        this.promotionLoader = promotionLoader;
    }

    public Mono<Product> execute(int id) {
        return getProductOutputPort.findById(id)
                .flatMap(promotionLoader::loadPromotions);
    }

    public Flux<Product> executeAll() {
        return getProductOutputPort.findAll()
                .flatMap(promotionLoader::loadPromotions);
    }
}
