package com.tienda.carritoservice.application.usecase;

import com.tienda.tienda.carrito.application.port.input.DeleteLineaCarritoInputPort;
import com.tienda.tienda.carrito.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.tienda.carrito.application.port.output.DeleteLineaCarritoOutputPort;
import com.tienda.tienda.carrito.application.service.TotalCalculator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteLineaCarritoUseCase implements DeleteLineaCarritoInputPort {

    private final GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    private final DeleteLineaCarritoOutputPort deleteLineaCarritoOutputPort;
    private final TotalCalculator totalCalculator;

    public DeleteLineaCarritoUseCase(GetLineaCarritoOutputPort getLineaCarritoOutputPort, DeleteLineaCarritoOutputPort deleteLineaCarritoOutputPort, TotalCalculator totalCalculator) {
        this.getLineaCarritoOutputPort = getLineaCarritoOutputPort;
        this.deleteLineaCarritoOutputPort = deleteLineaCarritoOutputPort;
        this.totalCalculator = totalCalculator;
    }

    public Mono<Boolean> execute(int id) {
        return getLineaCarritoOutputPort.findById(id)
                .flatMap(linea -> {
                    Integer carritoId = linea.getCarritoId();
                    return deleteLineaCarritoOutputPort.deleteById(id)
                            .then(totalCalculator.recalculate(carritoId))
                            .thenReturn(true);
                })
                .defaultIfEmpty(false);
    }
}