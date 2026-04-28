package com.tienda.tienda.unit.usecase.user;

import com.tienda.tienda.user.application.usecase.GetUserUseCase;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.domain.repository.GetUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseTest {

    @Mock private GetUserRepository getUserRepository;

    @InjectMocks
    private GetUserUseCase getUserUseCase;

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
    void execute_shouldReturnUser() {
        User user = testUser();
        when(getUserRepository.findById(1)).thenReturn(Mono.just(user));

        StepVerifier.create(getUserUseCase.execute(1))
                .expectNextMatches(result ->
                        result.getName().equals("Alberto") &&
                                result.getLastname().equals("García"))
                .verifyComplete();
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getUserRepository.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(getUserUseCase.execute(999))
                .verifyComplete();
    }

    @Test
    void executeAll_shouldReturnAllUsers() {
        User user = testUser();
        when(getUserRepository.findAll()).thenReturn(Flux.just(user));

        StepVerifier.create(getUserUseCase.executeAll())
                .expectNextMatches(result -> result.getName().equals("Alberto"))
                .verifyComplete();
    }
}