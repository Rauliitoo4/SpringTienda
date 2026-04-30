package com.tienda.tienda.unit.restAdapter.user;

import com.tienda.tienda.user.application.port.input.UpdateUserInputPort;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.UpdateUserRestAdapter;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(UpdateUserRestAdapter.class)
class UpdateUserRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UpdateUserInputPort updateUserInputPort;

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
        basicUserResponse.setName("Antonio");
        basicUserResponse.setLastname("García");
        basicUserResponse.setUsername("albertog");
        basicUserResponse.setEmail("albertog@gmail.com");
        basicUserResponse.setCarritoId(1);

        basicUserRequest = new UserRequest();
        basicUserRequest.setName("Antonio");
    }

    @Test
    void updateUser_shouldReturnUpdatedUserAndStatus200() {
        when(userRestMapper.toDomain(any(UserRequest.class))).thenReturn(basicUser);
        when(updateUserInputPort.execute(eq(1), any(User.class))).thenReturn(Mono.just(basicUser));
        when(userRestMapper.toResponse(any(User.class))).thenReturn(basicUserResponse);

        webTestClient.put().uri("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicUserRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(basicUserResponse);
    }

    @Test
    void updateUser_ifNotExists_shouldReturn404() {
        when(userRestMapper.toDomain(any(UserRequest.class))).thenReturn(basicUser);
        when(updateUserInputPort.execute(eq(99), any(User.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(basicUserRequest)
                .exchange()
                .expectStatus().isNotFound();
    }
}