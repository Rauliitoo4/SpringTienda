package com.tienda.userservice.integration;

import com.tienda.userservice.application.model.CarritoModel;
import com.tienda.userservice.application.port.output.CreateCarritoOutputPort;
import com.tienda.userservice.application.usecase.*;
import com.tienda.userservice.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
class UserIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("user_test")
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
        registry.add("services.carrito-service.url", () -> "http://localhost:9999");
    }

    @MockitoBean
    private CreateCarritoOutputPort createCarritoOutputPort;

    @Autowired private CreateUserUseCase createUserUseCase;
    @Autowired private GetUserUseCase getUserUseCase;
    @Autowired private UpdateUserUseCase updateUserUseCase;
    @Autowired private DeleteUserUseCase deleteUserUseCase;
    @Autowired private AddFavoritoUseCase addFavoritoUseCase;
    @Autowired private RemoveFavoritoUseCase removeFavoritoUseCase;

    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());

    @Autowired
    private org.springframework.r2dbc.core.DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        doReturn(Mono.just(new CarritoModel())).when(createCarritoOutputPort).create();

        databaseClient.sql("DELETE FROM usuario_favoritos").fetch().rowsUpdated().block();
        databaseClient.sql("DELETE FROM usuarios").fetch().rowsUpdated().block();
    }

    private User createUserTest() {
        User user = new User();
        user.setName("Antonio");
        user.setLastname("García");
        user.setUsername("antonio");
        user.setEmail("antonio@gmail.com");
        user.setPassword("1234");
        return createUserUseCase.execute(user).block();
    }

    @Test
    void createUser_shouldSaveInDB() {
        User result = createUserTest();

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Antonio", result.getName());
        assertEquals("García", result.getLastname());
        assertNotNull(result.getUsername());
        assertNotNull(result.getEmail());
    }

    @Test
    void getAllUsers_shouldReturnUsersFromDB() {
        createUserTest();
        List<User> users = getUserUseCase.executeAll().collectList().block();

        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    void getUserById_shouldReturnUserFromDB() {
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
        changes.setUsername("updated_" + counter.incrementAndGet());

        User updated = updateUserUseCase.execute(created.getId(), changes).block();

        assertNotNull(updated);
        assertNotNull(updated.getUsername());
    }

    @Test
    void deleteUser_shouldDeleteInDB() {
        User created = createUserTest();
        boolean deleted = deleteUserUseCase.execute(created.getId()).block();

        assertTrue(deleted);
        assertNull(getUserUseCase.execute(created.getId()).block());
    }

    @Test
    void addFavorito_shouldAddProductToFavoritos() {
        User user = createUserTest();

        User result = addFavoritoUseCase.execute(user.getId(), 1).block();

        assertNotNull(result);
        assertFalse(result.getFavoritoIds().isEmpty());
        assertTrue(result.getFavoritoIds().contains(1));
    }

    @Test
    void addFavorito_ifAlreadyExists_shouldNotDuplicate() {
        User user = createUserTest();

        addFavoritoUseCase.execute(user.getId(), 1).block();
        User result = addFavoritoUseCase.execute(user.getId(), 1).block();

        assertNotNull(result);
        assertEquals(1, result.getFavoritoIds().size());
    }

    @Test
    void removeFavorito_shouldRemoveProductFromFavoritos() {
        User user = createUserTest();

        addFavoritoUseCase.execute(user.getId(), 1).block();
        User result = removeFavoritoUseCase.execute(user.getId(), 1).block();

        assertNotNull(result);
        assertTrue(result.getFavoritoIds().isEmpty());
    }

    @Test
    void getUserById_shouldReturnFavoritoIds() {
        User user = createUserTest();

        addFavoritoUseCase.execute(user.getId(), 1).block();
        User result = getUserUseCase.execute(user.getId()).block();

        assertNotNull(result);
        assertTrue(result.getFavoritoIds().contains(1));
    }
}