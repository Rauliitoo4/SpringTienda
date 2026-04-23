package com.tienda.tienda.carrito.application.service.helper;

import com.tienda.tienda.carrito.domain.Carrito;
import com.tienda.tienda.lineacarrito.application.port.LineaCarritoRepositoryPort;
import com.tienda.tienda.lineacarrito.application.service.helper.ProductLoader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LineaLoader {

    private final LineaCarritoRepositoryPort lineaCarritoRepo;
    private final ProductLoader productLoader;

    public LineaLoader(LineaCarritoRepositoryPort lineaCarritoRepo, ProductLoader productLoader) {
        this.lineaCarritoRepo = lineaCarritoRepo;
        this.productLoader = productLoader;
    }

    public Mono<Carrito> loadLineas(Carrito carrito) {
        return lineaCarritoRepo.findByCarritoId(carrito.getId())
                .flatMap(productLoader::loadProduct)
                .collectList()
                .doOnNext(carrito::setLineas)
                .thenReturn(carrito);
    }
}
