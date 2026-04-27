package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.domain.repository.UpdateLineaCarritoRepository;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper.LineaCarritoPersistenceMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.LineaCarritoJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UpdateLineaCarritoPersistenceAdapter implements UpdateLineaCarritoRepository {

    private final LineaCarritoJpaRepository jpaRepository;
    private final LineaCarritoPersistenceMapper mapper;

    public UpdateLineaCarritoPersistenceAdapter(LineaCarritoJpaRepository jpaRepository,
                                                LineaCarritoPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<LineaCarrito> save(LineaCarrito lineaCarrito) {
        return jpaRepository.save(mapper.toEntity(lineaCarrito))
                .map(mapper::toDomain);
    }
}