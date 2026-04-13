package com.tienda.tienda.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("usuarios")
public class User {

    @Id
    private Integer id;

    private String nombre;
    private String apellidos;
    private String username;
    private String email;
    private String password;

    @Column("carrito_id")
    private Integer carritoId;
}
