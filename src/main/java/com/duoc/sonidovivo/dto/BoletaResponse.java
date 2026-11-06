package com.duoc.sonidovivo.dto;

import com.duoc.sonidovivo.model.Boleta;
import java.time.LocalDateTime;
import java.util.List;

public record BoletaResponse(
        Long id,
        String numero,
        LocalDateTime fecha,
        Integer total,
        Long usuarioId,
        String usuarioNombre,
        List<DetalleBoletaResponse> items) {

    public static BoletaResponse desde(Boleta boleta) {
        return new BoletaResponse(
                boleta.getId(),
                boleta.getNumero(),
                boleta.getFecha(),
                boleta.getTotal(),
                boleta.getUsuario().getId(),
                boleta.getUsuario().getNombre(),
                boleta.getDetalles().stream().map(DetalleBoletaResponse::desde).toList());
    }
}
