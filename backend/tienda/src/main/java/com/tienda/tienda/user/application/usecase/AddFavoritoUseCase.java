package com.tienda.tienda.user.application.usecase;

import com.tienda.tienda.user.application.port.input.AddFavoritoInputPort;
import com.tienda.tienda.user.application.port.output.GetUserOutputPort;
import com.tienda.tienda.user.application.port.output.UserFavoritoOutputPort;
import com.tienda.tienda.user.application.service.FavoritoLoader;
import com.tienda.tienda.user.domain.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AddFavoritoUseCase implements AddFavoritoInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final UserFavoritoOutputPort userFavoritoOutputPort;
    private final FavoritoLoader favoritoLoader;

    public AddFavoritoUseCase(GetUserOutputPort getUserOutputPort, UserFavoritoOutputPort userFavoritoOutputPort, FavoritoLoader favoritoLoader) {
        this.getUserOutputPort = getUserOutputPort;
        this.userFavoritoOutputPort = userFavoritoOutputPort;
        this.favoritoLoader = favoritoLoader;
    }

    public Mono<User> execute(int userId, int productId) {
        return getUserOutputPort.findById(userId)
                .flatMap(user -> userFavoritoOutputPort.existsRelation(userId, productId)
                        .flatMap(count -> {
                            if (count > 0) return favoritoLoader.loadFavoritos(user);
                            return userFavoritoOutputPort.insertRelation(userId, productId)
                                    .then(favoritoLoader.loadFavoritos(user));
                        })
                );
    }
}
