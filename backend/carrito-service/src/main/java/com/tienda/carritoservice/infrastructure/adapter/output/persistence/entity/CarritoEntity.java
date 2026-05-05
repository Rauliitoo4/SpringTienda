package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("carritos")
public class CarritoEntity {

    @Id
    private Integer id;
    private double total;
}
