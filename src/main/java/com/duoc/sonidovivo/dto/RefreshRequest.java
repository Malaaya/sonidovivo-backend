package com.duoc.sonidovivo.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "El refresh token es obligatorio")
        String refreshToken) {
}
