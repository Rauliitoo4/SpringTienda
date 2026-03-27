package com.tienda.tienda.service;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.Promotion;

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

    public ProductDTO createProduct(ProductDTO dto){
        Product product = new Product();
        product.setId(dto.getId());
        product.setNombre(dto.getNombre());
        product.setPrecio(dto.getPrecio());
        product.setDescripcion(dto.getDescripcion());
        product.setMaterial(dto.getMaterial());
        product.setConsideraciones(dto.getConsideraciones());

        List<Promotion> promos = new ArrayList<>();
        if (dto.getPromociones() != null) {
            for (PromotionDTO p : dto.getPromociones()) {
                Promotion promo = new Promotion();
                promo.setId(p.getId());
                promo.setDescuento(p.getDescuento());
                promo.setDescripcion(p.getDescripcion());
                promos.add(promo);
            }
        }
        product.setPromociones(promos);

        productos.add(product);
        return convertToDTO(product);
    }

    public ProductDTO updateProduct(int id, ProductDTO dto){
        for (Product product : productos) {
            if (product.getId() == id) {
                if (dto.getNombre() != null) product.setNombre(dto.getNombre());
                if (dto.getPrecio() != null) product.setPrecio(dto.getPrecio());
                if (dto.getDescripcion() != null) product.setDescripcion(dto.getDescripcion());
                if (dto.getMaterial() != null) product.setMaterial(dto.getMaterial());
                if (dto.getConsideraciones() != null) product.setConsideraciones(dto.getConsideraciones());

                if (dto.getPromociones() != null) {
                    List<Promotion> promos = new ArrayList<>();
                    for (PromotionDTO p : dto.getPromociones()) {
                        Promotion promo = new Promotion();
                        promo.setId(p.getId());
                        promo.setDescuento(p.getDescuento());
                        promo.setDescripcion(p.getDescripcion());
                        promos.add(promo);
                    }
                    product.setPromociones(promos);
                }

                return convertToDTO(product);
            }
        }
        return null;
    }

    public boolean deleteProduct (int id) {
        return productos.removeIf(p -> p.getId() == id);
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
