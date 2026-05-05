package com.tienda.productservice.infrastructure.adapter.output.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("productos")
public class ProductEntity {

    @Id
    private Integer id;

    private String name;
    private double price;

    @Column("final_price")
    private double finalPrice;

    private String description;
    private String material;
    private String considerations;

    @Column("image_url")
    private String imageUrl;

    private String category;

    @Column("created_at")
    private java.time.LocalDateTime createdAt;

}
