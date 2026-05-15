package com.tienda.userservice.application.usecase;

import com.tienda.userservice.application.port.input.CreateUserInputPort;
import com.tienda.userservice.application.port.output.CreateCarritoOutputPort;
import com.tienda.userservice.application.port.output.CreateKeycloakUserOutputPort;
import com.tienda.userservice.application.port.output.CreateUserOutputPort;
import com.tienda.userservice.domain.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateUserUseCase implements CreateUserInputPort {

    private final CreateUserOutputPort createUserOutputPort;
    private final CreateCarritoOutputPort createCarritoOutputPort;
    private final CreateKeycloakUserOutputPort createKeycloakUserOutputPort;

    public CreateUserUseCase(CreateUserOutputPort createUserOutputPort,
                             CreateCarritoOutputPort createCarritoOutputPort,
                             CreateKeycloakUserOutputPort createKeycloakUserOutputPort) {
        this.createUserOutputPort = createUserOutputPort;
        this.createCarritoOutputPort = createCarritoOutputPort;
        this.createKeycloakUserOutputPort = createKeycloakUserOutputPort;
    }

    @Override
    public Mono<User> execute(User user) {
        return createCarritoOutputPort.create()
                .flatMap(carrito -> {
                    user.setCarritoId(carrito.getId());
                    return createKeycloakUserOutputPort.create(user)
                            .then(createUserOutputPort.save(user));
                });
    }
}