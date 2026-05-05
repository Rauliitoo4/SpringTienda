package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.application.port.output.CreateCarritoOutputPort;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper.CarritoPersistenceMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.CarritoR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CreateCarritoPersistenceAdapter implements CreateCarritoOutputPort {

    private final CarritoR2dbcRepository r2dbcRepository;
    private final CarritoPersistenceMapper mapper;

    public CreateCarritoPersistenceAdapter(CarritoR2dbcRepository r2dbcRepository, CarritoPersistenceMapper mapper) {
        this.r2dbcRepository = r2dbcRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Carrito> save(Carrito carrito) {
        return r2dbcRepository.save(mapper.toEntity(carrito))
                .map(mapper::toDomain);
    }
}