package com.tienda.tienda.carrito.infrastructure.adapter.output.persistence.entity;

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
@Table("lineas_carrito")
public class LineaCarritoEntity {

    @Id
    private Integer id;

    private double subtotal;
    private int quantity;
    private String size;

    @Column("carrito_id")
    private Integer carritoId;

    @Column("product_id")
    private Integer productId;
}
