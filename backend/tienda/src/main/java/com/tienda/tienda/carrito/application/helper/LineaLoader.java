package com.tienda.tienda.carrito.application.helper;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.repository.GetLineaCarritoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LineaLoader {

    private final GetLineaCarritoRepository getLineaCarritoRepository;
    private final ProductLoader productLoader;

    public LineaLoader(GetLineaCarritoRepository getLineaCarritoRepository, ProductLoader productLoader) {
        this.getLineaCarritoRepository = getLineaCarritoRepository;
        this.productLoader = productLoader;
    }

    public Mono<Carrito> loadLineas(Carrito carrito) {
        return getLineaCarritoRepository.findByCarritoId(carrito.getId())
                .flatMap(productLoader::loadProduct)
                .collectList()
                .doOnNext(carrito::setLineas)
                .thenReturn(carrito);
    }
}
