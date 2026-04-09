package com.tienda.tienda.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.dto.PromotionDTO;
import com.tienda.tienda.service.ProductService;
import com.tienda.tienda.controller.ProductController;

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

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProductDTO productoBase;
    private ProductDTO productoConPromo;

    @BeforeEach
    void setUp() {
        productoBase = new ProductDTO();
        productoBase.setId(1);
        productoBase.setNombre("Camiseta");
        productoBase.setPrecio(20.0);
        productoBase.setPrecioFinal(20.0);
        productoBase.setDescripcion("Camiseta de algodón");
        productoBase.setMaterial("Algodón");
        productoBase.setConsideraciones("Lavar a mano");
        productoBase.setImagenUrl("http://img.com/camiseta.jpg");
        productoBase.setPromociones(List.of());

        PromotionDTO promo = new PromotionDTO();
        promo.setId(1);
        promo.setDescripcion("10% de descuento");
        promo.setDescuento(10.0);

        productoConPromo = new ProductDTO();
        productoConPromo.setId(1);
        productoConPromo.setNombre("Camiseta");
        productoConPromo.setPrecio(20.0);
        productoConPromo.setPrecioFinal(18.0);
        productoConPromo.setPromociones(List.of(promo));
    }

    //GET /productos
    @Test
    void getAllProdcuts_deberiaRetornarListaYStatus200() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(productoBase));

        mockMvc.perform(get("/productos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].nombre").value("Camiseta"))
                    .andExpect(jsonPath("$[0].precio").value(20.0))
                    .andExpect(jsonPath("$[0].precioFinal").value(20.0));

    }

    @Test
    void getAllProducts_listVacia_deberiaRetornarArrayVacioYStatus200() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of());

        mockMvc.perform(get("/productos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
    }

    //GET /productos/{id}
    @Test
    void getProductById_existente_deberiaRetornarProductoyStatus200() throws Exception {
        when(productService.getProductById(1)).thenReturn(productoBase);

        mockMvc.perform(get("/productos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("Camiseta"))
                    .andExpect(jsonPath("$.precio").value(20.0));                 
    }

    @Test
    void getProductById_noExistente_deberiaRetornar404() throws Exception {
        when(productService.getProductById(99)).thenReturn(null);

        mockMvc.perform(get("/productos/99"))
                    .andExpect(status().isNotFound());     
    }

    //POST /productos
    @Test
    void createProduct_deberiaRetornarProductoCreadoYStatus201() throws Exception {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(productoBase);
        
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoBase)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("Camiseta"))
                    .andExpect(jsonPath("$.precio").value(20.0));
    }

    @Test
    void createProduct_fallido_deberiaRetornar400() throws Exception {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(null);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoBase)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createProduct_deberiaDelegarEnServicio() throws Exception {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(productoBase);
        
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoBase)))
                    .andExpect(status().isCreated());
        verify(productService, times(1)).createProduct(any(ProductDTO.class));
    }

    //PUT /productos/{id}
    @Test
    void updateProduct_existente_deberiaRetornarProductoActualizadoYStatus200() throws Exception {
        productoBase.setNombre("Camiseta Actualizada");
        when(productService.updateProduct(eq(1), any(ProductDTO.class))).thenReturn(productoBase);

        mockMvc.perform(put("/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoBase)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Camiseta Actualizada"));
    }

    @Test
    void updateProduct_noExistente_deberiaRetornar404() throws Exception {
        when(productService.updateProduct(eq(99), any(ProductDTO.class))).thenReturn(null);

        mockMvc.perform(put("/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoBase)))
                    .andExpect(status().isNotFound());
    }

    //DELETE /productos/{id}
    @Test
    void deleteProduct_existente_deberiaRetornar204() throws Exception {
        when(productService.deleteProduct(1)).thenReturn(true);

        mockMvc.perform(delete("/productos/1"))
                    .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_noExistente_deberiaRetornar404() throws Exception {
        when(productService.deleteProduct(99)).thenReturn(false);

        mockMvc.perform(delete("/productos/99"))
                    .andExpect(status().isNotFound());
    }

    //POST /productos/{productoID}/promociones/{promocionID}
    @Test
    void addPromotion_productoYPromoExistentes_deberiaRetornarProductoConPromoYStatus200() throws Exception {
        when(productService.addPromotion(1, 1)).thenReturn(productoConPromo);

        mockMvc.perform(post("/productos/1/promociones/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.precioFinal").value(18.0))
                    .andExpect(jsonPath("$.promociones.length()").value(1))
                    .andExpect(jsonPath("$.promociones[0].descuento").value(10.0));
    }

    @Test
    void addPromotion_productoNoExistente_deberiaRetornar404() throws Exception {
        when(productService.addPromotion(99, 1)).thenReturn(null);

        mockMvc.perform(post("/productos/1/promociones/1"))
                    .andExpect(status().isNotFound());
    }

    @Test
    void addPromotion_promoNoExistente_deberiaRetornar404() throws Exception {
        when(productService.addPromotion(1, 99)).thenReturn(null);

        mockMvc.perform(post("/productos/1/promociones/1"))
                    .andExpect(status().isNotFound());
    }

    //DELETE /productos/{productoID}/promociones/{promocionID}
    @Test
    void removePromotion_deberiaRetornarProductoSinPromoYStatus200() throws Exception {
        when(productService.removePromotion(1, 1)).thenReturn(productoBase);

        mockMvc.perform(delete("/productos/1/promociones/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.precioFinal").value(20.0))
                    .andExpect(jsonPath("$.promociones.length()").value(0));
    }

    @Test
    void removePromotion_productoNoExistente_deberiaRetornar404() throws Exception {
        when(productService.removePromotion(99, 1)).thenReturn(null);

        mockMvc.perform(delete("/productos/99/promociones/1"))
                    .andExpect(status().isNotFound());
    }
}
