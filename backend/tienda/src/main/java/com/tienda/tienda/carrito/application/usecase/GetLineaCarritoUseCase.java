package com.tienda.tienda.carrito.application.usecase;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.domain.repository.GetLineaCarritoRepository;
import com.tienda.tienda.carrito.application.helper.ProductLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetLineaCarritoUseCase {

    private final GetLineaCarritoRepository getLineaCarritoRepository;
    private final ProductLoader productLoader;

    public GetLineaCarritoUseCase(GetLineaCarritoRepository getLineaCarritoRepository,
                                  ProductLoader productLoader) {
        this.getLineaCarritoRepository = getLineaCarritoRepository;
        this.productLoader = productLoader;
    }

    public Mono<LineaCarrito> execute(int id) {
        return getLineaCarritoRepository.findById(id)
                .flatMap(productLoader::loadProduct);
    }

    public Flux<LineaCarrito> executeAll() {
        return getLineaCarritoRepository.findAll()
                .flatMap(productLoader::loadProduct);
    }
}