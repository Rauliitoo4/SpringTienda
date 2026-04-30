package com.tienda.tienda.carrito.application.service;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.application.port.output.GetLineaCarritoOutputPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LineaLoader {

    private final GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    private final ProductLoader productLoader;

    public LineaLoader(GetLineaCarritoOutputPort getLineaCarritoOutputPort, ProductLoader productLoader) {
        this.getLineaCarritoOutputPort = getLineaCarritoOutputPort;
        this.productLoader = productLoader;
    }

    public Mono<Carrito> loadLineas(Carrito carrito) {
        return getLineaCarritoOutputPort.findByCarritoId(carrito.getId())
                .flatMap(productLoader::loadProduct)
                .collectList()
                .doOnNext(carrito::setLineas)
                .thenReturn(carrito);
    }
}
