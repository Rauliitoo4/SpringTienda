package com.tienda.tienda.carrito.application.usecase;

import com.tienda.tienda.carrito.application.port.input.UpdateLineaCarritoInputPort;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.tienda.carrito.application.port.output.UpdateLineaCarritoOutputPort;
import com.tienda.tienda.carrito.application.helper.ProductLoader;
import com.tienda.tienda.carrito.application.helper.TotalCalculator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateLineaCarritoUseCase implements UpdateLineaCarritoInputPort {

    private final GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    private final UpdateLineaCarritoOutputPort updateLineaCarritoOutputPort;
    private final ProductLoader productLoader;
    private final TotalCalculator totalCalculator;

    public UpdateLineaCarritoUseCase(GetLineaCarritoOutputPort getLineaCarritoOutputPort, UpdateLineaCarritoOutputPort updateLineaCarritoOutputPort, ProductLoader productLoader, TotalCalculator totalCalculator) {
        this.getLineaCarritoOutputPort = getLineaCarritoOutputPort;
        this.updateLineaCarritoOutputPort = updateLineaCarritoOutputPort;
        this.productLoader = productLoader;
        this.totalCalculator = totalCalculator;
    }

    public Mono<LineaCarrito> execute(int id, int quantity) {
        return getLineaCarritoOutputPort.findById(id)
                .flatMap(linea -> {
                    if (quantity <= 0) return Mono.empty();
                    linea.setQuantity(quantity);
                    return productLoader.loadProduct(linea)
                            .flatMap(lineaWithProduct -> {
                                lineaWithProduct.setSubtotal(lineaWithProduct.getProduct().getFinalPrice() * quantity);
                                return updateLineaCarritoOutputPort.save(lineaWithProduct)
                                        .then(totalCalculator.recalculate(linea.getCarritoId()))
                                        .then(productLoader.loadProduct(linea));
                            });
                });
    }
}