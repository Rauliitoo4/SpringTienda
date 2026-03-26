package com.tienda.tienda.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String nombre;
    private String apellidos;
    private String username;
    private String email;
    private String password;
    private CarritoDTO carrito; 
}
