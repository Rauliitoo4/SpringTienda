package com.tienda.tienda.carrito.application.helper;

import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.domain.repository.GetCarritoRepository;
import com.tienda.tienda.carrito.domain.repository.GetLineaCarritoRepository;
import com.tienda.tienda.carrito.domain.repository.UpdateCarritoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TotalCalculator {

    private final GetLineaCarritoRepository getLineaCarritoRepository;
    private final GetCarritoRepository getCarritoRepository;
    private final UpdateCarritoRepository updateCarritoRepository;

    public TotalCalculator(GetLineaCarritoRepository getLineaCarritoRepository, GetCarritoRepository getCarritoRepository, UpdateCarritoRepository updateCarritoRepository) {
        this.getLineaCarritoRepository = getLineaCarritoRepository;
        this.getCarritoRepository = getCarritoRepository;
        this.updateCarritoRepository = updateCarritoRepository;
    }

    public Mono<Void> recalculate(int carritoId) {
        return getLineaCarritoRepository.findByCarritoId(carritoId)
                .map(LineaCarrito::getSubtotal)
                .reduce(0.0, Double::sum)
                .flatMap(total -> getCarritoRepository.findById(carritoId)
                        .flatMap(carrito -> {
                            carrito.setTotal(total);
                            return updateCarritoRepository.save(carrito);
                        }))
                .then();
    }
}