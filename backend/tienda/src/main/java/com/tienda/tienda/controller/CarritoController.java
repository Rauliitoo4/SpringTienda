package com.tienda.tienda.controller;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.service.CarritoService;
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
    public CarritoDTO getCarritoById(@PathVariable int id) {
        return carritoService.getCarritoById(id);
    }

    @PostMapping("/{carritoId}/productos/{productoId}")
    public CarritoDTO addProductToCarrito(@PathVariable int carritoId, @PathVariable int productoId, @RequestParam int cantidad) {
        return carritoService.addProductToCarrito(carritoId, productoId, cantidad);
    } 
}

