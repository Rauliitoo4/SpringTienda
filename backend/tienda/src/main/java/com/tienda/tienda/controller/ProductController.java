package com.tienda.tienda.controller;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.service.ProductService;
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
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable int id) {
        return productService.getProductById(id);     
    }   
    
    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO dto) {
        return productService.createProduct(dto);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct (@PathVariable int id, @RequestBody ProductDTO dto) {
       return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }

    @PostMapping("/{productoID}/promociones/{promocionID}")
    public ProductDTO addPromotion(@PathVariable int productoID, @PathVariable int promocionID) {
        return productService.addPromotion(productoID, promocionID);
    }
    
    @DeleteMapping("/{productoID}/promociones/{promocionID}")
    public ProductDTO removePromotion(@PathVariable int productoID, @PathVariable int promocionID) {
        return productService.removePromotion(productoID, promocionID);
    }
    
}
