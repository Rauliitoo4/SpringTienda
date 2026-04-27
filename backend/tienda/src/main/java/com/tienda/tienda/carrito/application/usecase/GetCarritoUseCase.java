package com.tienda.tienda.carrito.application.usecase;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.repository.GetCarritoRepository;
import com.tienda.tienda.carrito.application.helper.LineaLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GetCarritoUseCase {

    private final GetCarritoRepository getCarritoRepository;
    private final LineaLoader lineaLoader;

    public GetCarritoUseCase(GetCarritoRepository getCarritoRepository,
                             LineaLoader lineaLoader) {
        this.getCarritoRepository = getCarritoRepository;
        this.lineaLoader = lineaLoader;
    }

    public Mono<Carrito> execute(int id) {
        return getCarritoRepository.findById(id)
                .flatMap(lineaLoader::loadLineas);
    }
}