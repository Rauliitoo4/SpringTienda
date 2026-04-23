package com.tienda.tienda.promotion.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("promociones")
public class Promotion {

    @Id
    private Integer id;

    private double discount;
    private String description;
}
