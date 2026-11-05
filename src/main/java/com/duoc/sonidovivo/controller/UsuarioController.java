package com.duoc.sonidovivo.controller;

import com.duoc.sonidovivo.dto.UsuarioResponse;
import com.duoc.sonidovivo.exception.ApiException;
import com.duoc.sonidovivo.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Consulta de cuentas")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Solo ADMIN.")
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream().map(UsuarioResponse::desde).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario", description = "Solo ADMIN.")
    public UsuarioResponse obtener(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioResponse::desde)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id " + id));
    }
}
