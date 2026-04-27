package com.tienda.tienda.unit.service;

import com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.entity.CarritoEntity;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.response.UserResponse;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.request.UserRequest;
import com.tienda.tienda.user.infrastructure.adapter.input.rest.data.mapper.UserRestMapper;
import com.tienda.tienda.user.infrastructure.adapter.output.persistence.entity.UserEntity;
import com.tienda.tienda.user.domain.repository.UserRepository;
import com.tienda.tienda.carrito.application.service.CarritoService;
import com.tienda.tienda.user.application.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEntityServiceImplTest {
    
    @Mock
    private UserRepository userRepo;

    @Mock
    private CarritoService carritoService;

    @Mock
    private UserRestMapper userRestMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private UserEntity testingUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("Alberto");
        userEntity.setLastname("García");
        userEntity.setUsername("albertog");
        userEntity.setEmail("albertog@gmail.com");
        userEntity.setPassword("1234");
        userEntity.setCarritoId(1);
        return userEntity;
    }

    private UserResponse testingDto() {
        UserResponse dto = new UserResponse();
        dto.setId(1);
        dto.setName("Alberto");
        dto.setLastname("García");
        dto.setUsername("albertog");
        dto.setEmail("albertog@gmail.com");
        dto.setCarritoId(1);
        return dto;
    }

    @Test
    void getUserById_shouldReturn_User() {
        when(userRepo.findById(1)).thenReturn(Mono.just(testingUser()));
        when(userRestMapper.toDTO(any(UserEntity.class))).thenReturn(testingDto());

        StepVerifier.create(userServiceImpl.getUserById(1))
                .expectNextMatches(dto ->
                        dto.getName().equals("Alberto") &&
                        dto.getLastname().equals("García"))
                .verifyComplete();
    }

    @Test
    void getUserById_ifNotExists_shouldReturnNull() {
        when(userRepo.findById(999)).thenReturn(Mono.empty());

        StepVerifier.create(userServiceImpl.getUserById(999))
                .verifyComplete();
    }

    @Test
    void createUser_shouldReturnAndSaveDTO() {
        CarritoEntity carritoEntity = new CarritoEntity();
        carritoEntity.setId(1);
        carritoEntity.setTotal(0.0);

        UserEntity savedUserEntity = testingUser();

        when(carritoService.createCarrito()).thenReturn(Mono.just(carritoEntity));
        when(userRestMapper.toEntity(any(UserRequest.class))).thenReturn(savedUserEntity);
        when(userRepo.save(any(UserEntity.class))).thenReturn(Mono.just(savedUserEntity));
        when(userRestMapper.toDTO(any(UserEntity.class))).thenReturn(testingDto());

        UserRequest dto = new UserRequest();
        dto.setName("Alberto");
        dto.setLastname("García");
        dto.setUsername("albertog");
        dto.setEmail("albertog@gmail.com");
        dto.setPassword("1234");

        StepVerifier.create(userServiceImpl.createUser(dto))
                .expectNextMatches(result -> result.getName().equals("Alberto"))
                .verifyComplete();
        verify(userRepo, times(1)).save(any(UserEntity.class));
    }

    @Test
    void deleteUser_shouldReturnTrue() {
        when(userRepo.existsById(1)).thenReturn(Mono.just(true));
        when(userRepo.deleteById(1)).thenReturn(Mono.empty());

        StepVerifier.create(userServiceImpl.deleteUser(1))
                .expectNext(true)
                .verifyComplete();
        verify(userRepo, times(1)).deleteById(1);
    }

    @Test
    void deleteUser_ifNotExists_shouldReturnFalse() {
        when(userRepo.existsById(999)).thenReturn(Mono.just(false));

        StepVerifier.create(userServiceImpl.deleteUser(999))
                .expectNext(false)
                .verifyComplete();
        verify(userRepo, never()).deleteById(anyInt());
    }

    @Test
    void updateUser_shouldUpdate_User() {
        UserEntity userEntity = testingUser();
        UserResponse updatedDto = testingDto();
        updatedDto.setUsername("albertitog");

        when(userRepo.findById(1)).thenReturn(Mono.just(userEntity));
        when(userRepo.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));
        when(userRestMapper.toDTO(any(UserEntity.class))).thenReturn(updatedDto);

        UserRequest dto = new UserRequest();
        dto.setUsername("albertitog");

        StepVerifier.create(userServiceImpl.updateUser(1, dto))
                .expectNextMatches(result -> result.getUsername().equals("albertitog"))
                .verifyComplete();
    }
}
