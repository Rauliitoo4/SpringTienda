package com.tienda.tienda.carrito.application.usecase;

import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.carrito.domain.model.LineaCarrito;
import com.tienda.tienda.carrito.domain.repository.GetCarritoRepository;
import com.tienda.tienda.carrito.domain.repository.CreateLineaCarritoRepository;
import com.tienda.tienda.carrito.application.helper.LineaLoader;
import com.tienda.tienda.carrito.application.helper.TotalCalculator;
import com.tienda.tienda.product.domain.repository.GetProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddProductToCarritoUseCase {

    private final GetCarritoRepository getCarritoRepository;
    private final CreateLineaCarritoRepository createLineaCarritoRepository;
    private final GetProductRepository getProductRepository;
    private final LineaLoader lineaLoader;
    private final TotalCalculator totalCalculator;

    public AddProductToCarritoUseCase(GetCarritoRepository getCarritoRepository, CreateLineaCarritoRepository createLineaCarritoRepository, GetProductRepository getProductRepository, LineaLoader lineaLoader, TotalCalculator totalCalculator) {
        this.getCarritoRepository = getCarritoRepository;
        this.createLineaCarritoRepository = createLineaCarritoRepository;
        this.getProductRepository = getProductRepository;
        this.lineaLoader = lineaLoader;
        this.totalCalculator = totalCalculator;
    }

    public Mono<Carrito> execute(int carritoId, int productId, int quantity) {
        return getCarritoRepository.findById(carritoId)
                .zipWith(getProductRepository.findById(productId))
                .flatMap(tuple -> {
                    Carrito carrito = tuple.getT1();
                    LineaCarrito linea = new LineaCarrito();
                    linea.setQuantity(quantity);
                    linea.setProductId(tuple.getT2().getId());
                    linea.setCarritoId(carrito.getId());
                    linea.setSubtotal(tuple.getT2().getFinalPrice() * quantity);

                    return createLineaCarritoRepository.save(linea)
                            .then(totalCalculator.recalculate(carritoId))
                            .then(getCarritoRepository.findById(carritoId))
                            .flatMap(lineaLoader::loadLineas);
                });
    }
}