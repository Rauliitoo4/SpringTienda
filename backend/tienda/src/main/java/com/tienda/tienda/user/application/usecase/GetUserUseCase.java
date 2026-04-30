package com.tienda.tienda.user.application.usecase;

import com.tienda.tienda.user.application.port.input.GetUserInputPort;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.application.port.output.GetUserOutputPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GetUserUseCase implements GetUserInputPort {

    private final GetUserOutputPort getUserOutputPort;

    public GetUserUseCase(GetUserOutputPort getUserOutputPort) {
        this.getUserOutputPort = getUserOutputPort;
    }

    public Mono<User> execute(int id) {
        return getUserOutputPort.findById(id);
    }

    public Flux<User> executeAll() {
        return getUserOutputPort.findAll();
    }
}