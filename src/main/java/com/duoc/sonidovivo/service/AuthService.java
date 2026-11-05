package com.duoc.sonidovivo.service;

import com.duoc.sonidovivo.dto.AuthResponse;
import com.duoc.sonidovivo.dto.LoginRequest;
import com.duoc.sonidovivo.dto.RefreshRequest;
import com.duoc.sonidovivo.dto.RegistroRequest;
import com.duoc.sonidovivo.dto.UsuarioResponse;
import com.duoc.sonidovivo.exception.ApiException;
import com.duoc.sonidovivo.model.Rol;
import com.duoc.sonidovivo.model.Usuario;
import com.duoc.sonidovivo.repository.UsuarioRepository;
import com.duoc.sonidovivo.security.JwtService;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        String email = normalizarEmail(request.email());
        String run = request.run().trim().toUpperCase(Locale.ROOT);
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe una cuenta con ese correo");
        }
        if (usuarioRepository.existsByRun(run)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe una cuenta con ese RUN");
        }
        Usuario usuario = new Usuario();
        usuario.setRun(run);
        usuario.setNombre(request.nombre().trim());
        usuario.setEmail(email);
        usuario.setTelefono(request.telefono() == null || request.telefono().isBlank()
                ? null
                : request.telefono().trim());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRol(Rol.CLIENTE);
        return crearRespuesta(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public AuthResponse iniciarSesion(LoginRequest request) {
        String email = normalizarEmail(request.email());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.password()));
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return crearRespuesta(usuario);
    }

    @Transactional(readOnly = true)
    public AuthResponse renovar(RefreshRequest request) {
        String email = jwtService.obtenerUsuario(request.refreshToken(), true)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED,
                        "Refresh token inválido o expirado"));
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED,
                        "El usuario del refresh token ya no existe"));
        UserDetails detalles = usuario;
        if (!jwtService.esValidoPara(request.refreshToken(), detalles, true)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Refresh token inválido o expirado");
        }
        return crearRespuesta(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse perfil(Usuario usuario) {
        return UsuarioResponse.desde(usuario);
    }

    private AuthResponse crearRespuesta(Usuario usuario) {
        return new AuthResponse(
                jwtService.generarAccessToken(usuario),
                jwtService.generarRefreshToken(usuario),
                "Bearer",
                UsuarioResponse.desde(usuario));
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
