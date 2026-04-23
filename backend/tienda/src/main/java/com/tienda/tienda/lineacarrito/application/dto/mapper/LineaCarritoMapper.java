package com.tienda.tienda.lineacarrito.application.dto.mapper;
import com.tienda.tienda.lineacarrito.domain.LineaCarrito;
import com.tienda.tienda.lineacarrito.application.dto.LineaCarritoDTO;
import com.tienda.tienda.product.application.dto.mapper.ProductMapper;
import org.springframework.stereotype.Component;

@Component
public class LineaCarritoMapper {

    private final ProductMapper productMapper;

    public LineaCarritoMapper(ProductMapper productMapper){
        this.productMapper = productMapper;
    }

    public LineaCarritoDTO toDTO (LineaCarrito linea) {
        LineaCarritoDTO dto = new LineaCarritoDTO();
        dto.setId(linea.getId());
        dto.setQuantity(linea.getQuantity());
        dto.setSubtotal(linea.getSubtotal());
        dto.setCarritoId(linea.getCarritoId());

        if (linea.getProduct() != null){
            dto.setProduct(productMapper.toDTO(linea.getProduct()));
        }
        return dto;
    }
}
