package com.tienda.tienda.service;

import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import com.tienda.tienda.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LineaCarritoService {
    
    private final LineaCarritoRepository lineaRepo;
    private final CarritoRepository carritoRepo;

    public LineaCarritoService(LineaCarritoRepository lineaRepo, CarritoRepository carritoRepo){
        this.lineaRepo = lineaRepo;
        this.carritoRepo = carritoRepo;
    }

    public LineaCarritoDTO updateLinea(int id, int cantidad) {
        LineaCarrito linea = lineaRepo.findById(id).orElse(null);
        if (linea == null) return null;

        if (cantidad > 0) {
            linea.setCantidad(cantidad);
            linea.setSubtotal(linea.getProducto().getPrecioFinal() * cantidad);
        }

        Carrito carrito = linea.getCarrito();
        double total = carrito.getLineas().stream()
                    .mapToDouble(LineaCarrito::getSubtotal)
                    .sum();
        carrito.setTotal(total);
        carritoRepo.save(carrito);

        lineaRepo.save(linea);
        return convertToDTO(linea);
    }

    public boolean deleteLinea (int id) {
        LineaCarrito linea = lineaRepo.findById(id).orElse(null);
        if (linea == null) return false;

        Carrito carrito = linea.getCarrito();
        carrito.getLineas().remove(linea);

        lineaRepo.deleteById(id);

        double total = carrito.getLineas().stream()     
                    .mapToDouble(LineaCarrito::getSubtotal)
                    .sum();
        carrito.setTotal(total);
        carritoRepo.save(carrito);
        
        return true;
    }

    public LineaCarritoDTO getLineaById (int id){
        return lineaRepo.findById(id)
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    public List<LineaCarritoDTO> getAllLineas() {
        List<LineaCarritoDTO> listDTO = new ArrayList<>();
        for (LineaCarrito linea : lineaRepo.findAll()) {
            listDTO.add(convertToDTO(linea));
        }
        return listDTO;
    }

    private LineaCarritoDTO convertToDTO(LineaCarrito linea) {
        LineaCarritoDTO dto = new LineaCarritoDTO();
        dto.setId(linea.getId());
        dto.setCantidad(linea.getCantidad());
        dto.setSubtotal(linea.getSubtotal());

        Product producto = linea.getProducto();
        ProductDTO productoDTO = new ProductDTO();
        productoDTO.setId(producto.getId());
        productoDTO.setNombre(producto.getNombre());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setPrecioFinal(producto.getPrecioFinal());
        productoDTO.setDescripcion(producto.getDescripcion());
        productoDTO.setMaterial(producto.getMaterial());
        productoDTO.setConsideraciones(producto.getConsideraciones());
        
        List<PromotionDTO> promosDTO = new ArrayList<>();
        for (Promotion promo : producto.getPromociones()) {
            PromotionDTO pDTO = new PromotionDTO();
            pDTO.setId(promo.getId());
            pDTO.setDescuento(promo.getDescuento());
            pDTO.setDescripcion(promo.getDescripcion()); 
            promosDTO.add(pDTO);
        }

        productoDTO.setPromociones(promosDTO);
        dto.setProducto(productoDTO);
        
        return dto;
    }
}
