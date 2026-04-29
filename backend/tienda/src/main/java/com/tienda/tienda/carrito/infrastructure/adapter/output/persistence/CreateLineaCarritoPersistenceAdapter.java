package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.domain.repository.CreateLineaCarritoRepository;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper.LineaCarritoPersistenceMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.LineaCarritoR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CreateLineaCarritoPersistenceAdapter implements CreateLineaCarritoRepository {

    private final LineaCarritoR2dbcRepository jpaRepository;
    private final LineaCarritoPersistenceMapper mapper;

    public CreateLineaCarritoPersistenceAdapter(LineaCarritoR2dbcRepository jpaRepository, LineaCarritoPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<LineaCarrito> save(LineaCarrito lineaCarrito) {
        return jpaRepository.save(mapper.toEntity(lineaCarrito))
                .map(mapper::toDomain);
    }
}