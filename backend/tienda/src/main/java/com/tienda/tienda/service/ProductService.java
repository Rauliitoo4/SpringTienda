package com.tienda.tienda.service;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.model.Product;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class ProductService {

    private List<Product> productos = new ArrayList<>();
    
    public ProductService() {
        productos.add(new Product(1, "Camiseta", 19.99));
        productos.add(new Product(2, "Pantalón", 39.99));
    }

    public List<ProductDTO> getAllProducts() {
        return productos.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
    }

    public ProductDTO getProductById(int id){
        return productos.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst()
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    public void addProduct (ProductDTO dto) {
        Product producto = convertToEntity(dto);
        productos.add(producto);
    }

    private ProductDTO convertToDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setDescripcion(p.getDescripcion());
        dto.setMaterial(p.getMaterial());
        dto.setConsideraciones(p.getConsideraciones());
        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product p = new Product();
        p.setId(dto.getId());
        p.setNombre(dto.getNombre());
        p.setPrecio(dto.getPrecio());
        p.setDescripcion(dto.getDescripcion());
        p.setMaterial(dto.getMaterial());
        p.setConsideraciones(dto.getConsideraciones());
        return p;
    }
}
