package com.tienda.tienda.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("productos")
public class Product {

    @Id
    private Integer id;

    private String nombre;
    private double precio;

    @Column("precio_final")
    private double precioFinal;

    private String descripcion;
    private String material;
    private String consideraciones;

    @Column("imagen_url")
    private String imagenUrl;

    @Transient
    private List<Promotion> promociones;

}
