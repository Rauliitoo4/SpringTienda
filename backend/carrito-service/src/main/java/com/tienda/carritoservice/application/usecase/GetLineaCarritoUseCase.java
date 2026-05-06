package com.tienda.carritoservice.application.usecase;

import com.tienda.carritoservice.application.port.input.GetLineaCarritoInputPort;
import com.tienda.carritoservice.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetLineaCarritoUseCase implements GetLineaCarritoInputPort {

    private final GetLineaCarritoOutputPort getLineaCarritoOutputPort;

    public GetLineaCarritoUseCase(GetLineaCarritoOutputPort getLineaCarritoOutputPort) {
        this.getLineaCarritoOutputPort = getLineaCarritoOutputPort;
    }

    public Mono<LineaCarrito> execute(int id) {
        return getLineaCarritoOutputPort.findById(id);
    }

    public Flux<LineaCarrito> executeAll() {
        return getLineaCarritoOutputPort.findAll();
    }
}