package com.tienda.tienda.lineacarrito.domain;

import com.tienda.tienda.product.infrastructure.adapter.output.persistence.entity.ProductEntity;
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
    private int quantity;

    @Column("carrito_id")
    private Integer carritoId;

    @Column("product_id")
    private Integer productId;

    @Transient
    private ProductEntity productEntity;

}
