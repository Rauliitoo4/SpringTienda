package com.tienda.tienda.carrito.infrastructure.adapter.input.rest;

import com.tienda.tienda.carrito.application.usecase.UpdateLineaCarritoUseCase;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.LineaCarritoRestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.mapper.UpdateLineaCarritoRequestMapper;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.request.UpdateLineaCarritoRequest;
import com.tienda.tienda.carrito.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/lineas")
public class UpdateLineaCarritoRestAdapter {

    private final UpdateLineaCarritoUseCase updateLineaCarritoUseCase;
    private final LineaCarritoRestMapper lineaCarritoRestMapper;
    private final UpdateLineaCarritoRequestMapper requestMapper;

    public UpdateLineaCarritoRestAdapter(UpdateLineaCarritoUseCase updateLineaCarritoUseCase, LineaCarritoRestMapper lineaCarritoRestMapper, UpdateLineaCarritoRequestMapper requestMapper) {
        this.updateLineaCarritoUseCase = updateLineaCarritoUseCase;
        this.lineaCarritoRestMapper = lineaCarritoRestMapper;
        this.requestMapper = requestMapper;
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<LineaCarritoResponse>> updateLinea(@PathVariable int id, @RequestBody UpdateLineaCarritoRequest request) {
        return updateLineaCarritoUseCase.execute(id, requestMapper.toQuantity(request))
                .map(linea -> ResponseEntity.ok(lineaCarritoRestMapper.toResponse(linea)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}