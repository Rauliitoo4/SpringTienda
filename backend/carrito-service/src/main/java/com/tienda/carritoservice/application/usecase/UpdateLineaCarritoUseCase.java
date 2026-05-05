package com.tienda.carritoservice.application.usecase;

import com.tienda.carritoservice.application.port.input.UpdateLineaCarritoInputPort;
import com.tienda.carritoservice.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.carritoservice.application.port.output.UpdateLineaCarritoOutputPort;
import com.tienda.carritoservice.application.service.TotalCalculator;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateLineaCarritoUseCase implements UpdateLineaCarritoInputPort {

    private final GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    private final UpdateLineaCarritoOutputPort updateLineaCarritoOutputPort;
    private final TotalCalculator totalCalculator;

    public UpdateLineaCarritoUseCase(GetLineaCarritoOutputPort getLineaCarritoOutputPort,
                                     UpdateLineaCarritoOutputPort updateLineaCarritoOutputPort,
                                     TotalCalculator totalCalculator) {
        this.getLineaCarritoOutputPort = getLineaCarritoOutputPort;
        this.updateLineaCarritoOutputPort = updateLineaCarritoOutputPort;
        this.totalCalculator = totalCalculator;
    }

    public Mono<LineaCarrito> execute(int id, int quantity) {
        return getLineaCarritoOutputPort.findById(id)
                .flatMap(linea -> {
                    if (quantity <= 0) return Mono.empty();
                    linea.setQuantity(quantity);
                    return updateLineaCarritoOutputPort.save(linea)
                            .then(totalCalculator.recalculate(linea.getCarritoId()))
                            .then(getLineaCarritoOutputPort.findById(id));
                });
    }
}