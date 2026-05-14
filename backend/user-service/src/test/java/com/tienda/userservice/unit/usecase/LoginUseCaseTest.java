package com.tienda.userservice.unit.usecase;

import com.tienda.userservice.application.port.output.GetUserOutputPort;
import com.tienda.userservice.application.usecase.LoginUseCase;
import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private GetUserOutputPort getUserOutputPort;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@test.com");
        testUser.setPassword("password");
    }

    @Test
    void execute_credencialesCorrectas_devuelveToken() {
        when(getUserOutputPort.findByEmailAndPassword("test@test.com", "password"))
                .thenReturn(Mono.just(testUser));
        when(jwtTokenProvider.generateToken("1"))
                .thenReturn("eyJhbGciOiJIUzI1NiJ9.token");

        StepVerifier.create(loginUseCase.execute("test@test.com", "password"))
                .expectNext("eyJhbGciOiJIUzI1NiJ9.token")
                .verifyComplete();

        verify(getUserOutputPort).findByEmailAndPassword("test@test.com", "password");
        verify(jwtTokenProvider).generateToken("1");
    }

    @Test
    void execute_credencialesIncorrectas_devuelveVacio() {
        when(getUserOutputPort.findByEmailAndPassword("wrong@test.com", "wrong"))
                .thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.execute("wrong@test.com", "wrong"))
                .verifyComplete();

        verify(jwtTokenProvider, never()).generateToken(any());
    }
}