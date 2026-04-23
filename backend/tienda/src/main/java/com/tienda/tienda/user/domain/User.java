package com.tienda.tienda.user.domain;

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

    private String name;
    private String lastname;
    private String username;
    private String email;
    private String password;

    @Column("carrito_id")
    private Integer carritoId;
}
