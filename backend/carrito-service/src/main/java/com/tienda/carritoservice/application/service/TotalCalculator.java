package com.tienda.tienda.carrito.application.service;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.application.port.output.GetCarritoOutputPort;
import com.tienda.tienda.carrito.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.tienda.carrito.application.port.output.UpdateCarritoOutputPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TotalCalculator {

    private final GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    private final GetCarritoOutputPort getCarritoOutputPort;
    private final UpdateCarritoOutputPort updateCarritoOutputPort;

    public TotalCalculator(GetLineaCarritoOutputPort getLineaCarritoOutputPort, GetCarritoOutputPort getCarritoOutputPort, UpdateCarritoOutputPort updateCarritoOutputPort) {
        this.getLineaCarritoOutputPort = getLineaCarritoOutputPort;
        this.getCarritoOutputPort = getCarritoOutputPort;
        this.updateCarritoOutputPort = updateCarritoOutputPort;
    }

    public Mono<Void> recalculate(int carritoId) {
        return getLineaCarritoOutputPort.findByCarritoId(carritoId)
                .map(LineaCarrito::getSubtotal)
                .reduce(0.0, Double::sum)
                .flatMap(total -> getCarritoOutputPort.findById(carritoId)
                        .flatMap(carrito -> {
                            carrito.setTotal(total);
                            return updateCarritoOutputPort.save(carrito);
                        }))
                .then();
    }
}