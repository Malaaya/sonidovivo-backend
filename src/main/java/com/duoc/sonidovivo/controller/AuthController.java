package com.duoc.sonidovivo.controller;

import com.duoc.sonidovivo.dto.AuthResponse;
import com.duoc.sonidovivo.dto.LoginRequest;
import com.duoc.sonidovivo.dto.RefreshRequest;
import com.duoc.sonidovivo.dto.RegistroRequest;
import com.duoc.sonidovivo.dto.UsuarioResponse;
import com.duoc.sonidovivo.model.Usuario;
import com.duoc.sonidovivo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro, inicio de sesión y tokens de acceso")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar cliente", description = "Crea una cuenta con rol CLIENTE.")
    public AuthResponse registrar(@Valid @RequestBody RegistroRequest request) {
        return authService.registrar(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Devuelve access token y refresh token.")
    public AuthResponse iniciarSesion(@Valid @RequestBody LoginRequest request) {
        return authService.iniciarSesion(request);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar tokens", description = "Valida el refresh token y entrega un nuevo par de tokens.")
    public AuthResponse renovar(@Valid @RequestBody RefreshRequest request) {
        return authService.renovar(request);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Ver perfil", description = "Devuelve los datos de la cuenta autenticada.")
    public UsuarioResponse perfil(@AuthenticationPrincipal Usuario usuario) {
        return authService.perfil(usuario);
    }
}
