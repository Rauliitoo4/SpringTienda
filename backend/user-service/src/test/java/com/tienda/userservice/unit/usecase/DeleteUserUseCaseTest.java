package com.tienda.userservice.unit.usecase;

import com.tienda.userservice.application.port.output.DeleteUserOutputPort;
import com.tienda.userservice.application.usecase.DeleteUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

    @Mock
    private DeleteUserOutputPort deleteUserOutputPort;

    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;

    @Test
    void execute_shouldDeleteAndReturnTrue() {
        when(deleteUserOutputPort.existsById(1)).thenReturn(Mono.just(true));
        when(deleteUserOutputPort.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(deleteUserUseCase.execute(1))
                .expectNext(true)
                .verifyComplete();

        verify(deleteUserOutputPort, times(1)).deleteById(1);
    }

    @Test
    void execute_ifNotExists_shouldReturnFalse() {
        when(deleteUserOutputPort.existsById(99)).thenReturn(Mono.just(false));

        StepVerifier.create(deleteUserUseCase.execute(99))
                .expectNext(false)
                .verifyComplete();

        verify(deleteUserOutputPort, never()).deleteById(anyInt());
    }
}