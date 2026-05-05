package com.tienda.tienda.carrito.application.service;

import com.tienda.tienda.carrito.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.tienda.carrito.application.port.output.UpdateLineaCarritoOutputPort;
import com.tienda.tienda.product.domain.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LineasCarritoUpdater {

    private final GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    private final UpdateLineaCarritoOutputPort updateLineaCarritoOutputPort;
    private final TotalCalculator totalCalculator;

    public LineasCarritoUpdater(GetLineaCarritoOutputPort getLineaCarritoOutputPort, UpdateLineaCarritoOutputPort updateLineaCarritoOutputPort, TotalCalculator totalCalculator) {
        this.getLineaCarritoOutputPort = getLineaCarritoOutputPort;
        this.updateLineaCarritoOutputPort = updateLineaCarritoOutputPort;
        this.totalCalculator = totalCalculator;
    }

    public Mono<Void> updateLineas(Product product) {
        return getLineaCarritoOutputPort.findAll()
                .filter(linea -> linea.getProductId().equals(product.getId()))
                .flatMap(linea -> {
                    linea.setSubtotal(product.getFinalPrice() * linea.getQuantity());
                    return updateLineaCarritoOutputPort.save(linea);
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