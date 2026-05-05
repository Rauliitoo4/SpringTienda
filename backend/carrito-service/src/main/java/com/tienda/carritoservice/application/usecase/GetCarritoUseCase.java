package com.tienda.tienda.carrito.application.usecase;

import com.tienda.tienda.carrito.application.port.input.GetCarritoInputPort;
import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.application.port.output.GetCarritoOutputPort;
import com.tienda.tienda.carrito.application.service.LineaLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetCarritoUseCase implements GetCarritoInputPort {

    private final GetCarritoOutputPort getCarritoOutputPort;
    private final LineaLoader lineaLoader;

    public GetCarritoUseCase(GetCarritoOutputPort getCarritoOutputPort, LineaLoader lineaLoader) {
        this.getCarritoOutputPort = getCarritoOutputPort;
        this.lineaLoader = lineaLoader;
    }

    public Mono<Carrito> execute(int id) {
        return getCarritoOutputPort.findById(id)
                .flatMap(lineaLoader::loadLineas);
    }
}