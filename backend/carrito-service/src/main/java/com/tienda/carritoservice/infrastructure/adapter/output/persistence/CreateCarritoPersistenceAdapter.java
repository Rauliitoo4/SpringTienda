package com.tienda.carritoservice.infrastructure.adapter.output.persistence;

import com.tienda.carritoservice.domain.model.Carrito;
import com.tienda.carritoservice.application.port.output.CreateCarritoOutputPort;
import com.tienda.carritoservice.infrastructure.adapter.output.persistence.mapper.CarritoPersistenceMapper;
import com.tienda.carritoservice.infrastructure.adapter.output.persistence.repository.CarritoR2dbcRepository;
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