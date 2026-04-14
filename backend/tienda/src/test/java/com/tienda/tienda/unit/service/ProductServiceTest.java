package com.tienda.tienda.unit.service;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.*;
import com.tienda.tienda.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepo;
    @Mock private PromotionRepository promotionRepo;
    @Mock private CarritoRepository carritoRepo;
    @Mock private LineaCarritoRepository lineaCarritoRepo;
    @Mock private ProductoPromocionRepository productoPromocionRepo;

    @InjectMocks
    private ProductService productService;

    private Product productoDePrueba() {
        Product producto = new Product();
        producto.setId(1);
        producto.setNombre("Camiseta");
        producto.setPrecio(20.0);
        producto.setPrecioFinal(20.0);
        producto.setPromociones(List.of());
        return producto;
    }

    private void mockCargarPromociones(int productId) {
        when(productoPromocionRepo.findPromotionIdsByProductId(productId))
                .thenReturn(Flux.empty());
    }

    @Test
    void obtenerProductoPorId_deberiaDevolver_elProducto() {
        Product producto = productoDePrueba();
        when(productRepo.findById(1)).thenReturn(Mono.just(producto));
        mockCargarPromociones(1);

        StepVerifier.create(productService.getProductById(1))
                .expectNextMatches(dto ->
                        dto.getNombre().equals("Camiseta") &&
                        dto.getPrecio() == 20.0)
                .verifyComplete();
    }

    @Test
    void obtenerProductoPorId_siNoExiste_deberiaDevolverNull() {
        when(productRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(productService.getProductById(999))
                .verifyComplete();
    }

    @Test
    void crearProducto_deberiaGuardaryDevolverDTO() {
        Product productoGuardado = productoDePrueba();
        when(productRepo.save(any(Product.class))).thenReturn(Mono.just(productoGuardado));
        mockCargarPromociones(1);

        ProductDTO dto = new ProductDTO();
        dto.setNombre("Camiseta");
        dto.setPrecio(20.0);

        StepVerifier.create(productService.createProduct(dto))
                .expectNextMatches(resultado -> resultado.getNombre().equals("Camiseta"))
                .verifyComplete();
        verify(productRepo, times(1)).save(any(Product.class));
    }

    @Test
    void eliminarProducto_siExiste_deberiaDevolverTrue() {
        when(productRepo.existsById(1)).thenReturn(Mono.just(true));
        when(productRepo.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(productService.deleteProduct(1))
                .expectNext(true)
                .verifyComplete();
        verify(productRepo, times(1)).deleteById(1);
    }

    @Test
    void eliminarProducto_siNoExiste_deberiaDevolverFalse() {
        when(productRepo.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(productService.deleteProduct(999))
                .expectNext(false)
                .verifyComplete();
        verify(productRepo, never()).deleteById(anyInt());
    }

    @Test
    void actualizarProducto_deberiaModificar_elProducto() {
        Product producto = productoDePrueba();
        when(productRepo.findById(1)).thenReturn(Mono.just(producto));
        mockCargarPromociones(1);
        when(productRepo.save(any(Product.class))).thenReturn(Mono.just(producto));

        ProductDTO dto = new ProductDTO();
        dto.setPrecio(200.0);

        StepVerifier.create(productService.updateProduct(1, dto))
                .expectNextMatches(resultado -> resultado.getPrecio() == 200.00)
                .verifyComplete();
    }
}

