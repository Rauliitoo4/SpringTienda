package com.tienda.tienda.service;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.dto.mapper.ProductMapper;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.Promotion;
import com.tienda.tienda.repository.*;

import com.tienda.tienda.service.helper.PrecioCalculator;
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
    private final ProductoPromocionRepository productoPromocionRepo;
    private final ProductMapper productMapper;
    private final PromotionLoader promotionLoader;
    private final LineaCarritoService lineaCarritoService;
    private final PrecioCalculator precioCalculator;
    
    public ProductService(ProductRepository productRepo, PromotionRepository promotionRepo, ProductoPromocionRepository productoPromocionRepo, ProductMapper productMapper, PromotionLoader promotionLoader, LineaCarritoService lineaCarritoService, PrecioCalculator precioCalculator) {
        this.productRepo = productRepo;
        this.promotionRepo = promotionRepo;
        this.productoPromocionRepo = productoPromocionRepo;
        this.productMapper = productMapper;
        this.promotionLoader = promotionLoader;
        this.lineaCarritoService = lineaCarritoService;
        this.precioCalculator = precioCalculator;
    }

    public Mono<ProductDTO> createProduct(ProductDTO dto){
        Product product = productMapper.toEntity(dto);
        return productRepo.save(product)
                .flatMap(promotionLoader::cargarPromociones)
                .map(productMapper::toDTO);
    }

    public Mono<ProductDTO> updateProduct(int id, ProductDTO dto) {
        return productRepo.findById(id)
                .flatMap(promotionLoader::cargarPromociones)
                .flatMap(producto -> {
                    if (dto.getNombre() != null) producto.setNombre(dto.getNombre());
                    if (dto.getPrecio() > 0) {
                        producto.setPrecio(dto.getPrecio());
                        precioCalculator.recalcularPrecioFinal(producto);
                    }
                    if (dto.getDescripcion() != null) producto.setDescripcion(dto.getDescripcion());
                    if (dto.getMaterial() != null) producto.setMaterial(dto.getMaterial());
                    if (dto.getConsideraciones() != null) producto.setConsideraciones(dto.getConsideraciones());
                    return productRepo.save(producto)
                            .map(productMapper::toDTO);
                });
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
                                            precioCalculator.recalcularPrecioFinal(p);
                                            return productRepo.save(p)
                                                    .then(lineaCarritoService.actualizarLineasCarrito(p))
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
                                    precioCalculator.recalcularPrecioFinal(p);
                                    return productRepo.save(p)
                                            .then(lineaCarritoService.actualizarLineasCarrito(p))
                                            .thenReturn(productMapper.toDTO(p));
                                }));
    }
}
