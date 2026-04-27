package com.tienda.tienda.carrito.application.usecase;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.repository.CreateCarritoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateCarritoUseCase {

    private final CreateCarritoRepository createCarritoRepository;

    public CreateCarritoUseCase(CreateCarritoRepository createCarritoRepository) {
        this.createCarritoRepository = createCarritoRepository;
    }

    public Mono<Carrito> execute() {
        Carrito carrito = new Carrito();
        carrito.setTotal(0.0);
        return createCarritoRepository.save(carrito);
    }
}