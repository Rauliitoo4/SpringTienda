package com.tienda.tienda.service;

import com.tienda.tienda.model.*;
import com.tienda.tienda.dto.*;
import com.tienda.tienda.repository.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
public class LineaCarritoService {
    
    private final LineaCarritoRepository lineaRepo;
    private final CarritoRepository carritoRepo;
    private final ProductRepository productRepo;
    private final PromotionRepository promoRepo;
    private final ProductoPromocionRepository productoPromocionRepo;

    public LineaCarritoService(LineaCarritoRepository lineaRepo, CarritoRepository carritoRepo, ProductRepository productRepo, PromotionRepository promoRepo, ProductoPromocionRepository productoPromocionRepo){
        this.lineaRepo = lineaRepo;
        this.carritoRepo = carritoRepo;
        this.productRepo = productRepo;
        this.promoRepo = promoRepo;
        this.productoPromocionRepo = productoPromocionRepo;
    }

    public Mono<LineaCarritoDTO> updateLinea(int id, int cantidad) {
        return lineaRepo.findById(id)
                .flatMap(linea -> {
                    if (cantidad <= 0) return Mono.empty();
                    linea.setCantidad(cantidad);
                    return productRepo.findById(linea.getProductoId())
                            .flatMap(producto -> {
                                linea.setSubtotal(producto.getPrecioFinal() * cantidad);
                                return lineaRepo.save(linea)
                                        .then(recalcularTotal(linea.getCarritoId()))
                                        .then(cargarProducto(linea));
                            });
                })
                .flatMap(this::convertToDTO);
    }

    public Mono<Boolean> deleteLinea (int id) {
        return lineaRepo.findById(id)
                .flatMap(linea -> {
                    Integer carritoId = linea.getCarritoId();
                    return lineaRepo.deleteById(id)
                            .then(recalcularTotal(carritoId))
                            .thenReturn(true);
                })
                .defaultIfEmpty(false);
    }

    public Mono<LineaCarritoDTO> getLineaById (int id){
        return lineaRepo.findById(id)
                .flatMap(this::cargarProducto)
                .flatMap(this::convertToDTO);
    }

    public Flux<LineaCarritoDTO> getAllLineas() {
        return lineaRepo.findAll()
                .flatMap(this::cargarProducto)
                .flatMap(this::convertToDTO);
    }

    private Mono<LineaCarrito> cargarProducto(LineaCarrito linea) {
        return productRepo.findById(linea.getProductoId())
                .flatMap(producto ->
                    productoPromocionRepo.findPromotionIdsByProductId(producto.getId())
                            .flatMap(promoId -> promoRepo.findById(promoId))
                            .collectList()
                            .doOnNext(producto::setPromociones)
                            .thenReturn(producto)
                )
                .doOnNext(linea::setProducto)
                .thenReturn(linea);
    }

    private Mono<Void> recalcularTotal(Integer carritoId) {
        return lineaRepo.findByCarritoId(carritoId)
                .map(LineaCarrito::getSubtotal)
                .reduce(0.0, Double::sum)
                .flatMap(total -> carritoRepo.findById(carritoId)
                        .flatMap(carrito -> {
                            carrito.setTotal(total);
                            return carritoRepo.save(carrito);
                        }))
                .then();
    }

    private Mono<LineaCarritoDTO> convertToDTO(LineaCarrito linea) {
        LineaCarritoDTO dto = new LineaCarritoDTO();
        dto.setId(linea.getId());
        dto.setCantidad(linea.getCantidad());
        dto.setSubtotal(linea.getSubtotal());
        dto.setCarritoId(linea.getCarritoId());

        Product producto = linea.getProducto();
        if (producto != null) {
            ProductDTO productoDTO = new ProductDTO();
            productoDTO.setId(producto.getId());
            productoDTO.setNombre(producto.getNombre());
            productoDTO.setPrecio(producto.getPrecio());
            productoDTO.setPrecioFinal(producto.getPrecioFinal());
            productoDTO.setDescripcion(producto.getDescripcion());
            productoDTO.setMaterial(producto.getMaterial());
            productoDTO.setConsideraciones(producto.getConsideraciones());

            if (producto.getPromociones() != null) {
                productoDTO.setPromociones(producto.getPromociones().stream().map(promo -> {
                    PromotionDTO pDTO = new PromotionDTO();
                    pDTO.setId(promo.getId());
                    pDTO.setDescuento(promo.getDescuento());
                    pDTO.setDescripcion(promo.getDescripcion());
                    return pDTO;
                }).collect(Collectors.toList()));
            }
            dto.setProducto(productoDTO);
        }
        return Mono.just(dto);
    }
}
