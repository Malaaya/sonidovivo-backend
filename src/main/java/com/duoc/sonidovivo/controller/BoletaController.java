package com.duoc.sonidovivo.controller;

import com.duoc.sonidovivo.dto.BoletaRequest;
import com.duoc.sonidovivo.dto.BoletaResponse;
import com.duoc.sonidovivo.model.Usuario;
import com.duoc.sonidovivo.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boletas")
@RequiredArgsConstructor
@Tag(name = "Boletas", description = "Gestión de compras y ventas")
public class BoletaController {

    private final BoletaService boletaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear boleta", description = "Permite a un cliente crear una compra")
    public BoletaResponse crear(@Valid @RequestBody BoletaRequest request,
            @AuthenticationPrincipal Usuario usuario) {
        return boletaService.crear(request, usuario);
    }

    @GetMapping("/mis-compras")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Mis compras", description = "Lista las boletas del cliente autenticado")
    public List<BoletaResponse> misCompras(@AuthenticationPrincipal Usuario usuario) {
        return boletaService.misCompras(usuario);
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar boletas", description = "Solo ADMIN y VENDEDOR")
    public List<BoletaResponse> listar() {
        return boletaService.listar();
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Detalle de boleta", description = "Cliente solo puede ver sus propias boletas")
    public BoletaResponse obtener(@PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {
        return boletaService.obtener(id, usuario);
    }
}
