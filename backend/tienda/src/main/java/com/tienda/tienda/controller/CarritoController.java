package com.tienda.tienda.controller;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.service.CarritoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/carritos")
public class CarritoController {
    
    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService){
        this.carritoService = carritoService;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CarritoDTO>> getCarritoById(@PathVariable int id) {
        return carritoService.getCarritoById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/{carritoId}/productos/{productoId}")
    public Mono<ResponseEntity<CarritoDTO>> addProductToCarrito(@PathVariable int carritoId, @PathVariable int productoId, @RequestParam int cantidad) {
        return carritoService.addProductToCarrito(carritoId, productoId, cantidad)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    } 
}

