package com.tienda.tienda.carrito.infrastructure.adapter.input.rest;

import com.tienda.tienda.carrito.application.usecase.UpdateLineaCarritoUseCase;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.LineaCarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/lineas")
public class UpdateLineaCarritoRestAdapter {

    private final UpdateLineaCarritoUseCase updateLineaCarritoUseCase;
    private final LineaCarritoRestMapper mapper;

    public UpdateLineaCarritoRestAdapter(UpdateLineaCarritoUseCase updateLineaCarritoUseCase,
                                         LineaCarritoRestMapper mapper) {
        this.updateLineaCarritoUseCase = updateLineaCarritoUseCase;
        this.mapper = mapper;
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<LineaCarritoResponse>> updateLinea(@PathVariable int id, @RequestParam int quantity) {
        return updateLineaCarritoUseCase.execute(id, quantity)
                .map(linea -> ResponseEntity.ok(mapper.toResponse(linea)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}