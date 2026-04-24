package com.tienda.tienda.product.infraestructure.input.rest;

import com.tienda.tienda.product.application.dto.ProductRequest;
import com.tienda.tienda.product.application.dto.ProductResponse;
import com.tienda.tienda.product.application.usecase.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/productos")
public class ProductController {
    
    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final AddPromotionUseCase addPromotionUseCase;
    private final RemovePromotionUseCase removePromotionUseCase;

    public ProductController(CreateProductUseCase createProductUseCase, UpdateProductUseCase updateProductUseCase, DeleteProductUseCase deleteProductUseCase, GetProductUseCase getProductUseCase, AddPromotionUseCase addPromotionUseCase, RemovePromotionUseCase removePromotionUseCase){
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.addPromotionUseCase = addPromotionUseCase;
        this.removePromotionUseCase = removePromotionUseCase;
    }

    @GetMapping
    public ResponseEntity<Flux<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(getProductUseCase.executeAll());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponse>> getProductById(@PathVariable int id) {
        return getProductUseCase.execute(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }   
    
    @PostMapping
    public Mono<ResponseEntity<ProductResponse>> createProduct(@RequestBody ProductRequest request) {
        return createProductUseCase.execute(request)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ProductResponse>> updateProduct (@PathVariable int id, @RequestBody ProductRequest request) {
        return updateProductUseCase.execute(id, request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable int id) {
        return deleteProductUseCase.execute(id)
                .map(d -> d
                        ? ResponseEntity.<Void>noContent().build()
                        : ResponseEntity.<Void>notFound().build());
    }

    @PostMapping("/{productoID}/promociones/{promocionID}")
    public Mono<ResponseEntity<ProductResponse>> addPromotion(@PathVariable int productoID, @PathVariable int promocionID) {
            return addPromotionUseCase.execute(productoID, promocionID)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{productoID}/promociones/{promocionID}")
    public Mono<ResponseEntity<ProductResponse>> removePromotion(@PathVariable int productoID, @PathVariable int promocionID) {
        return removePromotionUseCase.execute(productoID, promocionID)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
}
