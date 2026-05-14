package com.tienda.userservice.unit.restAdapter;

import com.tienda.userservice.application.port.input.UpdateUserInputPort;
import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.infrastructure.adapter.input.rest.UpdateUserRestAdapter;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.user.model.UserRequest;
import com.tienda.user.model.UserResponse;
import com.tienda.userservice.infrastructure.security.JwtAuthenticationFilter;
import com.tienda.userservice.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebFluxTest(UpdateUserRestAdapter.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class UpdateUserRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UpdateUserInputPort updateUserUseCase;

    @MockitoBean
    private UserRestMapper mapper;

    private User user;
    private UserResponse userResponse;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Alberto Actualizado");
        user.setEmail("alberto@gmail.com");

        userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("Alberto Actualizado");
        userResponse.setEmail("alberto@gmail.com");

        userRequest = new UserRequest();
        userRequest.setName("Alberto Actualizado");
        userRequest.setEmail("alberto@gmail.com");
    }

    @Test
    @WithMockUser
    void updateUser_shouldReturnUserAndStatus200() {
        when(mapper.toDomain(any(UserRequest.class))).thenReturn(user);
        when(updateUserUseCase.execute(eq(1), any(User.class))).thenReturn(Mono.just(user));
        when(mapper.toResponse(user)).thenReturn(userResponse);

        webTestClient.put().uri("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);

        verify(updateUserUseCase, times(1)).execute(eq(1), any(User.class));
    }

    @Test
    @WithMockUser
    void updateUser_ifNotExists_shouldReturn404() {
        when(mapper.toDomain(any(UserRequest.class))).thenReturn(user);
        when(updateUserUseCase.execute(eq(99), any(User.class))).thenReturn(Mono.empty());

        webTestClient.put().uri("/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isNotFound();
    }
}