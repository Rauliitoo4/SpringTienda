package com.tienda.tienda.carrito.application.usecase;

import com.tienda.tienda.carrito.application.port.input.GetLineaCarritoInputPort;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.tienda.carrito.application.service.ProductLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetLineaCarritoUseCase implements GetLineaCarritoInputPort {

    private final GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    private final ProductLoader productLoader;

    public GetLineaCarritoUseCase(GetLineaCarritoOutputPort getLineaCarritoOutputPort, ProductLoader productLoader) {
        this.getLineaCarritoOutputPort = getLineaCarritoOutputPort;
        this.productLoader = productLoader;
    }

    public Mono<LineaCarrito> execute(int id) {
        return getLineaCarritoOutputPort.findById(id)
                .flatMap(productLoader::loadProduct);
    }

    public Flux<LineaCarrito> executeAll() {
        return getLineaCarritoOutputPort.findAll()
                .flatMap(productLoader::loadProduct);
    }
}