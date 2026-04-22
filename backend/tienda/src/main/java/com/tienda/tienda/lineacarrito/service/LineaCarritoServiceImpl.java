package com.tienda.tienda.lineacarrito.service;

import com.tienda.tienda.carrito.service.CarritoService;
import com.tienda.tienda.lineacarrito.dto.LineaCarritoDTO;
import com.tienda.tienda.lineacarrito.dto.mapper.LineaCarritoMapper;
import com.tienda.tienda.lineacarrito.repository.port.LineaCarritoRepositoryPort;
import com.tienda.tienda.product.model.Product;
import com.tienda.tienda.lineacarrito.service.helper.ProductLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LineaCarritoServiceImpl implements LineaCarritoService{

    private final LineaCarritoRepositoryPort lineaRepo;
    private final LineaCarritoMapper lineaCarritoMapper;
    private final ProductLoader productLoader;
    private final CarritoService carritoService;

    public LineaCarritoServiceImpl(LineaCarritoRepositoryPort lineaRepo, LineaCarritoMapper lineaCarritoMapper, ProductLoader productLoader, CarritoService carritoService){
        this.lineaRepo = lineaRepo;
        this.lineaCarritoMapper = lineaCarritoMapper;
        this.productLoader = productLoader;
        this.carritoService = carritoService;
    }

    public Mono<LineaCarritoDTO> updateLinea(int id, int quantity) {
        return lineaRepo.findById(id)
                .flatMap(linea -> {
                    if (quantity <= 0) return Mono.empty();
                    linea.setQuantity(quantity);
                    return productLoader.loadProduct(linea)
                            .flatMap(lineaWithProduct -> {
                                lineaWithProduct.setSubtotal(lineaWithProduct.getProduct().getFinalPrice() * quantity);
                                return lineaRepo.save(linea)
                                        .then(carritoService.recalculateTotal(linea.getCarritoId()))
                                        .then(productLoader.loadProduct(linea));
                            });
                })
                .map(lineaCarritoMapper::toDTO);
    }

    public Mono<Boolean> deleteLinea (int id) {
        return lineaRepo.findById(id)
                .flatMap(linea -> {
                    Integer carritoId = linea.getCarritoId();
                    return lineaRepo.deleteById(id)
                            .then(carritoService.recalculateTotal(carritoId))
                            .thenReturn(true);
                })
                .defaultIfEmpty(false);
    }

    public Mono<LineaCarritoDTO> getLineaById (int id){
        return lineaRepo.findById(id)
                .flatMap(productLoader::loadProduct)
                .map(lineaCarritoMapper::toDTO);
    }

    public Flux<LineaCarritoDTO> getAllLineas() {
        return lineaRepo.findAll()
                .flatMap(productLoader::loadProduct)
                .map(lineaCarritoMapper::toDTO);
    }

    public Mono<Void> updateLineasCarrito(Product product) {
        return lineaRepo.findAll()
                .filter(linea -> linea.getProductId().equals(product.getId()))
                .flatMap(linea -> {
                    linea.setSubtotal(product.getFinalPrice() * linea.getQuantity());
                    return lineaRepo.save(linea);
                })
                .collectList()
                .flatMap(updatedLineas -> {
                    if (updatedLineas.isEmpty()) return Mono.empty();
                    Integer carritoId = updatedLineas.get(0).getCarritoId();
                    return carritoService.recalculateTotal(carritoId);
                })
                .then();
    }
}
