package com.tienda.tienda.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("carritos")
public class Carrito {

    @Id
    private Integer id;
    private double total;

    @Transient
    private List<LineaCarrito> lineas;
}
