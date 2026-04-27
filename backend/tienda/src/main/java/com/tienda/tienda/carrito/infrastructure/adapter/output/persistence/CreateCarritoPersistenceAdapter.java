package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.repository.CreateCarritoRepository;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper.CarritoPersistenceMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.CarritoJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CreateCarritoPersistenceAdapter implements CreateCarritoRepository {

    private final CarritoJpaRepository jpaRepository;
    private final CarritoPersistenceMapper mapper;

    public CreateCarritoPersistenceAdapter(CarritoJpaRepository jpaRepository, CarritoPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Carrito> save(Carrito carrito) {
        return jpaRepository.save(mapper.toEntity(carrito))
                .map(mapper::toDomain);
    }
}