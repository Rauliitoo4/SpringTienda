package com.tienda.tienda.unit.service;

import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.mapper.ProductMapper;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.repository.*;
import com.tienda.tienda.service.LineaCarritoService;
import com.tienda.tienda.service.ProductService;
import com.tienda.tienda.service.helper.PrecioCalculator;
import com.tienda.tienda.service.helper.PromotionLoader;
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
    @Mock private ProductoPromocionRepository productoPromocionRepo;
    @Mock private ProductMapper productMapper;
    @Mock private PromotionLoader promotionLoader;
    @Mock private LineaCarritoService lineaCarritoService;
    @Mock private PrecioCalculator precioCalculator;

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

    private ProductDTO dtoDePrueba() {
        ProductDTO dto = new ProductDTO();
        dto.setId(1);
        dto.setNombre("Camiseta");
        dto.setPrecio(20.0);
        dto.setPrecioFinal(20.0);
        return dto;
    }

    private void mockCargarPromociones() {
        when(promotionLoader.cargarPromociones(any(Product.class)))
                .thenReturn(Mono.just(productoDePrueba()));
    }

    @Test
    void obtenerProductoPorId_deberiaDevolver_elProducto() {
        when(productRepo.findById(1)).thenReturn(Mono.just(productoDePrueba()));
        mockCargarPromociones();
        when(productMapper.toDTO(any(Product.class))).thenReturn(dtoDePrueba());

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
        when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(productoGuardado);
        when(productRepo.save(any(Product.class))).thenReturn(Mono.just(productoGuardado));
        mockCargarPromociones();
        when(productMapper.toDTO(any(Product.class))).thenReturn(dtoDePrueba());

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
        mockCargarPromociones();
        when(productRepo.save(any(Product.class))).thenReturn(Mono.just(producto));

        ProductDTO dtoActualizado = new ProductDTO();
        dtoActualizado.setId(1);
        dtoActualizado.setNombre("Camiseta");
        dtoActualizado.setPrecio(200.0);
        dtoActualizado.setPrecioFinal(200.0);
        when(productMapper.toDTO(any(Product.class))).thenReturn(dtoActualizado);

        ProductDTO dto = new ProductDTO();
        dto.setPrecio(200.0);

        StepVerifier.create(productService.updateProduct(1, dto))
                .expectNextMatches(resultado -> resultado.getPrecio() == 200.00)
                .verifyComplete();
    }
}

