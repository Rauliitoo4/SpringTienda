package com.tienda.tienda.lineacarrito.controller;

import com.tienda.tienda.lineacarrito.dto.LineaCarritoDTO;
import com.tienda.tienda.lineacarrito.service.LineaCarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/lineas")
public class LineaCarritoController {
    
    private final LineaCarritoService lineaCarritoService;

    public LineaCarritoController(LineaCarritoService lineaCarritoService){
        this.lineaCarritoService = lineaCarritoService;
    }

    @GetMapping
    public ResponseEntity<Flux<LineaCarritoDTO>> getAllLineas() {
        return ResponseEntity.ok(lineaCarritoService.getAllLineas());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<LineaCarritoDTO>> getLineaById(@PathVariable int id) {
        return lineaCarritoService.getLineaById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<LineaCarritoDTO>> updateLinea(@PathVariable int id, @RequestParam int cantidad) {
        return lineaCarritoService.updateLinea(id, cantidad)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteLinea(@PathVariable int id) {
        return lineaCarritoService.deleteLinea(id)
                .map(d -> d
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }
    
    
}

