package com.tienda.tienda.unit.controller;

import com.tienda.tienda.dto.LineaCarritoDTO;
import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.service.LineaCarritoService;
import com.tienda.tienda.controller.LineaCarritoController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LineaCarritoController.class)
class LineaCarritoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LineaCarritoService lineaCarritoService;

    private LineaCarritoDTO linea;

    @BeforeEach
    void setUp() {
        ProductDTO producto = new ProductDTO();
        producto.setId(1);
        producto.setNombre("Camiseta");
        producto.setPrecio(20.0);
        producto.setPrecioFinal(18.0);

        linea = new LineaCarritoDTO();
        linea.setId(1);
        linea.setCantidad(2);
        linea.setSubtotal(36.0);
        linea.setCarritoId(1);
        linea.setProducto(producto);

    }

    //GET /lineas
    @Test
    void getAllLineas_deberiaRetornarListaYStatus200() throws Exception {
        when(lineaCarritoService.getAllLineas()).thenReturn(List.of(linea));

        mockMvc.perform(get("/lineas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].cantidad").value(2))
                    .andExpect(jsonPath("$[0].subtotal").value(36.0))
                    .andExpect(jsonPath("$[0].carritoId").value(1));
    }

    @Test
    void getAllLineas_listaVacia_deberiaRetornarArrayVacioYStatus200() throws Exception {
        when(lineaCarritoService.getAllLineas()).thenReturn(List.of());

        mockMvc.perform(get("/lineas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
    }

    //GET /lineas/{id}
    @Test
    void getLineaById_existente_deberiaRetornarLineayStatus200() throws Exception {
        when(lineaCarritoService.getLineaById(1)).thenReturn(linea);

        mockMvc.perform(get("/lineas/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.cantidad").value(2))
                    .andExpect(jsonPath("$.subtotal").value(36.0))
                    .andExpect(jsonPath("$.producto.nombre").value("Camiseta"));                 
    }

    @Test
    void getLineaById_noExistente_deberiaRetornar404() throws Exception {
        when(lineaCarritoService.getLineaById(99)).thenReturn(null);

        mockMvc.perform(get("/lineas/99"))
                    .andExpect(status().isNotFound());     
    }

    //PUT /lineas/{id}
    @Test
    void updateLinea_existente_deberiaRetornarLineaActualizadaYStatus200() throws Exception {
        linea.setCantidad(5);
        linea.setSubtotal(90.0);
        when(lineaCarritoService.updateLinea(eq(1), eq(5))).thenReturn(linea);

        mockMvc.perform(put("/lineas/1")
                        .param("cantidad", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cantidad").value(5))
                    .andExpect(jsonPath("$.subtotal").value(90.0));
    }

    @Test
    void updateLinea_noExistente_deberiaRetornar404() throws Exception {
        when(lineaCarritoService.updateLinea(eq(99), eq(5))).thenReturn(null);

        mockMvc.perform(put("/usuarios/99")
                        .param("cantidad", "5"))
                    .andExpect(status().isNotFound());
    }

    //DELETE /lineas/{id}
    @Test
    void deleteLinea_existente_deberiaRetornar204() throws Exception {
        when(lineaCarritoService.deleteLinea(1)).thenReturn(true);

        mockMvc.perform(delete("/lineas/1"))
                    .andExpect(status().isNoContent());
    }

    @Test
    void deleteLinea_noExistente_deberiaRetornar404() throws Exception {
        when(lineaCarritoService.deleteLinea(99)).thenReturn(false);

        mockMvc.perform(delete("/lineas/99"))
                    .andExpect(status().isNotFound());
    }
}
