package com.tienda.tienda.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("lineas_carrito")
public class LineaCarrito {

    @Id
    private Integer id;

    private double subtotal;
    private int cantidad;

    @Column("carrito_id")
    private Integer carritoId;

    @Column("producto_id")
    private Integer productoId;

    @Transient
    private Product producto;

}
