package com.tienda.tienda.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lineas_carrito")
public class LineaCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double subtotal;
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Product producto;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;
}
