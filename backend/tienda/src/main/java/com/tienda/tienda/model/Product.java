package com.tienda.tienda.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;
    private String nombre;
    private double precio;
    private String descripcion;
    private String material;
    private String consideraciones;
    private List<Promotion> promociones;

    public Product(int id, String nombre, Double precio){
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }
}
