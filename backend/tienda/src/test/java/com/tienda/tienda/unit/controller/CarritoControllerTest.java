package com.tienda.tienda.unit.controller;

import com.tienda.tienda.dto.CarritoDTO;
import com.tienda.tienda.dto.LineaCarritoDTO;
import com.tienda.tienda.dto.ProductDTO;
import com.tienda.tienda.service.CarritoService;
import com.tienda.tienda.controller.CarritoController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarritoController.class)
class CarritoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarritoService carritoService;

    private CarritoDTO carritoVacio;
    private CarritoDTO carritoConLinea;

    @BeforeEach
    void setUp() {
        carritoVacio = new CarritoDTO();
        carritoVacio.setId(1);
        carritoVacio.setTotal(0.0);
        carritoVacio.setLineas(List.of());

        ProductDTO producto = new ProductDTO();
        producto.setId(1);
        producto.setNombre("Camiseta");
        producto.setPrecio(20.0);
        producto.setPrecioFinal(18.0);

        LineaCarritoDTO linea = new LineaCarritoDTO();
        linea.setId(1);
        linea.setCantidad(2);
        linea.setSubtotal(36.0);
        linea.setProducto(producto);

        carritoConLinea = new CarritoDTO();
        carritoConLinea.setId(1);
        carritoConLinea.setTotal(36.0);
        carritoConLinea.setLineas(List.of(linea));
    }

    //GET /carritos/{id}
    @Test
    void getCarritoById_existente_deberiaRetornarCarritoyStatus200() throws Exception {
        when(carritoService.getCarritoById(1)).thenReturn(carritoVacio);

        mockMvc.perform(get("/carritos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.total").value(0.0))
                    .andExpect(jsonPath("$.lineas.length()").value(0));                 
    }

    @Test
    void getCarritoById_noExistente_deberiaRetornar404l() throws Exception {
        when(carritoService.getCarritoById(99)).thenReturn(null);

        mockMvc.perform(get("/carritos/99"))
                    .andExpect(status().isNotFound());     
    }

    //POST /carritos/{carritoId}/productos/{productoId}
    @Test
    void addProductToCarrito_deberiaRetornarCarritoActualizadoYStatus200() throws Exception {
        when(carritoService.addProductToCarrito(1, 1, 2)).thenReturn(carritoConLinea);
        
        mockMvc.perform(post("/carritos/1/productos/1")
                        .param("cantidad", "2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.total").value(36.0))
                    .andExpect(jsonPath("$.lineas.length()").value(1))
                    .andExpect(jsonPath("$.lineas[0].cantidad").value(2))
                    .andExpect(jsonPath("$.lineas[0].subtotal").value(36.0))
                    .andExpect(jsonPath("$.lineas[0].producto.nombre").value("Camiseta"));
    }

    @Test
    void addProductToCarrito_carritoNoExiste_deberiaRetornar404() throws Exception {
        when(carritoService.addProductToCarrito(99, 1, 2)).thenReturn(null);
        
        mockMvc.perform(post("/carritos/99/productos/1")
                        .param("cantidad", "2"))
                    .andExpect(status().isNotFound());
    }

    @Test
    void addProductToCarrito_productoNoExiste_deberiaRetornar404() throws Exception {
        when(carritoService.addProductToCarrito(1, 99, 2)).thenReturn(null);
        
        mockMvc.perform(post("/carritos/1/productos/99")
                        .param("cantidad", "2"))
                    .andExpect(status().isNotFound());
    }

    @Test
    void addProductToCarrito_deberiaDelegarEnServicio() throws Exception {
        when(carritoService.addProductToCarrito(1, 1, 2)).thenReturn(carritoConLinea);
        
        mockMvc.perform(post("/carritos/1/productos/1")
                        .param("cantidad", "2"))
                    .andExpect(status().isOk());
        
        verify(carritoService, times(1)).addProductToCarrito(1, 1, 2);
    }
}
