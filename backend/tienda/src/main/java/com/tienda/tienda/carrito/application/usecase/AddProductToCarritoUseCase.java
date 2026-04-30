package com.tienda.tienda.carrito.application.usecase;

import com.tienda.tienda.carrito.application.port.input.AddProductToCarritoInputPort;
import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.application.port.output.GetCarritoOutputPort;
import com.tienda.tienda.carrito.application.port.output.CreateLineaCarritoOutputPort;
import com.tienda.tienda.carrito.application.service.LineaLoader;
import com.tienda.tienda.carrito.application.service.TotalCalculator;
import com.tienda.tienda.product.application.port.output.GetProductOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddProductToCarritoUseCase implements AddProductToCarritoInputPort {

    private final GetCarritoOutputPort getCarritoOutputPort;
    private final CreateLineaCarritoOutputPort createLineaCarritoOutputPort;
    private final GetProductOutputPort getProductOutputPort;
    private final LineaLoader lineaLoader;
    private final TotalCalculator totalCalculator;

    public AddProductToCarritoUseCase(GetCarritoOutputPort getCarritoOutputPort, CreateLineaCarritoOutputPort createLineaCarritoOutputPort, GetProductOutputPort getProductOutputPort, LineaLoader lineaLoader, TotalCalculator totalCalculator) {
        this.getCarritoOutputPort = getCarritoOutputPort;
        this.createLineaCarritoOutputPort = createLineaCarritoOutputPort;
        this.getProductOutputPort = getProductOutputPort;
        this.lineaLoader = lineaLoader;
        this.totalCalculator = totalCalculator;
    }

    public Mono<Carrito> execute(int carritoId, int productId, int quantity) {
        return getCarritoOutputPort.findById(carritoId)
                .zipWith(getProductOutputPort.findById(productId))
                .flatMap(tuple -> {
                    Carrito carrito = tuple.getT1();
                    LineaCarrito linea = new LineaCarrito();
                    linea.setQuantity(quantity);
                    linea.setProductId(tuple.getT2().getId());
                    linea.setCarritoId(carrito.getId());
                    linea.setSubtotal(tuple.getT2().getFinalPrice() * quantity);

                    return createLineaCarritoOutputPort.save(linea)
                            .then(totalCalculator.recalculate(carritoId))
                            .then(getCarritoOutputPort.findById(carritoId))
                            .flatMap(lineaLoader::loadLineas);
                });
    }
}