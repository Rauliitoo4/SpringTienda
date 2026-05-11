package com.tienda.userservice.unit.usecase;

import com.tienda.userservice.application.port.output.GetUserOutputPort;
import com.tienda.userservice.application.port.output.UpdateUserOutputPort;
import com.tienda.userservice.application.usecase.UpdateUserUseCase;
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
class UpdateUserUseCaseTest {

    @Mock private GetUserOutputPort getUserOutputPort;
    @Mock private UpdateUserOutputPort updateUserOutputPort;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    private User testUser() {
        User user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setLastname("García");
        user.setUsername("albertog");
        user.setEmail("alberto@gmail.com");
        return user;
    }

    @Test
    void execute_shouldUpdateFieldsAndSave() {
        User existing = testUser();

        User updated = new User();
        updated.setName("Alberto Actualizado");
        updated.setEmail("nuevo@gmail.com");

        User saved = testUser();
        saved.setName("Alberto Actualizado");
        saved.setEmail("nuevo@gmail.com");

        when(getUserOutputPort.findById(1)).thenReturn(Mono.just(existing));
        when(updateUserOutputPort.save(any(User.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(updateUserUseCase.execute(1, updated))
                .expectNextMatches(result ->
                        result.getName().equals("Alberto Actualizado") &&
                                result.getEmail().equals("nuevo@gmail.com"))
                .verifyComplete();

        verify(updateUserOutputPort, times(1)).save(any(User.class));
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getUserOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(updateUserUseCase.execute(99, testUser()))
                .verifyComplete();

        verify(updateUserOutputPort, never()).save(any());
    }
}