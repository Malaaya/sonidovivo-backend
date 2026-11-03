package com.duoc.sonidovivo.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorApi> noEncontrado(RecursoNoEncontradoException ex) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorApi> conflicto(ConflictoException ex) {
        return responder(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorApi> reglaNegocio(ReglaNegocioException ex) {
        return responder(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApi> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.put(e.getField(), e.getDefaultMessage()));
        return responder(HttpStatus.BAD_REQUEST, "Datos inválidos", errores);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorApi> cuerpoInvalido(HttpMessageNotReadableException ex) {
        return responder(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es válido", null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorApi> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return responder(HttpStatus.BAD_REQUEST, "El parámetro '" + ex.getName() + "' tiene un valor inválido", null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorApi> integridad(DataIntegrityViolationException ex) {
        return responder(HttpStatus.CONFLICT,
                "La operación no se puede realizar porque el registro está en uso o duplica datos existentes", null);
    }

    private ResponseEntity<ErrorApi> responder(HttpStatus estado, String mensaje, Map<String, String> detalles) {
        return ResponseEntity.status(estado).body(ErrorApi.de(estado, mensaje, detalles));
    }
}
