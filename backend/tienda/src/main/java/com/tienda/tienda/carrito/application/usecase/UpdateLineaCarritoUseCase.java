package com.tienda.tienda.carrito.application.usecase;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.domain.repository.GetLineaCarritoRepository;
import com.tienda.tienda.carrito.domain.repository.UpdateLineaCarritoRepository;
import com.tienda.tienda.carrito.application.helper.ProductLoader;
import com.tienda.tienda.carrito.application.helper.TotalCalculator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateLineaCarritoUseCase {

    private final GetLineaCarritoRepository getLineaCarritoRepository;
    private final UpdateLineaCarritoRepository updateLineaCarritoRepository;
    private final ProductLoader productLoader;
    private final TotalCalculator totalCalculator;

    public UpdateLineaCarritoUseCase(GetLineaCarritoRepository getLineaCarritoRepository, UpdateLineaCarritoRepository updateLineaCarritoRepository, ProductLoader productLoader, TotalCalculator totalCalculator) {
        this.getLineaCarritoRepository = getLineaCarritoRepository;
        this.updateLineaCarritoRepository = updateLineaCarritoRepository;
        this.productLoader = productLoader;
        this.totalCalculator = totalCalculator;
    }

    public Mono<LineaCarrito> execute(int id, int quantity) {
        return getLineaCarritoRepository.findById(id)
                .flatMap(linea -> {
                    if (quantity <= 0) return Mono.empty();
                    linea.setQuantity(quantity);
                    return productLoader.loadProduct(linea)
                            .flatMap(lineaWithProduct -> {
                                lineaWithProduct.setSubtotal(lineaWithProduct.getProduct().getFinalPrice() * quantity);
                                return updateLineaCarritoRepository.save(lineaWithProduct)
                                        .then(totalCalculator.recalculate(linea.getCarritoId()))
                                        .then(productLoader.loadProduct(linea));
                            });
                });
    }
}