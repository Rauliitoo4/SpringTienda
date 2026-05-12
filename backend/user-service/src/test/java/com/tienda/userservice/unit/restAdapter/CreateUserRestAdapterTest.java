package com.tienda.userservice.unit.restAdapter;

import com.tienda.userservice.application.port.input.CreateUserInputPort;
import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.infrastructure.adapter.input.rest.CreateUserRestAdapter;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.user.model.UserRequest;
import com.tienda.user.model.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(CreateUserRestAdapter.class)
class CreateUserRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CreateUserInputPort createUserInputPort;

    @MockitoBean
    private UserRestMapper mapper;

    private User user;
    private UserResponse userResponse;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setLastname("García");
        user.setUsername("albertog");
        user.setEmail("alberto@gmail.com");
        user.setCarritoId(1);

        userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("Alberto");
        userResponse.setLastname("García");
        userResponse.setUsername("albertog");
        userResponse.setEmail("alberto@gmail.com");
        userResponse.setCarritoId(1);

        userRequest = new UserRequest();
        userRequest.setName("Alberto");
        userRequest.setLastname("García");
        userRequest.setUsername("albertog");
        userRequest.setEmail("alberto@gmail.com");
        userRequest.setPassword("1234");
    }

    @Test
    void createUser_shouldReturnUserAndStatus201() {
        when(mapper.toDomain(any(UserRequest.class))).thenReturn(user);
        when(createUserInputPort.execute(user)).thenReturn(Mono.just(user));
        when(mapper.toResponse(user)).thenReturn(userResponse);

        webTestClient.post().uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);

        verify(createUserInputPort, times(1)).execute(user);
    }
}