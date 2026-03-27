package com.tienda.tienda.service;

import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {
    
    private List<Carrito> carritos = new ArrayList<>();
    private final ProductService productService;

    public CarritoService(ProductService productService) {
        this.productService = productService;
    }

    public CarritoDTO createCarrito() {
        Carrito carrito = new Carrito();
        carrito.setLineas(new ArrayList<>());
        carritos.add(carrito);
        return convertToDTO(carrito);
    }

    public boolean deleteCarrito (int id) {
        return carritos.removeIf(c -> c.getId() == id);
    }

    public CarritoDTO getCarritoById (int id){
        return carritos.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .map(this::convertToDTO)
                    .orElse(null);
    }

    public CarritoDTO addProductToCarrito (int carritoID, int productID, int cantidad) {
        Carrito carrito = carritos.stream()         
                    .filter(c -> c.getId() == carritoID)
                    .findFirst()
                    .orElse(null);
        
        if (carrito == null) return null;

        //Obtener el producto desde el Servicio
        ProductDTO productoDTO = productService.getProductById(productID);
        if (productoDTO == null) return null;

        //Si el producto es correcto creamos la linea del carrito
        LineaCarrito linea = new LineaCarrito();
        linea.setId((int) (carrito.getLineas().size() + 1));
        linea.setCantidad(cantidad);
        linea.setSubtotal(productoDTO.getPrecio() * cantidad);

        //Convertir de DTO a model
        Product producto = new Product();
        producto.setId(productoDTO.getId());
        producto.setNombre(productoDTO.getNombre());
        producto.setPrecio(productoDTO.getPrecio());

        //Ya guardamos el producto en la linea
        linea.setProducto(producto);
        
        //Añadimos la nueva linea al carrito
        carrito.getLineas().add(linea);

        //Actualizar total
        carrito.setTotal(calcularTotal(carritoID));

        //Devuelvo la versión DTO del carrito actualizado
        return convertToDTO(carrito);


    }

    public double calcularTotal(int carritoID){
        Carrito carrito = carritos.stream()
                    .filter(c -> c.getId() == carritoID)
                    .findFirst()
                    .orElse(null);
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

            l.setProducto(productoDTO);

            lineasDTO.add(l);           
        }

        dto.setLineas(lineasDTO);
        return dto;
    }




}
