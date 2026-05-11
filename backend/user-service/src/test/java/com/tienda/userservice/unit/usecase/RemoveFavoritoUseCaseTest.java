package com.tienda.userservice.unit.usecase;

import com.tienda.userservice.application.port.output.GetUserOutputPort;
import com.tienda.userservice.application.port.output.UserFavoritoOutputPort;
import com.tienda.userservice.application.service.FavoritoLoader;
import com.tienda.userservice.application.usecase.RemoveFavoritoUseCase;
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
        user.setEmail("alberto@gmail.com");
        user.setFavoritoIds(List.of());
        return user;
    }

    @Test
    void execute_shouldRemoveFavoritoAndLoadFavoritos() {
        User user = testUser();

        when(getUserOutputPort.findById(1)).thenReturn(Mono.just(user));
        when(userFavoritoOutputPort.deleteRelation(1, 1)).thenReturn(Mono.empty());
        when(favoritoLoader.loadFavoritos(user)).thenReturn(Mono.just(user));

        StepVerifier.create(removeFavoritoUseCase.execute(1, 1))
                .expectNextMatches(result -> result.getId() == 1)
                .verifyComplete();

        verify(userFavoritoOutputPort, times(1)).deleteRelation(1, 1);
        verify(favoritoLoader, times(1)).loadFavoritos(user);
    }

    @Test
    void execute_ifUserNotExists_shouldReturnEmpty() {
        when(getUserOutputPort.findById(99)).thenReturn(Mono.empty());

        StepVerifier.create(removeFavoritoUseCase.execute(99, 1))
                .verifyComplete();

        verify(userFavoritoOutputPort, never()).deleteRelation(anyInt(), anyInt());
    }
}