package com.tienda.userservice.application.port.output;

import com.tienda.userservice.application.model.CarritoModel;
import reactor.core.publisher.Mono;

public interface CreateCarritoOutputPort {
    Mono<CarritoModel> create();
}