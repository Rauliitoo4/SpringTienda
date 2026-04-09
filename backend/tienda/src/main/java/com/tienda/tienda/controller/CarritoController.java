package com.tienda.tienda.controller;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.service.CarritoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
@RequestMapping("/carritos")
public class CarritoController {
    
    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService){
        this.carritoService = carritoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoDTO> getCarritoById(@PathVariable int id) {
        CarritoDTO dto = carritoService.getCarritoById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{carritoId}/productos/{productoId}")
    public ResponseEntity<CarritoDTO> addProductToCarrito(@PathVariable int carritoId, @PathVariable int productoId, @RequestParam int cantidad) {
        CarritoDTO dto = carritoService.addProductToCarrito(carritoId, productoId, cantidad);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    } 
}

