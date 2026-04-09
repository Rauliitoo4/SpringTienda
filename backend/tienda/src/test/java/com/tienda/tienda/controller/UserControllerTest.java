package com.tienda.tienda.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienda.tienda.dto.UserResponseDTO;
import com.tienda.tienda.dto.UserDTO;
import com.tienda.tienda.service.UserService;

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

@WebMvcTest(UserController.class)
class UserControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserResponseDTO usuarioResponse;
    private UserDTO usuarioRequest;

    @BeforeEach
    void setUp() {
        usuarioResponse = new UserResponseDTO();
        usuarioResponse.setId(1);
        usuarioResponse.setNombre("Alberto");
        usuarioResponse.setApellidos("García");
        usuarioResponse.setUsername("albertog");
        usuarioResponse.setEmail("albertog@gmail.com");
        usuarioResponse.setCarritoId(1);

        usuarioRequest = new UserDTO();
        usuarioRequest.setNombre("Alberto");
        usuarioRequest.setApellidos("García");
        usuarioRequest.setUsername("albertog");
        usuarioRequest.setEmail("albertog@gmail.com");
        usuarioRequest.setPassword("1234");
    }

    //GET /usuarios
    @Test
    void getAllUsers_deberiaRetornarListaYStatus200() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(usuarioResponse));

        mockMvc.perform(get("/usuarios"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].nombre").value("Alberto"))
                    .andExpect(jsonPath("$[0].carritoId").value(1));

    }

    @Test
    void getAllUsers_listaVacia_deberiaRetornarArrayVacioYStatus200() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/usuarios"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
    }

    //GET /usuarios/{id}
    @Test
    void getUserById_existente_deberiaRetornarUsuarioyStatus200() throws Exception {
        when(userService.getUserById(1)).thenReturn(usuarioResponse);

        mockMvc.perform(get("/usuarios/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("Alberto"))
                    .andExpect(jsonPath("$.carritoId").value(1));                 
    }

    @Test
    void getUserById_noExistente_deberiaRetornar404() throws Exception {
        when(userService.getUserById(99)).thenReturn(null);

        mockMvc.perform(get("/usuarios/99"))
                    .andExpect(status().isNotFound());     
    }

    //POST /usuarios
    @Test
    void createUser_deberiaRetornarUsuarioCreadoYStatus201() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(usuarioResponse);
        
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("Alberto"))
                    .andExpect(jsonPath("$.carritoId").value(1));
    }

    @Test
    void createUser_fallido_deberiaRetornar400() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(null);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_deberiaDelegarEnServicio() throws Exception {
        when(userService.createUser(any(UserDTO.class))).thenReturn(usuarioResponse);
        
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                    .andExpect(status().isCreated());
        verify(userService, times(1)).createUser(any(UserDTO.class));
    }

    //PUT /usuarios/{id}
    @Test
    void updateUser_existente_deberiaRetornarUsuarioActualizadoYStatus200() throws Exception {
        usuarioResponse.setNombre("Antonio");
        when(userService.updateUser(eq(1), any(UserDTO.class))).thenReturn(usuarioResponse);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Antonio"));
    }

    @Test
    void updateUser_noExistente_deberiaRetornar404() throws Exception {
        when(userService.updateUser(eq(99), any(UserDTO.class))).thenReturn(null);

        mockMvc.perform(put("/usuarios/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioRequest)))
                    .andExpect(status().isNotFound());
    }

    //DELETE /usuarios/{id}
    @Test
    void deleteUser_existente_deberiaRetornar204() throws Exception {
        when(userService.deleteUser(1)).thenReturn(true);

        mockMvc.perform(delete("/usuarios/1"))
                    .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_noExistente_deberiaRetornar404() throws Exception {
        when(userService.deleteUser(99)).thenReturn(false);

        mockMvc.perform(delete("/usuarios/99"))
                    .andExpect(status().isNotFound());
    }
}
