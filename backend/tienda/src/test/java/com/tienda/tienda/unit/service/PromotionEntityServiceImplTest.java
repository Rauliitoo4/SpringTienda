package com.tienda.tienda.unit.service;

import com.tienda.tienda.promotion.application.dto.PromotionResponse;
import com.tienda.tienda.promotion.application.dto.mapper.PromotionMapper;
import com.tienda.tienda.promotion.infrastructure.adapter.output.persistence.entity.PromotionEntity;
import com.tienda.tienda.product.domain.repository.ProductPromotionRepository;
import com.tienda.tienda.promotion.application.port.PromotionRepositoryPort;
import com.tienda.tienda.promotion.application.service.PromotionServiceImpl;
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
public class PromotionEntityServiceImplTest {
    
    @Mock
    private PromotionRepositoryPort promotionRepo;

    @Mock
    private ProductPromotionRepository productPromotionRepo;

    @Mock
    private PromotionMapper promotionMapper;

    @InjectMocks
    private PromotionServiceImpl promotionServiceImpl;

    private PromotionEntity testingPromo() {
        PromotionEntity promo = new PromotionEntity();
        promo.setId(1);
        promo.setDescription("Rebajas verano");
        promo.setDiscount(20.0);
        return promo;
    }

    private PromotionResponse testingDto() {
        PromotionResponse dto = new PromotionResponse();
        dto.setId(1);
        dto.setDescription("Rebajas verano");
        dto.setDiscount(20.0);
        return dto;
    }

    @Test
    void getPromotionById_shouldReturn_Promotion() {
        when(promotionRepo.findById(1)).thenReturn(Mono.just(testingPromo()));
        when(promotionMapper.toDTO(any(PromotionEntity.class))).thenReturn(testingDto());

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
        PromotionEntity promo = testingPromo();
        when(promotionMapper.toEntity(any(PromotionResponse.class))).thenReturn(promo);
        when(promotionRepo.save(any(PromotionEntity.class))).thenReturn(Mono.just(promo));
        when(promotionMapper.toDTO(any(PromotionEntity.class))).thenReturn(testingDto());

        PromotionResponse dto = new PromotionResponse();
        dto.setDescription("Rebajas verano");
        dto.setDiscount(20.0);

        StepVerifier.create(promotionServiceImpl.createPromotion(dto))
                .expectNextMatches(result ->
                        result.getDescription().equals("Rebajas verano") &&
                        result.getDiscount() == 20.0)
                .verifyComplete();
        verify(promotionRepo, times(1)).save(any(PromotionEntity.class));
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
        PromotionEntity promo = testingPromo();
        PromotionResponse updatedDto = new PromotionResponse();
        updatedDto.setDiscount(30.0);
        when(promotionRepo.findById(1)).thenReturn(Mono.just(promo));
        when(promotionRepo.save(any(PromotionEntity.class))).thenReturn(Mono.just(promo));
        when(promotionMapper.toDTO(any(PromotionEntity.class))).thenReturn(updatedDto);

        PromotionResponse dto = new PromotionResponse();
        dto.setDiscount(30.0);

        StepVerifier.create(promotionServiceImpl.updatePromotion(1, dto))
                .expectNextMatches(result -> result.getDiscount() == 30.0)
                .verifyComplete();
    }
}
