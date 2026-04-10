package com.tienda.tienda.service;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.Promotion;
import com.tienda.tienda.repository.*;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final PromotionRepository promotionRepo;
    private final CarritoRepository carritoRepo;
    private final LineaCarritoRepository lineaRepo;
    private final ProductoPromocionRepository productoPromocionRepo;
    
    
    public ProductService(ProductRepository productRepo, PromotionRepository promotionRepo, CarritoRepository carritoRepo, LineaCarritoRepository lineaRepo, ProductoPromocionRepository productoPromocionRepo) {
        this.productRepo = productRepo;
        this.promotionRepo = promotionRepo;
        this.carritoRepo = carritoRepo;
        this.lineaRepo = lineaRepo;
        this.productoPromocionRepo = productoPromocionRepo;
    }

    private Mono<ProductDTO> cargarPromociones(Product product) {
        return productoPromocionRepo
                .findPromotionIdsByProductId(product.getId())
                .flatMap(promoId -> promotionRepo.findById(promoId))
                .collectList()
                .doOnNext(product::setPromociones)
                .thenReturn(product);
    }

    public Mono<ProductDTO> createProduct(ProductDTO dto){
        Product product = convertToEntity(dto);
        return productRepo.save(product)
                .flatMap(this::cargarPromociones)
                .map(this::convertToDTO);
    }

    public Mono<ProductDTO> updateProduct(int id, ProductDTO dto){
        return productRepo.findById(id)
                .flatMap(producto -> cargarPromociones(producto)
                        .flatMap(productoConPromos -> {
                            if (dto.getNombre() != null) productoConPromos.setNombre(dto.getNombre());
                            if (dto.getPrecio() >= 0) {
                                productoConPromos.setPrecio(dto.getPrecio());
                                recalcularPrecioFinal(productoConPromos);
                            }
                            if (dto.getDescripcion() != null) productoConPromos.setDescripcion(dto.getDescripcion());
                            if (dto.getMaterial() != null) productoConPromos.setMaterial(dto.getMaterial());
                            if (dto.getConsideraciones() != null) productoConPromos.setConsideraciones(dto.getConsideraciones());
                            return productRepo.save(productoConPromos)
                                    .map(this::convertToDTO);
                        }));
    }

    public Mono<Boolean> deleteProduct (int id) {
        return productRepo.existsById(id)
                .flatMap(exists -> {
                    if (!exists) return Mono.just(false);
                    return productRepo.deleteById(id).thenReturn(true);
                });
    }

    public Flux<ProductDTO> getAllProducts() {
        return productRepo.findAll()
                .flatMap(this::cargarPromociones)
                .map(this::convertToDTO);
    }

    public Mono<ProductDTO> getProductById(int id){
        return productRepo.findById(id)
                    .flatMap(this::cargarPromociones)
                    .map(this::convertToDTO);
    }

    public Mono<ProductDTO> addPromotion(int productoID, int promocionID) {
        return productRepo.findById(productoID)
                .zipWith(promotionRepo.findById(promocionID))
                .flatMap(tuple -> {
                    Product producto = tuple.getT1();
                    return productoPromocionRepo.existsRelation(productoID, promocionID)
                            .flatMap(count -> {
                                if (count > 0) return cargarPromociones(producto).map(this::convertToDTO);
                                ProductoPromocionRepository.ProductoPromocion rel =
                                        new ProductoPromocionRepository.ProductoPromocion();
                                rel.setProductoId(productoID);
                                rel.setPromotionId(promocionID);
                                return productoPromocionRepo.save(rel)
                                        .then(cargarPromociones(producto))
                                        .flatMap(p -> {
                                            recalcularPrecioFinal(p);
                                            return productRepo.save(p)
                                                    .then(actualizarLineasCarrito(p))
                                                    .thenReturn(convertToDTO(p));
                                        });
                            });
                });
    }

    public Mono<ProductDTO> removePromotion(int productoID, int promocionID) {
        return productRepo.findById(productoID)
                .flatMap(producto ->
                        productoPromocionRepo.deleteByProductIdAndPromotionId(productoID, promocionID)
                                .then(cargarPromociones(producto))
                                .flatMap(p -> {
                                    recalcularPrecioFinal(p);
                                    return productRepo.save(p)
                                            .then(actualizarLineasCarrito(p))
                                            .thenReturn(convertToDTO(p));
                                }));
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

        if (p.getPromociones() != null) {
            List<PromotionDTO> promosDTO = p.getPromociones().stream().map(promo -> {
                PromotionDTO pDTO = new PromotionDTO();
                pDTO.setId(promo.getId());
                pDTO.setDescripcion(promo.getDescripcion());
                pDTO.setDescuento(promo.getDescuento());
                return pDTO;
            }).collect(Collectors.toList());
            dto.setPromociones(promosDTO);
        }
        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product p = new Product();
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
            return;
        }
        double maxDescuento = producto.getPromociones().stream()
                .mapToDouble(Promotion::getDescuento)
                .max()
                .orElse(0);
        producto.setPrecioFinal(producto.getPrecio() * (1 - maxDescuento / 100));
    }

    private Mono<Void> actualizarLineasCarrito(Product producto) {
        return lineaRepo.findAll()
                .filter(linea -> linea.getProductoId() == producto.getId())
                .flatMap(linea -> {
                    linea.setSubtotal(producto.getPrecioFinal() * linea.getCantidad());
                    return lineaRepo.save(linea)
                            .then(carritoRepo.findById(linea.getCarritoId()))
                            .flatMap(carrito ->
                                    lineaRepo.findByCarritoId(carrito.getId())
                                            .mapToDouble(l -> l) // ver nota abajo
                                            .then()
                            );
                }).then();
    }
}
