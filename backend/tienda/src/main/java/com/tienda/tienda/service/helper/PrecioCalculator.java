package com.tienda.tienda.service.helper;

import com.tienda.tienda.model.Product;
import com.tienda.tienda.model.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PrecioCalculator {

    public void recalcularPrecioFinal(Product producto) {
        if (producto.getPromociones() == null || producto.getPromociones().isEmpty()) {
            producto.setPrecioFinal(producto.getPrecio());
            return;
        }
        double maxDescuento = producto.getPromociones().stream()
                .mapToDouble(Promotion::getDescuento)
                .max()
                .orElse(0);
        producto.setPrecioFinal(producto.getPrecio() * (1 - maxDescuento / 100));
    }
}
