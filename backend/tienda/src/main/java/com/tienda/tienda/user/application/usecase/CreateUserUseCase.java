package com.tienda.tienda.user.application.usecase;

import com.tienda.tienda.user.domain.model.User;
import com.tienda.tienda.user.domain.repository.CreateUserRepository;
import com.tienda.tienda.carrito.application.usecase.CreateCarritoUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateUserUseCase {

    private final CreateUserRepository createUserRepository;
    private final CreateCarritoUseCase createCarritoUseCase;

    public CreateUserUseCase(CreateUserRepository createUserRepository, CreateCarritoUseCase createCarritoUseCase) {
        this.createUserRepository = createUserRepository;
        this.createCarritoUseCase = createCarritoUseCase;
    }

    public Mono<User> execute(User user) {
        return createCarritoUseCase.execute()
                .flatMap(carrito -> {
                    user.setCarritoId(carrito.getId());
                    return createUserRepository.save(user);
                });
    }
}
