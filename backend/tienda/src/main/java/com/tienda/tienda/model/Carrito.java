package com.tienda.tienda.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double total;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL)
    private List<LineaCarrito> lineas;
}
