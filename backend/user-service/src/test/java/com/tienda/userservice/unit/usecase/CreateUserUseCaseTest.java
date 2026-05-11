package com.tienda.userservice.unit.usecase;

import com.tienda.userservice.application.model.CarritoModel;
import com.tienda.userservice.application.port.output.CreateCarritoOutputPort;
import com.tienda.userservice.application.port.output.CreateUserOutputPort;
import com.tienda.userservice.application.usecase.CreateUserUseCase;
import com.tienda.userservice.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock private CreateUserOutputPort createUserOutputPort;
    @Mock private CreateCarritoOutputPort createCarritoOutputPort;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    void execute_shouldCreateCarritoAndThenUser() {
        CarritoModel carrito = new CarritoModel();
        carrito.setId(1);

        User user = new User();
        user.setName("Alberto");
        user.setEmail("alberto@gmail.com");

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setName("Alberto");
        savedUser.setEmail("alberto@gmail.com");
        savedUser.setCarritoId(1);

        when(createCarritoOutputPort.create()).thenReturn(Mono.just(carrito));
        when(createUserOutputPort.save(any(User.class))).thenReturn(Mono.just(savedUser));

        StepVerifier.create(createUserUseCase.execute(user))
                .expectNextMatches(result -> result.getId() == 1 && result.getCarritoId() == 1)
                .verifyComplete();

        verify(createCarritoOutputPort, times(1)).create();
        verify(createUserOutputPort, times(1)).save(any(User.class));
    }
}