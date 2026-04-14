package com.tienda.tienda.unit.service;

import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.Promotion;
import com.tienda.tienda.repository.ProductoPromocionRepository;
import com.tienda.tienda.repository.PromotionRepository;
import com.tienda.tienda.repository.ProductRepository;
import com.tienda.tienda.service.PromotionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {
    
    @Mock
    private PromotionRepository promotionRepo;

    @Mock
    private ProductoPromocionRepository productoPromocionRepo;

    @InjectMocks
    private PromotionService promotionService;

    private Promotion promoDePrueba() {
        Promotion promo = new Promotion();
        promo.setId(1);
        promo.setDescripcion("Rebajas verano");
        promo.setDescuento(20.0);
        return promo;
    }

    @Test
    void obtenerPromocionPorId_deberiaDevolver_laPromocion() {
        when(promotionRepo.findById(1)).thenReturn(Mono.just(promoDePrueba()));

        StepVerifier.create(promotionService.getPromotionById(1))
                .expectNextMatches(dto ->
                        dto.getDescripcion().equals("Rebajas verano") &&
                        dto.getDescuento() == 20.0)
                .verifyComplete();

    }

    @Test
    void obtenerPromocionPorId_siNoExiste_deberiaDevolverNull() {
        when(promotionRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(promotionService.getPromotionById(999))
                .verifyComplete();
    }

    @Test
    void crearPromocion_deberiaGuardaryDevolverDTO() {
        Promotion promo = promoDePrueba();
        when(promotionRepo.save(any(Promotion.class))).thenReturn(Mono.just(promo));

        PromotionDTO dto = new PromotionDTO();
        dto.setDescripcion("Rebajas verano");
        dto.setDescuento(20.0);

        StepVerifier.create(promotionService.createPromotion(dto))
                .expectNextMatches(resultado ->
                        resultado.getDescripcion().equals("Rebajas verano") &&
                        resultado.getDescuento() == 20.0)
                .verifyComplete();
        verify(promotionRepo, times(1)).save(any(Promotion.class));
    }

    @Test
    void eliminarPromocion_siExiste_deberiaDevolverTrue() {
        when(promotionRepo.existsById(1)).thenReturn(Mono.just(true));
        when(productoPromocionRepo.deleteByPromotionId(1)).thenReturn(Mono.empty());
        when(promotionRepo.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(promotionService.deletePromotion(1))
                .expectNext(true)
                .verifyComplete();
        verify(promotionRepo, times(1)).deleteById(1);
    }

    @Test
    void eliminarPromocion_siNoExiste_deberiaDevolverFalse() {
        when(promotionRepo.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(promotionService.deletePromotion(999))
                .expectNext(false)
                .verifyComplete();
        verify(promotionRepo, never()).deleteById(anyInt());
    }

    @Test
    void actualizarPromocion_deberiaModificar_laPromocion() {
        Promotion promo = promoDePrueba();
        when(promotionRepo.findById(1)).thenReturn(Mono.just(promo));
        when(promotionRepo.save(any(Promotion.class))).thenReturn(Mono.just(promo));

        PromotionDTO dto = new PromotionDTO();
        dto.setDescuento(30.0);

        StepVerifier.create(promotionService.updatePromotion(1, dto))
                .expectNextMatches(resultado -> resultado.getDescuento() == 30.0)
                .verifyComplete();
    }
}
