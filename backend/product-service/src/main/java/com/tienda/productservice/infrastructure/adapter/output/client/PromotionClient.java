package com.tienda.productservice.infrastructure.client;

import com.tienda.productservice.application.model.PromotionModel;
import com.tienda.productservice.application.port.output.GetPromotionOutputPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@ReactiveFeignClient(name = "promotion-service", url = "${services.promotion-service.url}")
public interface PromotionClient extends GetPromotionOutputPort {

    @GetMapping("/promociones/{id}")
    Mono<PromotionModel> findById(@PathVariable int id);

    @Override
    default Flux<PromotionModel> findAllByIds(List<Integer> ids) {
        return Flux.fromIterable(ids).flatMap(this::findById);
    }
}