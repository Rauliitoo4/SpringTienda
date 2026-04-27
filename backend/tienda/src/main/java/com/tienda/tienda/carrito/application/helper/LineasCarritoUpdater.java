package com.tienda.tienda.carrito.application.helper;

import com.tienda.tienda.carrito.domain.repository.GetLineaCarritoRepository;
import com.tienda.tienda.carrito.domain.repository.UpdateLineaCarritoRepository;
import com.tienda.tienda.product.domain.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LineasCarritoUpdater {

    private final GetLineaCarritoRepository getLineaCarritoRepository;
    private final UpdateLineaCarritoRepository updateLineaCarritoRepository;
    private final TotalCalculator totalCalculator;

    public LineasCarritoUpdater(GetLineaCarritoRepository getLineaCarritoRepository, UpdateLineaCarritoRepository updateLineaCarritoRepository, TotalCalculator totalCalculator) {
        this.getLineaCarritoRepository = getLineaCarritoRepository;
        this.updateLineaCarritoRepository = updateLineaCarritoRepository;
        this.totalCalculator = totalCalculator;
    }

    public Mono<Void> updateLineas(Product product) {
        return getLineaCarritoRepository.findAll()
                .filter(linea -> linea.getProductId().equals(product.getId()))
                .flatMap(linea -> {
                    linea.setSubtotal(product.getFinalPrice() * linea.getQuantity());
                    return updateLineaCarritoRepository.save(linea);
                })
                .collectList()
                .flatMap(updatedLineas -> {
                    if (updatedLineas.isEmpty()) return Mono.empty();
                    Integer carritoId = updatedLineas.get(0).getCarritoId();
                    return totalCalculator.recalculate(carritoId);
                })
                .then();
    }
}