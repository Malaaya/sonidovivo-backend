package com.duoc.sonidovivo.exception;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;

public record ErrorApi(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        Map<String, String> detalles) {

    public static ErrorApi de(HttpStatus estado, String mensaje, Map<String, String> detalles) {
        return new ErrorApi(LocalDateTime.now(), estado.value(), estado.getReasonPhrase(), mensaje, detalles);
    }
}
