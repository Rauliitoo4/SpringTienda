package com.tienda.tienda.controller;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/productos")
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        ProductDTO dto = productService.getProductById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);     
    }   
    
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO dto) {
        ProductDTO creado = productService.createProduct(dto);
        if (creado == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct (@PathVariable int id, @RequestBody ProductDTO dto) {
       ProductDTO actualizado = productService.updateProduct(id, dto);
       if (actualizado == null) return ResponseEntity.notFound().build();
       return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        boolean eliminado = productService.deleteProduct(id);
        if (!eliminado) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productoID}/promociones/{promocionID}")
    public ResponseEntity<ProductDTO> addPromotion(@PathVariable int productoID, @PathVariable int promocionID) {
        ProductDTO dto = productService.addPromotion(productoID, promocionID);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }
    
    @DeleteMapping("/{productoID}/promociones/{promocionID}")
    public ResponseEntity<ProductDTO> removePromotion(@PathVariable int productoID, @PathVariable int promocionID) {
        ProductDTO dto = productService.removePromotion(productoID, promocionID);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }
    
}
