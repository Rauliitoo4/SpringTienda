package com.tienda.tienda.unit.restAdapter.user;

import com.tienda.tienda.user.application.port.input.CreateUserInputPort;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.CreateUserRestAdapter;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.request.UserRequest;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
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
    private UserRestMapper userRestMapper;

    private User basicUser;
    private UserResponse basicUserResponse;
    private UserRequest basicUserRequest;

    @BeforeEach
    void setUp() {
        basicUser = new User();
        basicUser.setId(1);
        basicUser.setName("Alberto");
        basicUser.setLastname("García");
        basicUser.setUsername("albertog");
        basicUser.setEmail("albertog@gmail.com");
        basicUser.setCarritoId(1);

        basicUserResponse = new UserResponse();
        basicUserResponse.setId(1);
        basicUserResponse.setName("Alberto");
        basicUserResponse.setLastname("García");
        basicUserResponse.setUsername("albertog");
        basicUserResponse.setEmail("albertog@gmail.com");
        basicUserResponse.setCarritoId(1);

        basicUserRequest = new UserRequest();
        basicUserRequest.setName("Alberto");
        basicUserRequest.setLastname("García");
        basicUserRequest.setUsername("albertog");
        basicUserRequest.setEmail("albertog@gmail.com");
        basicUserRequest.setPassword("1234");
    }

    @Test
    void createUser_shouldReturnCreatedUserAndStatus201() {
        when(userRestMapper.toDomain(any(UserRequest.class))).thenReturn(basicUser);
        when(createUserInputPort.execute(any(User.class))).thenReturn(Mono.just(basicUser));
        when(userRestMapper.toResponse(any(User.class))).thenReturn(basicUserResponse);

        webTestClient.post().uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicUserRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .isEqualTo(basicUserResponse);

        verify(createUserInputPort, times(1)).execute(any(User.class));
    }
}