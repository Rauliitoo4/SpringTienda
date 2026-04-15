package com.tienda.tienda.service;

import com.tienda.tienda.dto.mapper.CarritoMapper;
import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import com.tienda.tienda.repository.*;
import com.tienda.tienda.service.helper.LineaLoader;
import com.tienda.tienda.service.helper.ProductLoader;
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
    private final CarritoMapper carritoMapper;
    private final LineaLoader lineaLoader;

    public CarritoService(ProductRepository productRepo, CarritoRepository carritoRepo, LineaCarritoRepository lineaRepo, CarritoMapper carritoMapper, LineaLoader lineaLoader) {
        this.productRepo = productRepo;
        this.carritoRepo = carritoRepo;
        this.lineaRepo = lineaRepo;
        this.carritoMapper = carritoMapper;
        this.lineaLoader = lineaLoader;
    }

    public Mono<CarritoDTO> getCarritoById (int id){
        return carritoRepo.findById(id)
                    .flatMap(lineaLoader::cargarLineas)
                    .map(carritoMapper::toDTO);
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
                            .flatMap(lineaLoader::cargarLineas)
                            .map(carritoMapper::toDTO);
                });
    }

    public Mono<Carrito> createCarrito() {
        Carrito carrito = new Carrito();
        carrito.setTotal(0.0);
        return carritoRepo.save(carrito);
    }

    public Mono<Double> calcularTotal(int carritoID){
        return lineaRepo.findByCarritoId(carritoID)
                .map(LineaCarrito::getSubtotal)
                .reduce(0.0, Double::sum);
    }

    public Mono<Void> recalcularTotal(int carritoID) {
        return calcularTotal(carritoID)
                .flatMap(total -> carritoRepo.findById(carritoID)
                        .flatMap(carrito -> {
                            carrito.setTotal(total);
                            return carritoRepo.save(carrito);
                        }))
                .then();
    }
}
