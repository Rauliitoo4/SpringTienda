package com.tienda.userservice.infrastructure.adapter.output.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("usuarios")
public class UserEntity {

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
