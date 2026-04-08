package com.tienda.tienda.unit;

import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.Promotion;
import com.tienda.tienda.repository.PromotionRepository;
import com.tienda.tienda.repository.ProductRepository;
import com.tienda.tienda.service.PromotionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private ProductRepository productRepo;

    @InjectMocks
    private PromotionService promotionService;

    @Test
    void obtenerPromocionPorId_deberiaDevolver_laPromocion() {
        //Given
        Promotion promo = new Promotion();
        promo.setId(1);
        promo.setDescripcion("Rebajas verano");
        promo.setDescuento(20.0);
        when(promotionRepo.findById(1)).thenReturn(Optional.of(promo));

        //When
        PromotionDTO resultado = promotionService.getPromotionById(1);
        
        //Then 
        assertNotNull(resultado);
        assertEquals("Rebajas verano", resultado.getDescripcion());
        assertEquals(20.0, resultado.getDescuento());
    }

    @Test
    void obtenerPromocionPorId_siNoExiste_deberiaDevolverNull() {
        //Given
        when(promotionRepo.findById(999)).thenReturn(Optional.empty());

        //When
        PromotionDTO resultado = promotionService.getPromotionById(999);

        //Then
        assertNull(resultado);
    }

    @Test
    void crearPromocion_deberiaGuardaryDevolverDTO() {
        //Given
        PromotionDTO dto = new PromotionDTO();
        dto.setDescripcion("Black friday");
        dto.setDescuento(50.0);

        Promotion promo = new Promotion();
        promo.setId(1);
        promo.setDescripcion("Black friday");
        promo.setDescuento(50.0);
        when(promotionRepo.save(any(Promotion.class))).thenReturn(promo);

        //When
        PromotionDTO resultado = promotionService.createPromotion(dto);

        //Then
        assertNotNull(resultado);
        assertEquals("Black friday", resultado.getDescripcion());
        assertEquals(50.0, resultado.getDescuento());
        verify(promotionRepo, times(1)).save(any(Promotion.class));
    }

    @Test
    void eliminarPromocion_siExiste_deberiaDevolverTrue() {
        //Given
        Promotion promo = new Promotion();
        promo.setId(1);
        promo.setDescuento(20.0);

        Product producto = new Product();
        producto.setId(1);
        producto.setPrecio(100.0);
        producto.setPromociones(new ArrayList<>(List.of(promo)));
        
        when(promotionRepo.findById(1)).thenReturn(Optional.of(promo));
        when(productRepo.findAll()).thenReturn(List.of(producto));

        //When
        boolean resultado = promotionService.deletePromotion(1);

        //Then
        assertTrue(resultado);
        verify(promotionRepo, times(1)).deleteById(1);
    }

    @Test
    void eliminarPromocion_siNoExiste_deberiaDevolverFalse() {
        //Given
        when(promotionRepo.findById(999)).thenReturn(Optional.empty());

        //When
        boolean resultado = promotionService.deletePromotion(999);

        //Then
        assertFalse(resultado);
        verify(promotionRepo, never()).deleteById(999);
    }

    @Test
    void actualizarPromocion_deberiaModificar_laPromocion() {
        //Given 
        Promotion promo = new Promotion();
        promo.setId(1);
        promo.setDescripcion("Rebajas");
        promo.setDescuento(10.0);
        when(promotionRepo.findById(1)).thenReturn(Optional.of(promo));
        when(promotionRepo.save(any(Promotion.class))).thenReturn(promo);

        PromotionDTO dto = new PromotionDTO();
        dto.setDescuento(30.0);

        //When
        PromotionDTO resultado = promotionService.updatePromotion(1, dto);

        //Then
        assertNotNull(resultado);
        assertEquals(30.0, resultado.getDescuento());
    }
}
