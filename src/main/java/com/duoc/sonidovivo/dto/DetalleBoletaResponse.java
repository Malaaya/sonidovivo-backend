package com.duoc.sonidovivo.dto;

import com.duoc.sonidovivo.model.DetalleBoleta;

public record DetalleBoletaResponse(
        Long id,
        Long productoId,
        String productoNombre,
        Integer cantidad,
        Integer precioUnitario,
        Integer subtotal) {

    public static DetalleBoletaResponse desde(DetalleBoleta detalle) {
        return new DetalleBoletaResponse(
                detalle.getId(),
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal());
    }
}
