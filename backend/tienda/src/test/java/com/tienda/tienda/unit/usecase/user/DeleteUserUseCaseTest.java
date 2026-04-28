package com.tienda.tienda.unit.usecase.user;

import com.tienda.tienda.user.application.usecase.DeleteUserUseCase;
import com.tienda.tienda.user.domain.repository.DeleteUserRepository;
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

    @Mock private DeleteUserRepository deleteUserRepository;

    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;

    @Test
    void execute_ifExists_shouldReturnTrue() {
        when(deleteUserRepository.existsById(1)).thenReturn(Mono.just(true));
        when(deleteUserRepository.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(deleteUserUseCase.execute(1))
                .expectNext(true)
                .verifyComplete();

        verify(deleteUserRepository, times(1)).deleteById(1);
    }

    @Test
    void execute_ifNotExists_shouldReturnFalse() {
        when(deleteUserRepository.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(deleteUserUseCase.execute(999))
                .expectNext(false)
                .verifyComplete();

        verify(deleteUserRepository, never()).deleteById(anyInt());
    }
}