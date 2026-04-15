package com.tienda.tienda.service;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.dto.mapper.ProductMapper;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.Promotion;
import com.tienda.tienda.repository.*;

import com.tienda.tienda.service.helper.PromotionLoader;
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
    private final ProductMapper productMapper;
    private final PromotionLoader promotionLoader;
    
    public ProductService(ProductRepository productRepo, PromotionRepository promotionRepo, CarritoRepository carritoRepo, LineaCarritoRepository lineaRepo, ProductoPromocionRepository productoPromocionRepo, ProductMapper productMapper, PromotionLoader promotionLoader) {
        this.productRepo = productRepo;
        this.promotionRepo = promotionRepo;
        this.carritoRepo = carritoRepo;
        this.lineaRepo = lineaRepo;
        this.productoPromocionRepo = productoPromocionRepo;
        this.productMapper = productMapper;
        this.promotionLoader = promotionLoader;
    }

    public Mono<ProductDTO> createProduct(ProductDTO dto){
        Product product = productMapper.toEntity(dto);
        return productRepo.save(product)
                .flatMap(promotionLoader::cargarPromociones)
                .map(productMapper::toDTO);
    }

    public Mono<ProductDTO> updateProduct(int id, ProductDTO dto){
        return productRepo.findById(id)
                .flatMap(producto -> promotionLoader.cargarPromociones(producto)
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
                                    .map(productMapper::toDTO);
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
                .flatMap(promotionLoader::cargarPromociones)
                .map(productMapper::toDTO);
    }

    public Mono<ProductDTO> getProductById(int id){
        return productRepo.findById(id)
                    .flatMap(promotionLoader::cargarPromociones)
                    .map(productMapper::toDTO);
    }

    public Mono<ProductDTO> addPromotion(int productoID, int promocionID) {
        return productRepo.findById(productoID)
                .zipWith(promotionRepo.findById(promocionID))
                .flatMap(tuple -> {
                    Product producto = tuple.getT1();
                    return productoPromocionRepo.existsRelation(productoID, promocionID)
                            .flatMap(count -> {
                                if (count > 0) return promotionLoader.cargarPromociones(producto).map(productMapper::toDTO);
                                return productoPromocionRepo.insertRelation(productoID, promocionID)
                                        .then(promotionLoader.cargarPromociones(producto))
                                        .flatMap(p -> {
                                            recalcularPrecioFinal(p);
                                            return productRepo.save(p)
                                                    .then(actualizarLineasCarrito(p))
                                                    .thenReturn(productMapper.toDTO(p));
                                        });
                            });
                });
    }

    public Mono<ProductDTO> removePromotion(int productoID, int promocionID) {
        return productRepo.findById(productoID)
                .flatMap(producto ->
                        productoPromocionRepo.deleteByProductIdAndPromotionId(productoID, promocionID)
                                .then(promotionLoader.cargarPromociones(producto))
                                .flatMap(p -> {
                                    recalcularPrecioFinal(p);
                                    return productRepo.save(p)
                                            .then(actualizarLineasCarrito(p))
                                            .thenReturn(productMapper.toDTO(p));
                                }));
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
                .filter(linea -> linea.getProductoId().equals(producto.getId()))
                .flatMap(linea -> {
                    linea.setSubtotal(producto.getPrecioFinal() * linea.getCantidad());
                    return lineaRepo.save(linea);
                })
                .collectList()
                .flatMap(lineasActualizadas -> {
                    if (lineasActualizadas.isEmpty()) return Mono.empty();
                    Integer carritoId = lineasActualizadas.get(0).getCarritoId();
                    return lineaRepo.findByCarritoId(carritoId)
                            .map(linea -> linea.getSubtotal())
                            .reduce(0.0, Double::sum)
                            .flatMap(total -> carritoRepo.findById(carritoId)
                                    .flatMap(carrito -> {
                                        carrito.setTotal(total);
                                        return carritoRepo.save(carrito);
                                    }));
                })
                .then();
    }
}
