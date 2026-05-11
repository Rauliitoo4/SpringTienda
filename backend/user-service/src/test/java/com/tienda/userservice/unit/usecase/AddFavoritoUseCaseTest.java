package com.tienda.userservice.unit.usecase;

import com.tienda.userservice.application.port.output.GetUserOutputPort;
import com.tienda.userservice.application.port.output.UserFavoritoOutputPort;
import com.tienda.userservice.application.service.FavoritoLoader;
import com.tienda.userservice.application.usecase.AddFavoritoUseCase;
import com.tienda.userservice.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddFavoritoUseCaseTest {

    @Mock private GetUserOutputPort getUserOutputPort;
    @Mock private UserFavoritoOutputPort userFavoritoOutputPort;
    @Mock private FavoritoLoader favoritoLoader;

    @InjectMocks
    private AddFavoritoUseCase addFavoritoUseCase;

    private User testUser() {
        User user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setEmail("alberto@gmail.com");
        user.setFavoritoIds(List.of());
        return user;
    }

    @Test
    void execute_shouldAddFavoritoAndLoadFavoritos() {
        User user = testUser();
        User userWithFavorito = testUser();
        userWithFavorito.setFavoritoIds(List.of(1));

        when(getUserOutputPort.findById(1)).thenReturn(Mono.just(user));
        when(userFavoritoOutputPort.existsRelation(1, 1)).thenReturn(Mono.just(0));
        when(userFavoritoOutputPort.insertRelation(1, 1)).thenReturn(Mono.empty());
        when(favoritoLoader.loadFavoritos(user)).thenReturn(Mono.just(userWithFavorito));

        StepVerifier.create(addFavoritoUseCase.execute(1, 1))
                .expectNextMatches(result -> result.getFavoritoIds().contains(1))
                .verifyComplete();

        verify(userFavoritoOutputPort, times(1)).insertRelation(1, 1);
    }

    @Test
    void execute_ifRelationAlreadyExists_shouldNotInsertAgain() {
        User user = testUser();
        User userWithFavorito = testUser();
        userWithFavorito.setFavoritoIds(List.of(1));

        when(getUserOutputPort.findById(1)).thenReturn(Mono.just(user));
        when(userFavoritoOutputPort.existsRelation(1, 1)).thenReturn(Mono.just(1));
        when(favoritoLoader.loadFavoritos(user)).thenReturn(Mono.just(userWithFavorito));

        StepVerifier.create(addFavoritoUseCase.execute(1, 1))
                .expectNextMatches(result -> !result.getFavoritoIds().isEmpty())
                .verifyComplete();

        verify(userFavoritoOutputPort, never()).insertRelation(anyInt(), anyInt());
    }

    @Test
    void execute_ifUserNotExists_shouldReturnEmpty() {
        when(getUserOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(addFavoritoUseCase.execute(99, 1))
                .verifyComplete();

        verify(userFavoritoOutputPort, never()).insertRelation(anyInt(), anyInt());
    }
}