package com.tienda.tienda.service;

import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LineaCarritoService {
    
    private List<LineaCarrito> lineas = new ArrayList<>();

    public LineaCarritoDTO createLinea(LineaCarritoDTO dto) {
        LineaCarrito linea = new LineaCarrito();
        linea.setId(dto.getId());
        linea.setCantidad(dto.getCantidad());
        linea.setSubtotal(dto.getProducto().getPrecio() * dto.getCantidad());

        Product producto = new Product();
        ProductDTO prodDTO = dto.getProducto();
        if (prodDTO != null) {
            producto.setId(prodDTO.getId());
            producto.setNombre(prodDTO.getNombre());
            producto.setPrecio(prodDTO.getPrecio());
            producto.setDescripcion(prodDTO.getDescripcion());
            producto.setMaterial(prodDTO.getMaterial());
            producto.setConsideraciones(prodDTO.getConsideraciones());
            
            List<Promotion> promociones = new ArrayList<>();
            for (PromotionDTO promoDTO : prodDTO.getPromociones()){
                Promotion promo = new Promotion();
                promo.setId(promoDTO.getId());
                promo.setDescripcion(promoDTO.getDescripcion());
                promo.setDescuento(promo.getDescuento());
                promociones.add(promo);
            }
            producto.setPromociones(promociones);
        }

        linea.setProducto(producto);

        lineas.add(linea);

        return convertToDTO(linea);
    }

    public LineaCarritoDTO updateLinea(int id, int cantidad) {
        for (LineaCarrito linea : lineas) {
            if (linea.getId() == id){
                if (cantidad > 0) linea.setCantidad(cantidad);
                return convertToDTO(linea);
            }
        }
        return null;
    }

    public boolean deleteLinea (int id) {
        return lineas.removeIf(l -> l.getId() == id);
    }

    public LineaCarritoDTO getLineaById (int id){
        return lineas.stream()
                    .filter(l -> l.getId() == id)
                    .findFirst()
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    public List<LineaCarritoDTO> getAllLineas() {
        List<LineaCarritoDTO> listDTO = new ArrayList<>();
        for (LineaCarrito linea : lineas) {
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
