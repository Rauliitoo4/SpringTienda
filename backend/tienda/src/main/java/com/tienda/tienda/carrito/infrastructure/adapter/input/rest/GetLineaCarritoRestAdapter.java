package com.tienda.tienda.carrito.infrastructure.adapter.input.rest;

import com.tienda.tienda.carrito.application.usecase.GetLineaCarritoUseCase;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.LineaCarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/lineas")
public class GetLineaCarritoRestAdapter {

    private final GetLineaCarritoUseCase getLineaCarritoUseCase;
    private final LineaCarritoRestMapper mapper;

    public GetLineaCarritoRestAdapter(GetLineaCarritoUseCase getLineaCarritoUseCase, LineaCarritoRestMapper mapper) {
        this.getLineaCarritoUseCase = getLineaCarritoUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<LineaCarritoResponse>> getLineaById(@PathVariable int id) {
        return getLineaCarritoUseCase.execute(id)
                .map(linea -> ResponseEntity.ok(mapper.toResponse(linea)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Flux<LineaCarritoResponse>> getAllLineas() {
        return ResponseEntity.ok(getLineaCarritoUseCase.executeAll()
                .map(mapper::toResponse));
    }
}