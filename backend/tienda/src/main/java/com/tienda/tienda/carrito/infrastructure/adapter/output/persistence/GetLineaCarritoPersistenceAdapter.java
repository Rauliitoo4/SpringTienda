package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.domain.repository.GetLineaCarritoRepository;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper.LineaCarritoPersistenceMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.LineaCarritoJpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GetLineaCarritoPersistenceAdapter implements GetLineaCarritoRepository {

    private final LineaCarritoJpaRepository jpaRepository;
    private final LineaCarritoPersistenceMapper mapper;

    public GetLineaCarritoPersistenceAdapter(LineaCarritoJpaRepository jpaRepository,
                                             LineaCarritoPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<LineaCarrito> findById(int id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<LineaCarrito> findAll() {
        return jpaRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Flux<LineaCarrito> findByCarritoId(int carritoId) {
        return jpaRepository.findByCarritoId(carritoId)
                .map(mapper::toDomain);
    }
}