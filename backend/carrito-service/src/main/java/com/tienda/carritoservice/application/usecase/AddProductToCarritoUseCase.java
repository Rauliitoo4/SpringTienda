package com.tienda.carritoservice.application.usecase;

import com.tienda.carritoservice.application.port.input.AddProductToCarritoInputPort;
import com.tienda.carritoservice.application.port.output.CreateLineaCarritoOutputPort;
import com.tienda.carritoservice.application.port.output.GetCarritoOutputPort;
import com.tienda.carritoservice.application.port.output.GetLineaCarritoOutputPort;
import com.tienda.carritoservice.application.port.output.UpdateLineaCarritoOutputPort;
import com.tienda.carritoservice.application.service.LineaLoader;
import com.tienda.carritoservice.application.service.ProductLoader;
import com.tienda.carritoservice.application.service.TotalCalculator;
import com.tienda.carritoservice.domain.model.Carrito;
import com.tienda.carritoservice.domain.model.LineaCarrito;
import com.tienda.carritoservice.domain.model.Size;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddProductToCarritoUseCase implements AddProductToCarritoInputPort {

    private final GetCarritoOutputPort getCarritoOutputPort;
    private final CreateLineaCarritoOutputPort createLineaCarritoOutputPort;
    private final GetLineaCarritoOutputPort getLineaCarritoOutputPort;
    private final UpdateLineaCarritoOutputPort updateLineaCarritoOutputPort;
    private final ProductLoader productLoader;
    private final LineaLoader lineaLoader;
    private final TotalCalculator totalCalculator;

    public AddProductToCarritoUseCase(GetCarritoOutputPort getCarritoOutputPort,
                                      CreateLineaCarritoOutputPort createLineaCarritoOutputPort,
                                      GetLineaCarritoOutputPort getLineaCarritoOutputPort,
                                      UpdateLineaCarritoOutputPort updateLineaCarritoOutputPort,
                                      ProductLoader productLoader,
                                      LineaLoader lineaLoader,
                                      TotalCalculator totalCalculator) {
        this.getCarritoOutputPort = getCarritoOutputPort;
        this.createLineaCarritoOutputPort = createLineaCarritoOutputPort;
        this.getLineaCarritoOutputPort = getLineaCarritoOutputPort;
        this.updateLineaCarritoOutputPort = updateLineaCarritoOutputPort;
        this.productLoader = productLoader;
        this.lineaLoader = lineaLoader;
        this.totalCalculator = totalCalculator;
    }

    public Mono<Carrito> execute(int carritoId, int productId, int quantity, Size size) {
        return getCarritoOutputPort.findById(carritoId)
                .zipWith(productLoader.loadProduct(productId))
                .flatMap(tuple -> {
                    Carrito carrito = tuple.getT1();
                    double finalPrice = tuple.getT2().getFinalPrice();

                    return getLineaCarritoOutputPort.findByCarritoIdAndProductIdAndSize(carritoId, productId, size)
                            .flatMap(lineaExistente -> {
                                lineaExistente.setQuantity(lineaExistente.getQuantity() + quantity);
                                lineaExistente.setSubtotal(finalPrice * lineaExistente.getQuantity());
                                return updateLineaCarritoOutputPort.save(lineaExistente);
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                LineaCarrito nuevaLinea = new LineaCarrito();
                                nuevaLinea.setQuantity(quantity);
                                nuevaLinea.setProductId(productId);
                                nuevaLinea.setCarritoId(carrito.getId());
                                nuevaLinea.setSubtotal(finalPrice * quantity);
                                nuevaLinea.setSize(size);
                                return createLineaCarritoOutputPort.save(nuevaLinea);
                            }))
                            .then(totalCalculator.recalculate(carritoId))
                            .then(getCarritoOutputPort.findById(carritoId))
                            .flatMap(lineaLoader::loadLineas);
                });
    }
}