package com.tienda.tienda.service;

import com.tienda.tienda.dto.mapper.LineaCarritoMapper;
import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import com.tienda.tienda.repository.*;
import com.tienda.tienda.service.helper.ProductLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class LineaCarritoService {

    private final LineaCarritoRepository lineaRepo;
    private final CarritoRepository carritoRepo;
    private final LineaCarritoMapper lineaCarritoMapper;
    private final ProductLoader productLoader;
    private final CarritoService carritoService;

    public LineaCarritoService(LineaCarritoRepository lineaRepo, CarritoRepository carritoRepo, LineaCarritoMapper lineaCarritoMapper, ProductLoader productLoader, CarritoService carritoService){
        this.lineaRepo = lineaRepo;
        this.carritoRepo = carritoRepo;
        this.lineaCarritoMapper = lineaCarritoMapper;
        this.productLoader = productLoader;
        this.carritoService = carritoService;
    }

    public Mono<LineaCarritoDTO> updateLinea(int id, int cantidad) {
        return lineaRepo.findById(id)
                .flatMap(linea -> {
                    if (cantidad <= 0) return Mono.empty();
                    linea.setCantidad(cantidad);
                    return productLoader.cargarProducto(linea)
                            .flatMap(lineaConProducto -> {
                                lineaConProducto.setSubtotal(lineaConProducto.getProducto().getPrecioFinal() * cantidad);
                                return lineaRepo.save(linea)
                                        .then(carritoService.recalcularTotal(linea.getCarritoId()))
                                        .then(productLoader.cargarProducto(linea));
                            });
                })
                .map(lineaCarritoMapper::toDTO);
    }

    public Mono<Boolean> deleteLinea (int id) {
        return lineaRepo.findById(id)
                .flatMap(linea -> {
                    Integer carritoId = linea.getCarritoId();
                    return lineaRepo.deleteById(id)
                            .then(carritoService.recalcularTotal(carritoId))
                            .thenReturn(true);
                })
                .defaultIfEmpty(false);
    }

    public Mono<LineaCarritoDTO> getLineaById (int id){
        return lineaRepo.findById(id)
                .flatMap(productLoader::cargarProducto)
                .map(lineaCarritoMapper::toDTO);
    }

    public Flux<LineaCarritoDTO> getAllLineas() {
        return lineaRepo.findAll()
                .flatMap(productLoader::cargarProducto)
                .map(lineaCarritoMapper::toDTO);
    }

    public Mono<Void> actualizarLineasCarrito(Product producto) {
        return lineaRepo.findAll()
                .filter(linea -> linea.getProductoId().equals(producto.getId()))
                .flatMap(linea -> {
                    linea.setSubtotal(producto.getPrecioFinal() * linea.getCantidad());
                    return lineaRepo.save(linea);
                })
                .collectList()
                .flatMap(lineasActualizadas -> {
                    if (lineasActualizadas.isEmpty()) return Mono.empty();
                    Integer carritoId = lineasActualizadas.get(0).getCarritoId();
                    return carritoService.recalcularTotal(carritoId);
                })
                .then();
    }
}
