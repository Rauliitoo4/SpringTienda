package com.tienda.tienda.unit.usecase.user;

import com.tienda.tienda.user.application.port.output.GetUserOutputPort;
import com.tienda.tienda.user.application.port.output.UserFavoritoOutputPort;
import com.tienda.tienda.user.application.service.FavoritoLoader;
import com.tienda.tienda.user.application.usecase.RemoveFavoritoUseCase;
import com.tienda.tienda.user.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveFavoritoUseCaseTest {

    @Mock private GetUserOutputPort getUserOutputPort;
    @Mock private UserFavoritoOutputPort userFavoritoOutputPort;
    @Mock private FavoritoLoader favoritoLoader;

    @InjectMocks
    private RemoveFavoritoUseCase removeFavoritoUseCase;

    private User testUser() {
        User user = new User();
        user.setId(1);
        user.setName("Alberto");
        user.setCarritoId(1);
        user.setFavoritoIds(List.of(1));
        return user;
    }

    @Test
    void execute_shouldRemoveFavoritoAndReturnUser() {
        User user = testUser();
        User userWithoutFavorito = testUser();
        userWithoutFavorito.setFavoritoIds(List.of());

        when(getUserOutputPort.findById(1)).thenReturn(Mono.just(user));
        when(userFavoritoOutputPort.deleteRelation(1, 1)).thenReturn(Mono.empty());
        when(favoritoLoader.loadFavoritos(any(User.class))).thenReturn(Mono.just(userWithoutFavorito));

        StepVerifier.create(removeFavoritoUseCase.execute(1, 1))
                .expectNextMatches(result -> result.getFavoritoIds().isEmpty())
                .verifyComplete();

        verify(userFavoritoOutputPort, times(1)).deleteRelation(1, 1);
    }

    @Test
    void execute_ifUserNotExists_shouldReturnEmpty() {
        when(getUserOutputPort.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(removeFavoritoUseCase.execute(999, 1))
                .verifyComplete();

        verify(userFavoritoOutputPort, never()).deleteRelation(anyInt(), anyInt());
    }
}