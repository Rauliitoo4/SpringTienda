package com.tienda.tienda.user.infrastructure.adapter.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("usuario_favoritos")
public class UserFavoritoEntity {

    @Column("user_id")
    private Integer userId;

    @Column("product_id")
    private Integer productId;
}
