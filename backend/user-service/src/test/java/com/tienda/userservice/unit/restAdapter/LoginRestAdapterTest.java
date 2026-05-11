package com.tienda.userservice.unit.restAdapter;

import com.tienda.userservice.application.port.input.LoginInputPort;
import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.infrastructure.adapter.input.rest.LoginRestAdapter;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.request.LoginRequest;
import com.tienda.userservice.infrastructure.adapter.input.rest.data.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@WebFluxTest(LoginRestAdapter.class)
class LoginRestAdapterTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private LoginInputPort loginInputPort;

    @MockitoBean
    private UserRestMapper mapper;

    private User user;
    private UserResponse userResponse;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setEmail("alberto@gmail.com");

        userResponse = new UserResponse();
        userResponse.setId(1);
        userResponse.setName("Alberto");
        userResponse.setEmail("alberto@gmail.com");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("alberto@gmail.com");
        loginRequest.setPassword("1234");
    }

    @Test
    void login_shouldReturnUserAndStatus200() {
        when(loginInputPort.execute("alberto@gmail.com", "1234")).thenReturn(Mono.just(user));
        when(mapper.toResponse(user)).thenReturn(userResponse);

        webTestClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);

        verify(loginInputPort, times(1)).execute("alberto@gmail.com", "1234");
    }

    @Test
    void login_ifCredentialsInvalid_shouldReturn401() {
        when(loginInputPort.execute("wrong@gmail.com", "wrongpass")).thenReturn(Mono.empty());

        LoginRequest wrongRequest = new LoginRequest();
        wrongRequest.setEmail("wrong@gmail.com");
        wrongRequest.setPassword("wrongpass");

        webTestClient.post().uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(wrongRequest)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}