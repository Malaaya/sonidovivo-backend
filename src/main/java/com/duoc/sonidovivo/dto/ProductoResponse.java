package com.duoc.sonidovivo.dto;

import com.duoc.sonidovivo.model.Producto;

public record ProductoResponse(
        Long id,
        String codigo,
        String nombre,
        String marca,
        String modelo,
        String descripcion,
        Integer precio,
        Integer stock,
        String imagen,
        Long categoriaId,
        String categoriaNombre) {

    public static ProductoResponse desde(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getMarca(),
                producto.getModelo(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getImagen(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre());
    }
}
