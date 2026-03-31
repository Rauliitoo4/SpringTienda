package com.tienda.tienda.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productos")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private double precio;
    private double precioFinal;
    private String descripcion;
    private String material;
    private String consideraciones;
    private String imagenUrl;

    @ManyToMany
    @JoinTable(
        name = "producto_promocion",
        joinColumns = @JoinColumn(name = "producto_id"),
        inverseJoinColumns = @JoinColumn(name = "promocion_id")
    )
    private List<Promotion> promociones;

}
