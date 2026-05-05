package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.application.port.output.GetCarritoOutputPort;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper.CarritoPersistenceMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.CarritoR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class GetCarritoPersistenceAdapter implements GetCarritoOutputPort {

    private final CarritoR2dbcRepository r2dbcRepository;
    private final CarritoPersistenceMapper mapper;

    public GetCarritoPersistenceAdapter(CarritoR2dbcRepository r2dbcRepository, CarritoPersistenceMapper mapper) {
        this.r2dbcRepository = r2dbcRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Carrito> findById(int id) {
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }
}