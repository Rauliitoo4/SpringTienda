package com.tienda.tienda.carrito.application.usecase;

import com.tienda.tienda.carrito.domain.repository.GetLineaCarritoRepository;
import com.tienda.tienda.carrito.domain.repository.DeleteLineaCarritoRepository;
import com.tienda.tienda.carrito.application.helper.TotalCalculator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteLineaCarritoUseCase {

    private final GetLineaCarritoRepository getLineaCarritoRepository;
    private final DeleteLineaCarritoRepository deleteLineaCarritoRepository;
    private final TotalCalculator totalCalculator;

    public DeleteLineaCarritoUseCase(GetLineaCarritoRepository getLineaCarritoRepository, DeleteLineaCarritoRepository deleteLineaCarritoRepository, TotalCalculator totalCalculator) {
        this.getLineaCarritoRepository = getLineaCarritoRepository;
        this.deleteLineaCarritoRepository = deleteLineaCarritoRepository;
        this.totalCalculator = totalCalculator;
    }

    public Mono<Boolean> execute(int id) {
        return getLineaCarritoRepository.findById(id)
                .flatMap(linea -> {
                    Integer carritoId = linea.getCarritoId();
                    return deleteLineaCarritoRepository.deleteById(id)
                            .then(totalCalculator.recalculate(carritoId))
                            .thenReturn(true);
                })
                .defaultIfEmpty(false);
    }
}