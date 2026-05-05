package com.tienda.carritoservice.application.service;

import com.tienda.carritoservice.application.service.ProductLoader;
import com.tienda.carritoservice.domain.model.Carrito;
import com.tienda.carritoservice.application.port.output.GetLineaCarritoOutputPort;
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
