package com.tienda.carritoservice.infrastructure.adapter.input.rest;

import com.tienda.carritoservice.application.port.input.UpdateLineaCarritoInputPort;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.LineaCarritoRestMapper;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.mapper.UpdateLineaCarritoRequestMapper;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.request.UpdateLineaCarritoRequest;
import com.tienda.carritoservice.infrastructure.adapter.input.rest.data.response.LineaCarritoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/lineas")
public class UpdateLineaCarritoRestAdapter {

    private final UpdateLineaCarritoInputPort updateLineaCarritoInputPort;
    private final LineaCarritoRestMapper lineaCarritoRestMapper;
    private final UpdateLineaCarritoRequestMapper requestMapper;

    public UpdateLineaCarritoRestAdapter(UpdateLineaCarritoInputPort updateLineaCarritoInputPort, LineaCarritoRestMapper lineaCarritoRestMapper, UpdateLineaCarritoRequestMapper requestMapper) {
        this.updateLineaCarritoInputPort = updateLineaCarritoInputPort;
        this.lineaCarritoRestMapper = lineaCarritoRestMapper;
        this.requestMapper = requestMapper;
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<LineaCarritoResponse>> updateLinea(@PathVariable int id, @RequestBody UpdateLineaCarritoRequest request) {
        return updateLineaCarritoInputPort.execute(id, requestMapper.toQuantity(request))
                .map(linea -> ResponseEntity.ok(lineaCarritoRestMapper.toResponse(linea)))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}