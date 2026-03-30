package com.tienda.tienda.service;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.Promotion;
import com.tienda.tienda.repository.ProductRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    
    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public ProductDTO createProduct(ProductDTO dto){
        Product product = convertToEntity(dto);
        productRepo.save(product);
        return convertToDTO(product);
    }

    public ProductDTO updateProduct(int id, ProductDTO dto){
        Product producto = productRepo.findById(id).orElse(null);
        if (producto == null) return null;

        if (dto.getNombre() != null) producto.setNombre(dto.getNombre());
        if (dto.getPrecio() >= 0) producto.setPrecio(dto.getPrecio());
        if (dto.getDescripcion() != null) producto.setDescripcion(dto.getDescripcion());
        if (dto.getMaterial() != null) producto.setMaterial(dto.getMaterial());
        if (dto.getConsideraciones() != null) producto.setConsideraciones(dto.getConsideraciones());

        productRepo.save(producto);
        return convertToDTO(producto);
    }

    public boolean deleteProduct (int id) {
        if (!productRepo.existsById(id)) return false;
        productRepo.deleteById(id);
        return true;
    }

    public List<ProductDTO> getAllProducts() {
        List <ProductDTO> listDTO = new ArrayList<>();
        for (Product producto : productRepo.findAll()) {
            listDTO.add(convertToDTO(producto));
        }
        return listDTO;
    }

    public ProductDTO getProductById(int id){
        return productRepo.findById(id)
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    private ProductDTO convertToDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setDescripcion(p.getDescripcion());
        dto.setMaterial(p.getMaterial());
        dto.setConsideraciones(p.getConsideraciones());

        List<PromotionDTO> promosDTO = new ArrayList<>();
        if (p.getPromociones() != null) {
            for (Promotion promo : p.getPromociones()) {
                PromotionDTO pDTO = new PromotionDTO();
                pDTO.setId(promo.getId());
                pDTO.setDescripcion(promo.getDescripcion());
                pDTO.setDescuento(promo.getDescuento());
                promosDTO.add(pDTO);
            }
        }
        dto.setPromociones(promosDTO);

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
