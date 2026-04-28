package com.tienda.tienda.unit.usecase.user;

import com.tienda.tienda.carrito.application.usecase.CreateCarritoUseCase;
import com.tienda.tienda.carrito.domain.model.Carrito;
import com.tienda.tienda.user.application.usecase.CreateUserUseCase;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.domain.repository.CreateUserRepository;
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

    @Mock private CreateUserRepository createUserRepository;
    @Mock private CreateCarritoUseCase createCarritoUseCase;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private User testUser() {
        User user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setLastname("García");
        user.setUsername("albertog");
        user.setEmail("albertog@gmail.com");
        user.setPassword("1234");
        user.setCarritoId(1);
        return user;
    }

    private Carrito testCarrito() {
        Carrito carrito = new Carrito();
        carrito.setId(1);
        carrito.setTotal(0.0);
        return carrito;
    }

    @Test
    void execute_shouldCreateCarritoAndSaveUser() {
        User user = testUser();
        when(createCarritoUseCase.execute()).thenReturn(Mono.just(testCarrito()));
        when(createUserRepository.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(createUserUseCase.execute(user))
                .expectNextMatches(result ->
                        result.getName().equals("Alberto") &&
                                result.getCarritoId() == 1)
                .verifyComplete();

        verify(createUserRepository, times(1)).save(any(User.class));
        verify(createCarritoUseCase, times(1)).execute();
    }
}