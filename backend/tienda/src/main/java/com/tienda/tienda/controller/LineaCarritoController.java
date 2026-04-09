package com.tienda.tienda.controller;

import com.tienda.tienda.dto.LineaCarritoDTO;
import com.tienda.tienda.service.LineaCarritoService;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<LineaCarritoDTO>> getAllLineas() {
        return ResponseEntity.ok(lineaCarritoService.getAllLineas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineaCarritoDTO> getLineaById(@PathVariable int id) {
        LineaCarritoDTO dto = lineaCarritoService.getLineaById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineaCarritoDTO> updateLinea(@PathVariable int id, @RequestParam int cantidad) {
        LineaCarritoDTO actualizado = lineaCarritoService.updateLinea(id, cantidad);
        if (actualizado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLinea(@PathVariable int id) {
        boolean eliminado = lineaCarritoService.deleteLinea(id);
        if (!eliminado) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
    
    
}

