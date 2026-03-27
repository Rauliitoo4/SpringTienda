package com.tienda.tienda.controller;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/products")
public class ProductController {
    
    private final ProductService productService;

    //Constructor donde inyectamos el servicio
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    //Obtener todos los productos
    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    //Obtener producto por id
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable int id) {
        return productService.getProductById(id);
    }   
    
    
    
}
