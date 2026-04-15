package com.tienda.tienda.service.helper;

import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.repository.LineaCarritoRepository;
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

    public Mono<Carrito> cargarLineas(Carrito carrito) {
        return lineaCarritoRepo.findByCarritoId(carrito.getId())
                .flatMap(productLoader::cargarProducto)
                .collectList()
                .doOnNext(carrito::setLineas)
                .thenReturn(carrito);
    }
}
