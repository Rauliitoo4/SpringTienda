package com.tienda.carritoservice.infrastructure.adapter.input.rest;

import com.tienda.carritoservice.application.port.input.GetLineaCarritoInputPort;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.LineaCarritoRestMapper;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/lineas")
public class GetLineaCarritoRestAdapter {

    private final GetLineaCarritoInputPort getLineaCarritoInputPort;
    private final LineaCarritoRestMapper mapper;

    public GetLineaCarritoRestAdapter(GetLineaCarritoInputPort getLineaCarritoInputPort, LineaCarritoRestMapper mapper) {
        this.getLineaCarritoInputPort = getLineaCarritoInputPort;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<LineaCarritoResponse>> getLineaById(@PathVariable int id) {
        return getLineaCarritoInputPort.execute(id)
                .map(linea -> ResponseEntity.ok(mapper.toResponse(linea)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Flux<LineaCarritoResponse>> getAllLineas() {
        return ResponseEntity.ok(getLineaCarritoInputPort.executeAll()
                .map(mapper::toResponse));
    }
}