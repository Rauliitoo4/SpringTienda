package com.tienda.userservice.application.usecase;

import com.tienda.userservice.application.port.input.CreateUserInputPort;
import com.tienda.userservice.application.port.output.CreateUserOutputPort;
import com.tienda.userservice.domain.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateUserUseCase implements CreateUserInputPort {

    private final CreateUserOutputPort createUserOutputPort;

    public CreateUserUseCase(CreateUserOutputPort createUserOutputPort) {
        this.createUserOutputPort = createUserOutputPort;
    }

    @Override
    public Mono<User> execute(User user) {
        return createUserOutputPort.save(user);
    }
}