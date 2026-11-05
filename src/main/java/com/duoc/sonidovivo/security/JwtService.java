package com.duoc.sonidovivo.security;

import com.duoc.sonidovivo.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String ACCESS = "access";
    private static final String REFRESH = "refresh";

    private final SecretKey signingKey;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs) {
        this.signingKey = crearClave(secret);
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generarAccessToken(Usuario usuario) {
        return generarToken(usuario, ACCESS, accessTokenExpirationMs);
    }

    public String generarRefreshToken(Usuario usuario) {
        return generarToken(usuario, REFRESH, refreshTokenExpirationMs);
    }

    public Optional<String> obtenerUsuario(String token, boolean refresh) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String tipoEsperado = refresh ? REFRESH : ACCESS;
            if (!tipoEsperado.equals(claims.get("type", String.class))) {
                return Optional.empty();
            }
            return Optional.ofNullable(claims.getSubject());
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    public boolean esValidoPara(String token, UserDetails usuario, boolean refresh) {
        return obtenerUsuario(token, refresh)
                .filter(usuario.getUsername()::equalsIgnoreCase)
                .isPresent();
    }

    private String generarToken(Usuario usuario, String tipo, long expiracionMs) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("type", tipo)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusMillis(expiracionMs)))
                .signWith(signingKey)
                .compact();
    }

    private SecretKey crearClave(String secret) {
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        } catch (DecodingException | WeakKeyException ex) {
            throw new IllegalStateException("JWT_SECRET debe ser Base64 y contener al menos 32 bytes", ex);
        }
    }
}
