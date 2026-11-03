package com.duoc.sonidovivo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
        String nombre,

        @NotBlank(message = "El slug es obligatorio")
        @Size(max = 40, message = "El slug no puede superar los 40 caracteres")
        String slug) {
}
