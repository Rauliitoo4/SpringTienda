package com.tienda.tienda.integration;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.application.usecase.*;
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
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private GetUserUseCase getUserUseCase;

    @Autowired
    private UpdateUserUseCase updateUserUseCase;

    @Autowired
    private DeleteUserUseCase deleteUserUseCase;

    private User createUserTest() {
        User user = new User();
        user.setName("Alberto");
        user.setLastname("García");
        user.setUsername("albertog");
        user.setEmail("albertog@gmail.com");
        user.setPassword("1234");
        return createUserUseCase.execute(user).block();
    }

    @Test
    void createUser_shouldSaveInDB() {
        User result = createUserTest();

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
        User result = createUserTest();
        assertTrue(result.getCarritoId() > 0);
    }

    @Test
    void getAllUsers_shouldReturn_usersFromDB() {
        createUserTest();
        List<User> users = getUserUseCase.executeAll().collectList().block();

        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    void getUserById_shouldReturn_userFromDB() {
        User created = createUserTest();
        User result = getUserUseCase.execute(created.getId()).block();

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
    }

    @Test
    void getUserById_ifNotExists_shouldReturnNull() {
        User result = getUserUseCase.execute(9999).block();
        assertNull(result);
    }

    @Test
    void updateUser_shouldUpdateDataInDB() {
        User created = createUserTest();

        User changes = new User();
        changes.setUsername("albertitog");

        User updated = updateUserUseCase.execute(created.getId(), changes).block();

        assertNotNull(updated);
        assertEquals("albertitog", updated.getUsername());
    }

    @Test
    void deleteUser_shouldDeleteInDB() {
        User created = createUserTest();
        boolean deleted = deleteUserUseCase.execute(created.getId()).block();

        assertTrue(deleted);
        assertNull(getUserUseCase.execute(created.getId()).block());
    }

}

