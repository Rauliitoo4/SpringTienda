package com.tienda.userservice.unit.restAdapter;

import com.tienda.userservice.application.port.input.LoginInputPort;
import com.tienda.userservice.infrastructure.adapter.input.rest.LoginRestAdapter;
import com.tienda.user.model.LoginRequest;
import com.tienda.userservice.infrastructure.security.JwtAuthenticationFilter;
import com.tienda.userservice.infrastructure.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class LoginRestAdapterTest {

    @Mock
    private LoginInputPort loginInputPort;

    @InjectMocks
    private LoginRestAdapter loginRestAdapter;

    @Test
    void login_credencialesCorrectas_devuelve200ConToken() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        when(loginInputPort.execute("test@test.com", "password"))
                .thenReturn(Mono.just("eyJhbGciOiJIUzI1NiJ9.token"));

        StepVerifier.create(loginRestAdapter.login(request))
                .assertNext(response -> {
                    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                    assertThat(response.getBody()).isNotNull();
                    assertThat(response.getBody().getToken()).isEqualTo("eyJhbGciOiJIUzI1NiJ9.token");
                })
                .verifyComplete();
    }

    @Test
    void login_credencialesIncorrectas_devuelve401() {
        LoginRequest request = new LoginRequest();
        request.setEmail("wrong@test.com");
        request.setPassword("wrong");

        when(loginInputPort.execute("wrong@test.com", "wrong"))
                .thenReturn(Mono.empty());

        StepVerifier.create(loginRestAdapter.login(request))
                .assertNext(response ->
                        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED))
                .verifyComplete();
    }
}