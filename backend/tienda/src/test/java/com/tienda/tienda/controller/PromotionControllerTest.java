package com.tienda.tienda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.service.PromotionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PromotionController.class)
class PromotionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PromotionService promotionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private PromotionDTO promo;

    @BeforeEach
    void setUp() {
        promo = new PromotionDTO();
        promo.setId(1);
        promo.setDescripcion("Rebajas de verano");
        promo.setDescuento(10.0);
    }

    //GET /promociones
    @Test
    void getAllPromotions_deberiaRetornarListaYStatus200() throws Exception {
        when(promotionService.getAllPromotions()).thenReturn(List.of(promo));

        mockMvc.perform(get("/promociones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].descripcion").value("Rebajas de verano"))
                    .andExpect(jsonPath("$[0].descuento").value(10.0));

    }

    @Test
    void getAllPromotions_listaVacia_deberiaRetornarArrayVacioYStatus200() throws Exception {
        when(promotionService.getAllPromotions()).thenReturn(List.of());

        mockMvc.perform(get("/promociones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
    }

    //GET /promociones/{id}
    @Test
    void getPromotionById_existente_deberiaRetornarPromocionyStatus200() throws Exception {
        when(promotionService.getPromotionById(1)).thenReturn(promo);

        mockMvc.perform(get("/promociones/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.descripcion").value("Rebajas de verano"))
                    .andExpect(jsonPath("$.descuento").value(10.0));
        }

    @Test
    void getPromotionById_noExistente_deberiaRetornarStatus200conNull() throws Exception {
        when(promotionService.getPromotionById(99)).thenReturn(null);

        mockMvc.perform(get("/promociones/99"))
                    .andExpect(status().isOk());     
    }

    //POST /promociones
    @Test
    void createPromotion_deberiaRetornarPromocionCreadaYStatus200() throws Exception {
        when(promotionService.createPromotion(any(PromotionDTO.class))).thenReturn(promo);
        
        mockMvc.perform(post("/promociones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promo)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.descripcion").value("Rebajas de verano"))
                    .andExpect(jsonPath("$.descuento").value(10.0));
    }

    @Test
    void createPromotion_deberiaDelegarEnServicio() throws Exception {
        when(promotionService.createPromotion(any(PromotionDTO.class))).thenReturn(promo);
        
        mockMvc.perform(post("/promociones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promo)))
                    .andExpect(status().isOk());
        verify(promotionService, times(1)).createPromotion(any(PromotionDTO.class));
    }

    //PUT /promociones/{id}
    @Test
    void updatePromotion_existente_deberiaRetornarPromocionActualizadaYStatus200() throws Exception {
        promo.setDescuento(50.0);
        when(promotionService.updatePromotion(eq(1), any(PromotionDTO.class))).thenReturn(promo);

        mockMvc.perform(put("/promociones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promo)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.descuento").value(50.0));
    }

    @Test
    void updatePromotion_noExistente_deberiaRetornarStatus200conNull() throws Exception {
        when(promotionService.updatePromotion(eq(99), any(PromotionDTO.class))).thenReturn(null);

        mockMvc.perform(put("/promotion/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promo)))
                    .andExpect(status().isNotFound());
    }

    //DELETE /promociones/{id}
    @Test
    void deletePromotion_existente_deberiaRetornarTrueYStatus200() throws Exception {
        when(promotionService.deletePromotion(1)).thenReturn(true);

        mockMvc.perform(delete("/promociones/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
    }

    @Test
    void deletePromotion_noExistente_deberiaRetornarFalseYStatus200() throws Exception {
        when(promotionService.deletePromotion(99)).thenReturn(false);

        mockMvc.perform(delete("/promociones/99"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"));
    }
}
