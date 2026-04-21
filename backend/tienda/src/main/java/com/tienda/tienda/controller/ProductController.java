package com.tienda.tienda.controller;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/productos")
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Flux<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> getProductById(@PathVariable int id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }   
    
    @PostMapping
    public Mono<ResponseEntity<ProductDTO>> createProduct(@RequestBody ProductDTO dto) {
        return productService.createProduct(dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductDTO>> updateProduct (@PathVariable int id, @RequestBody ProductDTO dto) {
        return productService.updateProduct(id, dto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id)
                .map(d -> d
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }

    @PostMapping("/{productoID}/promociones/{promocionID}")
    public Mono<ResponseEntity<ProductDTO>> addPromotion(@PathVariable int productoID, @PathVariable int promocionID) {
            return productService.addPromotion(productoID, promocionID)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{productoID}/promociones/{promocionID}")
    public Mono<ResponseEntity<ProductDTO>> removePromotion(@PathVariable int productoID, @PathVariable int promocionID) {
        return productService.removePromotion(productoID, promocionID)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
}
