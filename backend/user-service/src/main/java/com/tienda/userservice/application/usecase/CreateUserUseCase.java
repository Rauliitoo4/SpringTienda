package com.tienda.userservice.application.usecase;

import com.tienda.tienda.user.application.port.input.CreateUserInputPort;
import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.application.port.output.CreateUserOutputPort;
import com.tienda.tienda.carrito.application.usecase.CreateCarritoUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateUserUseCase implements CreateUserInputPort {

    private final CreateUserOutputPort createUserOutputPort;
    private final CreateCarritoUseCase createCarritoUseCase;

    public CreateUserUseCase(CreateUserOutputPort createUserOutputPort, CreateCarritoUseCase createCarritoUseCase) {
        this.createUserOutputPort = createUserOutputPort;
        this.createCarritoUseCase = createCarritoUseCase;
    }

    @Override
    public Mono<User> execute(User user) {
        return createCarritoUseCase.execute()
                .flatMap(carrito -> {
                    user.setCarritoId(carrito.getId());
                    return createUserOutputPort.save(user);
                });
    }
}
