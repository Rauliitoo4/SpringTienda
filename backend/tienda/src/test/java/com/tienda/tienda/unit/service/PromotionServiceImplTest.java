package com.tienda.tienda.unit.service;

import com.tienda.tienda.promotion.dto.PromotionDTO;
import com.tienda.tienda.promotion.dto.mapper.PromotionMapper;
import com.tienda.tienda.promotion.model.Promotion;
import com.tienda.tienda.product.repository.port.ProductPromotionRepositoryPort;
import com.tienda.tienda.promotion.repository.port.PromotionRepositoryPort;
import com.tienda.tienda.promotion.service.PromotionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceImplTest {
    
    @Mock
    private PromotionRepositoryPort promotionRepo;

    @Mock
    private ProductPromotionRepositoryPort productPromotionRepo;

    @Mock
    private PromotionMapper promotionMapper;

    @InjectMocks
    private PromotionServiceImpl promotionServiceImpl;

    private Promotion testingPromo() {
        Promotion promo = new Promotion();
        promo.setId(1);
        promo.setDescription("Rebajas verano");
        promo.setDiscount(20.0);
        return promo;
    }

    private PromotionDTO testingDto() {
        PromotionDTO dto = new PromotionDTO();
        dto.setId(1);
        dto.setDescription("Rebajas verano");
        dto.setDiscount(20.0);
        return dto;
    }

    @Test
    void getPromotionById_shouldReturn_Promotion() {
        when(promotionRepo.findById(1)).thenReturn(Mono.just(testingPromo()));
        when(promotionMapper.toDTO(any(Promotion.class))).thenReturn(testingDto());

        StepVerifier.create(promotionServiceImpl.getPromotionById(1))
                .expectNextMatches(dto ->
                        dto.getDescription().equals("Rebajas verano") &&
                        dto.getDiscount() == 20.0)
                .verifyComplete();

    }

    @Test
    void getPromotionById_ifNotExists_shouldReturnNull() {
        when(promotionRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(promotionServiceImpl.getPromotionById(999))
                .verifyComplete();
    }

    @Test
    void createPromotion_shouldSaveAndReturnDTO() {
        Promotion promo = testingPromo();
        when(promotionMapper.toEntity(any(PromotionDTO.class))).thenReturn(promo);
        when(promotionRepo.save(any(Promotion.class))).thenReturn(Mono.just(promo));
        when(promotionMapper.toDTO(any(Promotion.class))).thenReturn(testingDto());

        PromotionDTO dto = new PromotionDTO();
        dto.setDescription("Rebajas verano");
        dto.setDiscount(20.0);

        StepVerifier.create(promotionServiceImpl.createPromotion(dto))
                .expectNextMatches(result ->
                        result.getDescription().equals("Rebajas verano") &&
                        result.getDiscount() == 20.0)
                .verifyComplete();
        verify(promotionRepo, times(1)).save(any(Promotion.class));
    }

    @Test
    void deletePromotion_shouldReturnTrue() {
        when(promotionRepo.existsById(1)).thenReturn(Mono.just(true));
        when(productPromotionRepo.deleteByPromotionId(1)).thenReturn(Mono.empty());
        when(promotionRepo.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(promotionServiceImpl.deletePromotion(1))
                .expectNext(true)
                .verifyComplete();
        verify(promotionRepo, times(1)).deleteById(1);
    }

    @Test
    void deletePromotion_ifNotExists_shouldReturnFalse() {
        when(promotionRepo.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(promotionServiceImpl.deletePromotion(999))
                .expectNext(false)
                .verifyComplete();
        verify(promotionRepo, never()).deleteById(anyInt());
    }

    @Test
    void updatePromotion_shouldUpdate_Promotion() {
        Promotion promo = testingPromo();
        PromotionDTO updatedDto = new PromotionDTO();
        updatedDto.setDiscount(30.0);
        when(promotionRepo.findById(1)).thenReturn(Mono.just(promo));
        when(promotionRepo.save(any(Promotion.class))).thenReturn(Mono.just(promo));
        when(promotionMapper.toDTO(any(Promotion.class))).thenReturn(updatedDto);

        PromotionDTO dto = new PromotionDTO();
        dto.setDiscount(30.0);

        StepVerifier.create(promotionServiceImpl.updatePromotion(1, dto))
                .expectNextMatches(result -> result.getDiscount() == 30.0)
                .verifyComplete();
    }
}
