package com.tienda.tienda.service;

import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import com.tienda.tienda.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {
    
    private final CarritoRepository carritoRepo;
    private final ProductRepository productRepo;

    public CarritoService(ProductRepository productRepo, CarritoRepository carritoRepo) {
        this.productRepo = productRepo;
        this.carritoRepo = carritoRepo;
    }

    public CarritoDTO getCarritoById (int id){
        return carritoRepo.findById(id)
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    public CarritoDTO addProductToCarrito (int carritoID, int productID, int cantidad) {
        Carrito carrito = carritoRepo.findById(carritoID).orElse(null);
        if (carrito == null) return null;

        Product producto = productRepo.findById(productID).orElse(null);
        if (producto == null) return null;
        
        LineaCarrito linea = new LineaCarrito();
        linea.setCantidad(cantidad);
        linea.setProducto(producto);
        linea.setSubtotal(producto.getPrecioFinal() * cantidad);
        linea.setCarrito(carrito);

        carrito.getLineas().add(linea);

        carrito.setTotal(calcularTotal(carritoID));

        carritoRepo.save(carrito);
        return convertToDTO(carrito);
    }

    public double calcularTotal(int carritoID){
        Carrito carrito = carritoRepo.findById(carritoID).orElse(null);
        if (carrito == null) return 0;

        return carrito.getLineas().stream()
                    .mapToDouble(LineaCarrito::getSubtotal)
                    .sum();
    }

    private CarritoDTO convertToDTO (Carrito carrito){
        CarritoDTO dto = new CarritoDTO();
        dto.setId(carrito.getId());
        dto.setTotal(carrito.getTotal());
        
        List<LineaCarritoDTO> lineasDTO = new ArrayList<>();

        for (LineaCarrito linea : carrito.getLineas()) {
            LineaCarritoDTO l = new LineaCarritoDTO();
            l.setId(linea.getId());
            l.setCantidad(linea.getCantidad());
            l.setSubtotal(linea.getSubtotal());

            ProductDTO productoDTO = new ProductDTO();
            Product p = linea.getProducto();
            productoDTO.setId(p.getId());
            productoDTO.setNombre(p.getNombre());
            productoDTO.setPrecio(p.getPrecio());
            productoDTO.setPrecioFinal(p.getPrecioFinal());
            productoDTO.setDescripcion(p.getDescripcion()); 
            productoDTO.setMaterial(p.getMaterial());
            productoDTO.setConsideraciones(p.getConsideraciones());

            l.setProducto(productoDTO);

            lineasDTO.add(l);           
        }

        dto.setLineas(lineasDTO);
        return dto;
    }




}
