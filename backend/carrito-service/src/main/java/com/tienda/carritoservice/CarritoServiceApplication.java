package com.tienda.carritoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class CarritoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarritoServiceApplication.class, args);
    }

}
