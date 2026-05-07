package com.tienda.carritoservice.application.service;

import com.tienda.carritoservice.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.carritoservice.domain.model.Carrito;
import com.tienda.carritoservice.domain.model.LineaCarrito;
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
                .flatMap(linea -> productLoader.loadProduct(linea.getProductId())
                        .doOnNext(linea::setProduct)
                        .thenReturn(linea))
                .collectList()
                .doOnNext(carrito::setLineas)
                .thenReturn(carrito);
    }
}