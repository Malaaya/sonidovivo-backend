package com.duoc.sonidovivo.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record BoletaRequest(
        @NotEmpty(message = "Debe incluir al menos un producto en la compra")
        List<DetalleBoletaRequest> items) {
}
