package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.repository.UpdateCarritoRepository;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper.CarritoPersistenceMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.CarritoR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UpdateCarritoPersistenceAdapter implements UpdateCarritoRepository {

    private final CarritoR2dbcRepository jpaRepository;
    private final CarritoPersistenceMapper mapper;

    public UpdateCarritoPersistenceAdapter(CarritoR2dbcRepository jpaRepository, CarritoPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Carrito> save(Carrito carrito) {
        return jpaRepository.save(mapper.toEntity(carrito))
                .map(mapper::toDomain);
    }
}