package com.tienda.tienda.unit.usecase.user;

import com.tienda.tienda.user.application.usecase.UpdateUserUseCase;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.domain.repository.GetUserRepository;
import com.tienda.tienda.user.domain.repository.UpdateUserRepository;
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

    @Mock private GetUserRepository getUserRepository;
    @Mock private UpdateUserRepository updateUserRepository;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    private User testUser() {
        User user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setLastname("García");
        user.setUsername("albertog");
        user.setEmail("albertog@gmail.com");
        user.setCarritoId(1);
        return user;
    }

    @Test
    void execute_shouldUpdateAndReturnUser() {
        User user = testUser();
        when(getUserRepository.findById(1)).thenReturn(Mono.just(user));
        when(updateUserRepository.save(any(User.class))).thenReturn(Mono.just(user));

        User changes = new User();
        changes.setUsername("albertitog");

        StepVerifier.create(updateUserUseCase.execute(1, changes))
                .expectNextMatches(result -> result.getUsername().equals("albertitog"))
                .verifyComplete();

        verify(updateUserRepository, times(1)).save(any(User.class));
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getUserRepository.findById(999)).thenReturn(Mono.empty());

        User changes = new User();
        changes.setUsername("albertitog");

        StepVerifier.create(updateUserUseCase.execute(999, changes))
                .verifyComplete();
    }
}