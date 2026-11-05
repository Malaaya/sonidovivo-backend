package com.duoc.sonidovivo.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        UsuarioResponse usuario) {
}
