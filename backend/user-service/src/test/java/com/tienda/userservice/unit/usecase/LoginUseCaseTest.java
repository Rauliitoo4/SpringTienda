package com.tienda.userservice.unit.usecase;

import com.tienda.userservice.application.port.output.GetUserOutputPort;
import com.tienda.userservice.application.usecase.LoginUseCase;
import com.tienda.userservice.domain.model.User;
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

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Test
    void execute_shouldReturnUserIfCredentialsAreValid() {
        User user = new User();
        user.setId(1);
        user.setEmail("alberto@gmail.com");

        when(getUserOutputPort.findByEmailAndPassword("alberto@gmail.com", "1234"))
                .thenReturn(Mono.just(user));

        StepVerifier.create(loginUseCase.execute("alberto@gmail.com", "1234"))
                .expectNextMatches(result -> result.getId() == 1)
                .verifyComplete();

        verify(getUserOutputPort, times(1)).findByEmailAndPassword("alberto@gmail.com", "1234");
    }

    @Test
    void execute_ifCredentialsInvalid_shouldReturnEmpty() {
        when(getUserOutputPort.findByEmailAndPassword("wrong@gmail.com", "wrongpass"))
                .thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.execute("wrong@gmail.com", "wrongpass"))
                .verifyComplete();
    }
}