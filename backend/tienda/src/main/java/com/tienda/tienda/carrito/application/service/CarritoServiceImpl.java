package com.tienda.tienda.carrito.application.service;

import com.tienda.tienda.carrito.application.dto.CarritoDTO;
import com.tienda.tienda.carrito.application.dto.mapper.CarritoMapper;
import com.tienda.tienda.carrito.domain.Carrito;
import com.tienda.tienda.lineacarrito.domain.LineaCarrito;
import com.tienda.tienda.product.domain.Product;
import com.tienda.tienda.carrito.application.port.CarritoRepositoryPort;
import com.tienda.tienda.lineacarrito.application.port.LineaCarritoRepositoryPort;
import com.tienda.tienda.product.application.port.ProductRepositoryPort;
import com.tienda.tienda.carrito.application.service.helper.LineaLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CarritoServiceImpl implements CarritoService{
    
    private final CarritoRepositoryPort carritoRepo;
    private final ProductRepositoryPort productRepo;
    private final LineaCarritoRepositoryPort lineaRepo;
    private final CarritoMapper carritoMapper;
    private final LineaLoader lineaLoader;

    public CarritoServiceImpl(ProductRepositoryPort productRepo, CarritoRepositoryPort carritoRepo, LineaCarritoRepositoryPort lineaRepo, CarritoMapper carritoMapper, LineaLoader lineaLoader) {
        this.productRepo = productRepo;
        this.carritoRepo = carritoRepo;
        this.lineaRepo = lineaRepo;
        this.carritoMapper = carritoMapper;
        this.lineaLoader = lineaLoader;
    }

    public Mono<CarritoDTO> getCarritoById (int id){
        return carritoRepo.findById(id)
                    .flatMap(lineaLoader::loadLineas)
                    .map(carritoMapper::toDTO);
    }

    public Mono<CarritoDTO> addProductToCarrito (int carritoID, int productID, int quantity) {
        return carritoRepo.findById(carritoID)
                .zipWith(productRepo.findById(productID))
                .flatMap(tuple -> {
                    Carrito carrito = tuple.getT1();
                    Product product = tuple.getT2();

                    LineaCarrito linea = new LineaCarrito();
                    linea.setQuantity(quantity);
                    linea.setProductId(product.getId());
                    linea.setCarritoId(carrito.getId());
                    linea.setSubtotal(product.getFinalPrice() * quantity);
                    linea.setProduct(product);

                    return lineaRepo.save(linea)
                            .then(recalculateTotal(carritoID))
                            .then(carritoRepo.findById(carritoID))
                            .flatMap(lineaLoader::loadLineas)
                            .map(carritoMapper::toDTO);
                });
    }

    public Mono<Carrito> createCarrito() {
        Carrito carrito = new Carrito();
        carrito.setTotal(0.0);
        return carritoRepo.save(carrito);
    }

    public Mono<Double> calculateTotal(int carritoID){
        return lineaRepo.findByCarritoId(carritoID)
                .map(LineaCarrito::getSubtotal)
                .reduce(0.0, Double::sum);
    }

    public Mono<Void> recalculateTotal(int carritoID) {
        return calculateTotal(carritoID)
                .flatMap(total -> carritoRepo.findById(carritoID)
                        .flatMap(carrito -> {
                            carrito.setTotal(total);
                            return carritoRepo.save(carrito);
                        }))
                .then();
    }
}
