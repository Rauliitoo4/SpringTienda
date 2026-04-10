package com.tienda.tienda.repository;

import com.tienda.tienda.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Integer>{
    
}
