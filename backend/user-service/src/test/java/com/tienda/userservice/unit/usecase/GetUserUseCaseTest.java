package com.tienda.userservice.unit.usecase;

import com.tienda.userservice.application.port.output.GetUserOutputPort;
import com.tienda.userservice.application.service.FavoritoLoader;
import com.tienda.userservice.application.usecase.GetUserUseCase;
import com.tienda.userservice.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseTest {

    @Mock private GetUserOutputPort getUserOutputPort;
    @Mock private FavoritoLoader favoritoLoader;

    @InjectMocks
    private GetUserUseCase getUserUseCase;

    private User testUser() {
        User user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setEmail("alberto@gmail.com");
        user.setFavoritoIds(List.of());
        return user;
    }

    @Test
    void execute_shouldReturnUserWithFavoritos() {
        User user = testUser();

        when(getUserOutputPort.findById(1)).thenReturn(Mono.just(user));
        when(favoritoLoader.loadFavoritos(user)).thenReturn(Mono.just(user));

        StepVerifier.create(getUserUseCase.execute(1))
                .expectNextMatches(result -> result.getId() == 1)
                .verifyComplete();

        verify(favoritoLoader, times(1)).loadFavoritos(user);
    }

    @Test
    void execute_ifNotExists_shouldReturnEmpty() {
        when(getUserOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(getUserUseCase.execute(99))
                .verifyComplete();

        verify(favoritoLoader, never()).loadFavoritos(any());
    }

    @Test
    void executeAll_shouldReturnAllUsersWithFavoritos() {
        User user = testUser();

        when(getUserOutputPort.findAll()).thenReturn(Flux.just(user));
        when(favoritoLoader.loadFavoritos(user)).thenReturn(Mono.just(user));

        StepVerifier.create(getUserUseCase.executeAll())
                .expectNextCount(1)
                .verifyComplete();

        verify(favoritoLoader, times(1)).loadFavoritos(user);
    }
}