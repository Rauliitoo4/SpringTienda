package com.tienda.tienda.service;

import com.tienda.tienda.model.Product;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Arrays;

@Service
public class ProductService {
    
    public List<Product> getAllProducts() {
        return Arrays.asList(
            new Product(1, "Camiseta", 19.99),
            new Product(2, "Pantalón", 39.99)
        );
    }
}
