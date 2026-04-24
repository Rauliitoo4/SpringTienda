package com.tienda.tienda.product.application.usecase;

import com.tienda.tienda.product.domain.repository.ProductRepository;
import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import com.tienda.tienda.product.domain.service.PriceCalculator;
import com.tienda.tienda.product.application.dto.ProductResponse;
import com.tienda.tienda.product.application.mapper.ProductResponseMapper;
import com.tienda.tienda.product.application.helper.PromotionLoader;
import com.tienda.tienda.lineacarrito.application.service.LineaCarritoService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddPromotionUseCase {

    private final ProductRepository productRepository;
    private final ProductPromotionRepository productPromotionRepository;
    private final PromotionLoader promotionLoader;
    private final ProductResponseMapper mapper;
    private final LineaCarritoService lineaCarritoService;
    private final PriceCalculator priceCalculator = new PriceCalculator();

    public AddPromotionUseCase(ProductRepository productRepository, ProductPromotionRepository productPromotionRepository, PromotionLoader promotionLoader, ProductResponseMapper mapper, LineaCarritoService lineaCarritoService) {
        this.productRepository = productRepository;
        this.productPromotionRepository = productPromotionRepository;
        this.promotionLoader = promotionLoader;
        this.mapper = mapper;
        this.lineaCarritoService = lineaCarritoService;
    }

    public Mono<ProductResponse> execute (int productId, int promotionId) {
        return productRepository.findById(productId)
                .flatMap(product ->
                    productPromotionRepository.existsRelation(productId, promotionId)
                            .flatMap(count -> {
                                if (count > 0) {
                                    return promotionLoader.loadPromotions(product)
                                            .map(mapper::toResponse);
                                }
                                return productPromotionRepository.insertRelation(productId, promotionId)
                                        .then(promotionLoader.loadPromotions(product))
                                        .flatMap(p -> {
                                            priceCalculator.recalculateFinalPrice(p);
                                            return productRepository.save(p)
                                                    .then(lineaCarritoService.updateLineasCarrito(p))
                                                    .thenReturn(mapper.toResponse(p));
                                        });
                            })
                );
    }
}
