package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.mapper.LineaCarritoPersistenceMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.repository.LineaCarritoR2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class GetLineaCarritoPersistenceAdapter implements GetLineaCarritoOutputPort {

    private final LineaCarritoR2dbcRepository r2dbcRepository;
    private final LineaCarritoPersistenceMapper mapper;

    public GetLineaCarritoPersistenceAdapter(LineaCarritoR2dbcRepository r2dbcRepository, LineaCarritoPersistenceMapper mapper) {
        this.r2dbcRepository = r2dbcRepository;
        this.mapper = mapper;
    }

    @Override
    public Mono<LineaCarrito> findById(int id) {
        return r2dbcRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<LineaCarrito> findAll() {
        return r2dbcRepository.findAll()
                .map(mapper::toDomain);
    }

    @Override
    public Flux<LineaCarrito> findByCarritoId(int carritoId) {
        return r2dbcRepository.findByCarritoId(carritoId)
                .map(mapper::toDomain);
    }
}