package com.tienda.tienda.controller;

import com.tienda.tienda.dto.LineaCarritoDTO;
import com.tienda.tienda.service.LineaCarritoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;




@RestController
@RequestMapping("/lineas")
public class LineaCarritoController {
    
    private final LineaCarritoService lineaCarritoService;

    public LineaCarritoController(LineaCarritoService lineaCarritoService){
        this.lineaCarritoService = lineaCarritoService;
    }

    @GetMapping
    public List<LineaCarritoDTO> getAllLineas() {
        return lineaCarritoService.getAllLineas();
    }

    @GetMapping("/{id}")
    public LineaCarritoDTO getLineaById(@PathVariable int id) {
        return lineaCarritoService.getLineaById(id);
    }

    @PutMapping("/{id}")
    public LineaCarritoDTO updateLinea(@PathVariable int id, @RequestParam int cantidad) {
        return lineaCarritoService.updateLinea(id, cantidad);
    }
    
    @DeleteMapping("/{id}")
    public boolean deleteLinea(@PathVariable int id) {
        return lineaCarritoService.deleteLinea(id);
    }
    
    
}

