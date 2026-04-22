package com.tienda.tienda.product.service;

import com.tienda.tienda.product.dto.mapper.ProductMapper;

import com.tienda.tienda.product.dto.ProductDTO;
import com.tienda.tienda.product.model.Product;
import com.tienda.tienda.product.repository.port.ProductRepositoryPort;
import com.tienda.tienda.product.repository.port.ProductPromotionRepositoryPort;
import com.tienda.tienda.promotion.repository.port.PromotionRepositoryPort;
import com.tienda.tienda.lineacarrito.service.LineaCarritoService;
import com.tienda.tienda.product.service.helper.PriceCalculator;
import com.tienda.tienda.product.service.helper.PromotionLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepositoryPort productRepo;
    private final PromotionRepositoryPort promotionRepo;
    private final ProductPromotionRepositoryPort productPromotionRepo;
    private final ProductMapper productMapper;
    private final PromotionLoader promotionLoader;
    private final LineaCarritoService lineaCarritoService;
    private final PriceCalculator priceCalculator;
    
    public ProductServiceImpl(ProductRepositoryPort productRepo, PromotionRepositoryPort promotionRepo, ProductPromotionRepositoryPort productPromotionRepo, ProductMapper productMapper, PromotionLoader promotionLoader, LineaCarritoService lineaCarritoService, PriceCalculator priceCalculator) {
        this.productRepo = productRepo;
        this.promotionRepo = promotionRepo;
        this.productPromotionRepo = productPromotionRepo;
        this.productMapper = productMapper;
        this.promotionLoader = promotionLoader;
        this.lineaCarritoService = lineaCarritoService;
        this.priceCalculator = priceCalculator;
    }

    public Mono<ProductDTO> createProduct(ProductDTO dto){
        Product product = productMapper.toEntity(dto);
        return productRepo.save(product)
                .flatMap(promotionLoader::loadPromotions)
                .map(productMapper::toDTO);
    }

    public Mono<ProductDTO> updateProduct(int id, ProductDTO dto) {
        return productRepo.findById(id)
                .flatMap(promotionLoader::loadPromotions)
                .flatMap(product -> {
                    if (dto.getName() != null) product.setName(dto.getName());
                    if (dto.getPrice() > 0) {
                        product.setPrice(dto.getPrice());
                        priceCalculator.recalculateFinalPrice(product);
                    }
                    if (dto.getDescription() != null) product.setDescription(dto.getDescription());
                    if (dto.getMaterial() != null) product.setMaterial(dto.getMaterial());
                    if (dto.getConsiderations() != null) product.setConsiderations(dto.getConsiderations());
                    return productRepo.save(product)
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
                .flatMap(promotionLoader::loadPromotions)
                .map(productMapper::toDTO);
    }

    public Mono<ProductDTO> getProductById(int id){
        return productRepo.findById(id)
                    .flatMap(promotionLoader::loadPromotions)
                    .map(productMapper::toDTO);
    }

    public Mono<ProductDTO> addPromotion(int productID, int promotionID) {
        return productRepo.findById(productID)
                .zipWith(promotionRepo.findById(promotionID))
                .flatMap(tuple -> {
                    Product product = tuple.getT1();
                    return productPromotionRepo.existsRelation(productID, promotionID)
                            .flatMap(count -> {
                                if (count > 0) return promotionLoader.loadPromotions(product).map(productMapper::toDTO);
                                return productPromotionRepo.insertRelation(productID, promotionID)
                                        .then(promotionLoader.loadPromotions(product))
                                        .flatMap(p -> {
                                            priceCalculator.recalculateFinalPrice(p);
                                            return productRepo.save(p)
                                                    .then(lineaCarritoService.updateLineasCarrito(p))
                                                    .thenReturn(productMapper.toDTO(p));
                                        });
                            });
                });
    }

    public Mono<ProductDTO> removePromotion(int productID, int promotionID) {
        return productRepo.findById(productID)
                .flatMap(product ->
                        productPromotionRepo.deleteByProductIdAndPromotionId(productID, promotionID)
                                .then(promotionLoader.loadPromotions(product))
                                .flatMap(p -> {
                                    priceCalculator.recalculateFinalPrice(p);
                                    return productRepo.save(p)
                                            .then(lineaCarritoService.updateLineasCarrito(p))
                                            .thenReturn(productMapper.toDTO(p));
                                }));
    }
}
