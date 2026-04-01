package com.tienda.tienda.service;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.model.Carrito;
import com.tienda.tienda.model.LineaCarrito;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.Promotion;
import com.tienda.tienda.repository.*;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final PromotionRepository promotionRepo;
    private final CarritoRepository carritoRepo;
    private final LineaCarritoRepository lineaRepo;
    
    
    public ProductService(ProductRepository productRepo, PromotionRepository promotionRepo, CarritoRepository carritoRepo, LineaCarritoRepository lineaRepo) {
        this.productRepo = productRepo;
        this.promotionRepo = promotionRepo;
        this.carritoRepo = carritoRepo;
        this.lineaRepo = lineaRepo; 
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
        if (dto.getPrecio() >= 0) {
            producto.setPrecio(dto.getPrecio());
            recalcularPrecioFinal(producto);
        }
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

    public ProductDTO addPromotion(int productoID, int promocionID) {
        Product producto = productRepo.findById(productoID).orElse(null);
        if (producto == null) return null;

        Promotion promo = promotionRepo.findById(promocionID).orElse(null);
        if (promo == null) return null;

        if (!producto.getPromociones().contains(promo)) {
            producto.getPromociones().add(promo);
        }

        recalcularPrecioFinal(producto);
        actualizarLineasCarrito(producto);
        productRepo.save(producto);
        return convertToDTO(producto);
    }

    public ProductDTO removePromotion(int productoID, int promocionID) {
        Product producto = productRepo.findById(productoID).orElse(null);
        if (producto == null) return null;

        producto.getPromociones().removeIf(p -> p.getId() == promocionID);

        recalcularPrecioFinal(producto);
        actualizarLineasCarrito(producto);
        productRepo.save(producto);
        return convertToDTO(producto);
    }

    private ProductDTO convertToDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setPrecio(p.getPrecio());
        dto.setPrecioFinal(p.getPrecioFinal());
        dto.setDescripcion(p.getDescripcion());
        dto.setMaterial(p.getMaterial());
        dto.setConsideraciones(p.getConsideraciones());
        dto.setImagenUrl(p.getImagenUrl());

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
        p.setPrecioFinal(dto.getPrecio());
        p.setDescripcion(dto.getDescripcion());
        p.setMaterial(dto.getMaterial());
        p.setConsideraciones(dto.getConsideraciones());
        p.setImagenUrl(dto.getImagenUrl());
        return p;
    }

    private void recalcularPrecioFinal(Product producto) {
        if (producto.getPromociones() == null || producto.getPromociones().isEmpty()) {
            producto.setPrecioFinal(producto.getPrecio());
        }

        double maxDescuento = producto.getPromociones().stream()
                                .mapToDouble(Promotion::getDescuento)
                                .max()
                                .orElse(0);

        producto.setPrecioFinal(producto.getPrecio() * (1 - maxDescuento / 100));
    }

    private void actualizarLineasCarrito(Product producto) {
        List<LineaCarrito> lineas = lineaRepo.findAll();
        for (LineaCarrito linea : lineas) {
            if (linea.getProducto().getId() == producto.getId()) {
                linea.setSubtotal(producto.getPrecioFinal() * linea.getCantidad());
                lineaRepo.save(linea);

                Carrito carrito = linea.getCarrito();
                double total = carrito.getLineas().stream()
                                .mapToDouble(LineaCarrito::getSubtotal)
                                .sum();
                carrito.setTotal(total);
                carritoRepo.save(carrito);
            }
        }
    }
}
