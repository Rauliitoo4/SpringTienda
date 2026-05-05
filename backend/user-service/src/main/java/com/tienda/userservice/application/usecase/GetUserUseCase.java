package com.tienda.userservice.application.usecase;

import com.tienda.userservice.application.port.input.GetUserInputPort;
import com.tienda.userservice.application.service.FavoritoLoader;
import com.tienda.userservice.domain.model.User;
import com.tienda.userservice.application.port.output.GetUserOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetUserUseCase implements GetUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final FavoritoLoader favoritoLoader;

    public GetUserUseCase(GetUserOutputPort getUserOutputPort, FavoritoLoader favoritoLoader) {
        this.getUserOutputPort = getUserOutputPort;
        this.favoritoLoader = favoritoLoader;
    }

    public Mono<User> execute(int id) {
        return getUserOutputPort.findById(id)
                .flatMap(favoritoLoader::loadFavoritos);
    }

    public Flux<User> executeAll() {
        return getUserOutputPort.findAll()
                .flatMap(favoritoLoader::loadFavoritos);
    }
}