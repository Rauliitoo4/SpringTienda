package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.repository.GetCarritoRepository;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper.CarritoPersistenceMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.CarritoJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class GetCarritoPersistenceAdapter implements GetCarritoRepository {

    private final CarritoJpaRepository jpaRepository;
    private final CarritoPersistenceMapper mapper;

    public GetCarritoPersistenceAdapter(CarritoJpaRepository jpaRepository, CarritoPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Carrito> findById(int id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
}