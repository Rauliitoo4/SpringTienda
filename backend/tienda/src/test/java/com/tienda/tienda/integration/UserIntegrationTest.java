package com.tienda.tienda.integration;

import com.tienda.tienda.dto.UserDTO;
import com.tienda.tienda.dto.UserResponseDTO;
import com.tienda.tienda.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class UserIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("tienda_test")
            .withUsername("postgres")
            .withPassword("admin123");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
                + postgres.getHost() + ":" + postgres.getMappedPort(5432)
                + "/" + postgres.getDatabaseName());
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);

        registry.add("spring.liquibase.url", postgres::getJdbcUrl);
        registry.add("spring.liquibase.user", postgres::getUsername);
        registry.add("spring.liquibase.password", postgres::getPassword);
    }

    @Autowired
    private UserService userService;

    private UserResponseDTO crearUsuarioTest() {
        UserDTO dto = new UserDTO();
        dto.setNombre("Alberto");
        dto.setApellidos("García");
        dto.setUsername("albertog");
        dto.setEmail("albertog@gmail.com");
        dto.setPassword("1234");
        return userService.createUser(dto).block();
    }

    @Test
    void crearUsuario_deberiaGuardarloEnBD() {
        UserResponseDTO resultado = crearUsuarioTest();

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Alberto", resultado.getNombre());
        assertEquals("García", resultado.getApellidos());
        assertEquals("albertog", resultado.getUsername());
        assertEquals("albertog@gmail.com", resultado.getEmail());
        assertTrue(resultado.getCarritoId() > 0);
    }

    @Test
    void crearUsuario_deberiaCrearCarrito() {
        UserResponseDTO resultado = crearUsuarioTest();
        assertTrue(resultado.getCarritoId() > 0);    
    }

    @Test
    void obtenerTodosLosUsuarios_deberiaDevolver_losUsuariosDeBD() {
        crearUsuarioTest();
        List<UserResponseDTO> usuarios = userService.getAllUsers().collectList().block();

        assertNotNull(usuarios);
        assertFalse(usuarios.isEmpty());
    }

    @Test
    void obtenerUsuarioPorID_deberiaDevolver_elUsuarioDeBD() {
        UserResponseDTO creado = crearUsuarioTest();
        UserResponseDTO resultado = userService.getUserById(creado.getId()).block();

        assertNotNull(resultado);
        assertEquals(creado.getId(), resultado.getId());
    }

    @Test
    void obtenerUsuarioPorID_siNoExiste_deberiaDevolverNull() {
        UserResponseDTO resultado = userService.getUserById(9999).block();
        assertNull(resultado);
    }

    @Test
    void actualizarUsuario_deberiaModificarLosDatosEnBD() {
        UserResponseDTO creado = crearUsuarioTest();

        UserDTO cambios = new UserDTO();
        cambios.setUsername("albertitog");

        UserResponseDTO actualizado = userService.updateUser(creado.getId(), cambios).block();

        assertNotNull(actualizado);
        assertEquals("albertitog", actualizado.getUsername());
    }

    @Test
    void eliminarUsuario_deberiaEliminarloEnBD() {
        UserResponseDTO creado = crearUsuarioTest();
        boolean eliminado = userService.deleteUser(creado.getId()).block();

        assertTrue(eliminado);
        assertNull(userService.getUserById(creado.getId()).block());
    }

}

