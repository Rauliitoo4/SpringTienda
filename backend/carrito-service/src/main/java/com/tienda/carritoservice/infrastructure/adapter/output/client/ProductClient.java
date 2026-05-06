package com.tienda.carritoservice.infrastructure.adapter.output.client;

import com.tienda.carritoservice.application.model.ProductModel;
import com.tienda.carritoservice.application.port.output.GetProductOutputPort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "product-service", url = "${services.product-service.url}")
public interface ProductClient extends GetProductOutputPort {

    @Override
    @GetMapping("/productos/{id}")
    Mono<ProductModel> findById(@PathVariable int id);
}