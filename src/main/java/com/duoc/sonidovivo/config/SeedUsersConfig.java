package com.duoc.sonidovivo.config;

import com.duoc.sonidovivo.model.Rol;
import com.duoc.sonidovivo.model.Usuario;
import com.duoc.sonidovivo.repository.UsuarioRepository;
import java.util.Locale;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SeedUsersConfig {

    @Bean
    @ConditionalOnProperty(prefix = "app.seed-users", name = "enabled", havingValue = "true")
    CommandLineRunner seedUsers(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            org.springframework.core.env.Environment environment) {
        return args -> {
            crearSiNoExiste(usuarioRepository, passwordEncoder, environment,
                    "11111111-1", "Administrador", "admin@sonidovivo.cl",
                    "app.seed-users.admin-password", Rol.ADMIN);
            crearSiNoExiste(usuarioRepository, passwordEncoder, environment,
                    "22222222-2", "Vendedor", "vendedor@sonidovivo.cl",
                    "app.seed-users.seller-password", Rol.VENDEDOR);
            crearSiNoExiste(usuarioRepository, passwordEncoder, environment,
                    "33333333-3", "Cliente de prueba", "cliente@sonidovivo.cl",
                    "app.seed-users.customer-password", Rol.CLIENTE);
        };
    }

    private void crearSiNoExiste(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            org.springframework.core.env.Environment environment,
            String run,
            String nombre,
            String email,
            String propiedadPassword,
            Rol rol) {
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            return;
        }
        String password = environment.getProperty(propiedadPassword);
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("Falta configurar la propiedad " + propiedadPassword);
        }
        Usuario usuario = new Usuario();
        usuario.setRun(run);
        usuario.setNombre(nombre);
        usuario.setEmail(email.toLowerCase(Locale.ROOT));
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol(rol);
        usuarioRepository.save(usuario);
    }
}
