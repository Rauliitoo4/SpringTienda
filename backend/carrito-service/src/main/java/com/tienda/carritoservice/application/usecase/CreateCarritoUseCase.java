package com.tienda.carritoservice.application.usecase;

import com.tienda.tienda.carrito.application.port.input.CreateCarritoInputPort;
import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.application.port.output.CreateCarritoOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateCarritoUseCase implements CreateCarritoInputPort {

    private final CreateCarritoOutputPort createCarritoOutputPort;

    public CreateCarritoUseCase(CreateCarritoOutputPort createCarritoOutputPort) {
        this.createCarritoOutputPort = createCarritoOutputPort;
    }

    public Mono<Carrito> execute() {
        Carrito carrito = new Carrito();
        carrito.setTotal(0.0);
        return createCarritoOutputPort.save(carrito);
    }
}