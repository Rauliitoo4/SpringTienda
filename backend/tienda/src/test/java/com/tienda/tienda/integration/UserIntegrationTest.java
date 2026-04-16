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

    private UserResponseDTO createUserTest() {
        UserDTO dto = new UserDTO();
        dto.setName("Alberto");
        dto.setLastname("García");
        dto.setUsername("albertog");
        dto.setEmail("albertog@gmail.com");
        dto.setPassword("1234");
        return userService.createUser(dto).block();
    }

    @Test
    void createUser_shouldSaveInDB() {
        UserResponseDTO result = createUserTest();

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Alberto", result.getName());
        assertEquals("García", result.getLastname());
        assertEquals("albertog", result.getUsername());
        assertEquals("albertog@gmail.com", result.getEmail());
        assertTrue(result.getCarritoId() > 0);
    }

    @Test
    void createUser_shouldCreateCarrito() {
        UserResponseDTO result = createUserTest();
        assertTrue(result.getCarritoId() > 0);
    }

    @Test
    void getAllUsers_shouldReturn_usersFromDB() {
        createUserTest();
        List<UserResponseDTO> users = userService.getAllUsers().collectList().block();

        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    void getUserById_shouldReturn_userFromDB() {
        UserResponseDTO created = createUserTest();
        UserResponseDTO result = userService.getUserById(created.getId()).block();

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
    }

    @Test
    void getUserById_ifNotExists_shouldReturnNull() {
        UserResponseDTO result = userService.getUserById(9999).block();
        assertNull(result);
    }

    @Test
    void updateUser_shouldUpdateDataInDB() {
        UserResponseDTO created = createUserTest();

        UserDTO changes = new UserDTO();
        changes.setUsername("albertitog");

        UserResponseDTO updated = userService.updateUser(created.getId(), changes).block();

        assertNotNull(updated);
        assertEquals("albertitog", updated.getUsername());
    }

    @Test
    void deleteUser_shouldDeleteInDB() {
        UserResponseDTO created = createUserTest();
        boolean deleted = userService.deleteUser(created.getId()).block();

        assertTrue(deleted);
        assertNull(userService.getUserById(created.getId()).block());
    }

}

