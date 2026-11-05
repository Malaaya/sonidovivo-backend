package com.duoc.sonidovivo.dto;

import com.duoc.sonidovivo.model.Usuario;

public record UsuarioResponse(
        Long id,
        String run,
        String nombre,
        String email,
        String telefono,
        String rol) {

    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getRun(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getRol().name());
    }
}
