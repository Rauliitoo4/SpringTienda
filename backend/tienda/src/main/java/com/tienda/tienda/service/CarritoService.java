package com.tienda.tienda.service;

import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import com.tienda.tienda.repository.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarritoService {
    
    private final CarritoRepository carritoRepo;
    private final ProductRepository productRepo;
    private final LineaCarritoRepository lineaRepo;

    public CarritoService(ProductRepository productRepo, CarritoRepository carritoRepo, LineaCarritoRepository lineaRepo) {
        this.productRepo = productRepo;
        this.carritoRepo = carritoRepo;
        this.lineaRepo = lineaRepo;
    }

    public Mono<CarritoDTO> getCarritoById (int id){
        return carritoRepo.findById(id)
                    .map(this::convertToDTO);
    }

    public Mono<CarritoDTO> addProductToCarrito (int carritoID, int productID, int cantidad) {
        return carritoRepo.findById(carritoID)
                .zipWith(productRepo.findById(productID))
                .flatMap(tupla -> {
                    Carrito carrito = tupla.getT1();
                    Product producto = tupla.getT2();

                    LineaCarrito linea = new LineaCarrito();
                    linea.setCantidad(cantidad);
                    linea.setProductoId(producto.getId());
                    linea.setCarritoId(carrito.getId());
                    linea.setSubtotal(producto.getPrecioFinal() * cantidad);
                    linea.setProducto(producto);

                    return lineaRepo.save(linea)
                            .then(recalcularTotal(carritoID))
                            .then(carritoRepo.findById(carritoID))
                            .flatMap(this::cargarLineas);
                });
    }

    public Mono<Double> calcularTotal(int carritoID){
        return lineaRepo.findByCarritoId(carritoID)
                .map(LineaCarrito::getSubtotal)
                .reduce(0.0, Double::sum);
    }

    private Mono<Void> recalcularTotal(int carritoID) {
        return calcularTotal(carritoID)
                .flatMap(total -> carritoRepo.findById(carritoID)
                        .flatMap(carrito -> {
                            carrito.setTotal(total);
                            return carritoRepo.save(carrito);
                        }))
                .then();
    }

    private Mono<CarritoDTO> cargarLineas (Carrito carrito) {
        return lineaRepo.findByCarritoId(carrito.getId())
                .flatMap(linea ->
                    productRepo.findById(linea.getProductoId())
                            .doOnNext(linea::setProducto)
                            .thenReturn(linea)
                )
                .collectList()
                .map(lineas -> {
                    carrito.setLineas(lineas);
                    return convertToDTO(carrito);
                });
    }

    private CarritoDTO convertToDTO (Carrito carrito){
        CarritoDTO dto = new CarritoDTO();
        dto.setId(carrito.getId());
        dto.setTotal(carrito.getTotal());
        
        List<LineaCarritoDTO> lineasDTO = new ArrayList<>();

        if (carrito.getLineas() != null) {
            dto.setLineas(carrito.getLineas().stream().map(linea -> {
                LineaCarritoDTO l = new LineaCarritoDTO();
                l.setId(linea.getId());
                l.setCantidad(linea.getCantidad());
                l.setSubtotal(linea.getSubtotal());
                l.setCarritoId(linea.getCarritoId());

                Product p = linea.getProducto();
                if (p != null) {
                    ProductDTO productoDTO = new ProductDTO();
                    productoDTO.setId(p.getId());
                    productoDTO.setNombre(p.getNombre());
                    productoDTO.setPrecio(p.getPrecio());
                    productoDTO.setPrecioFinal(p.getPrecioFinal());
                    productoDTO.setDescripcion(p.getDescripcion());
                    productoDTO.setMaterial(p.getMaterial());
                    productoDTO.setConsideraciones(p.getConsideraciones());
                    l.setProducto(productoDTO);
                }
                return l;
            }).collect(java.util.stream.Collectors.toList()));
        }
        return dto;
    }




}
