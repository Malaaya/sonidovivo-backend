package com.duoc.sonidovivo.dto;

import com.duoc.sonidovivo.model.Categoria;

public record CategoriaResponse(Long id, String nombre, String slug) {

    public static CategoriaResponse desde(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getSlug());
    }
}
