package com.tienda.tienda.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String nombre;
    private String apellidos;
    private String username;
    private String email;
    private String password;
    private Carrito carrito; 
}
