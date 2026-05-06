package com.tienda.userservice.infrastructure.adapter.output.client;

import com.tienda.userservice.application.model.CarritoModel;
import com.tienda.userservice.application.port.output.CreateCarritoOutputPort;
import org.springframework.web.bind.annotation.PostMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "carrito-service", url = "${services.carrito-service.url}")
public interface CarritoClient extends CreateCarritoOutputPort {

    @Override
    @PostMapping("/carritos")
    Mono<CarritoModel> create();
}