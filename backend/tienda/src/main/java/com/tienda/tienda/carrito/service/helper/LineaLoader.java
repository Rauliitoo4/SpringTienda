package com.tienda.tienda.carrito.service.helper;

import com.tienda.tienda.carrito.model.Carrito;
import com.tienda.tienda.lineacarrito.repository.LineaCarritoRepository;
import com.tienda.tienda.lineacarrito.service.helper.ProductLoader;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LineaLoader {

    private final LineaCarritoRepository lineaCarritoRepo;
    private final ProductLoader productLoader;

    public LineaLoader(LineaCarritoRepository lineaCarritoRepo, ProductLoader productLoader) {
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
