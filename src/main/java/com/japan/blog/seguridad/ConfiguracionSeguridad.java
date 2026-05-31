package com.japan.blog.seguridad;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    @Bean
    public UserDetailsManager usersCustom(DataSource dataSource) {
        JdbcUserDetailsManager usuario = new JdbcUserDetailsManager(dataSource);
        // sacamos usuario y pass de la bd, el 1 al final es enabled (siempre activo)
        usuario.setUsersByUsernameQuery("SELECT username, password, 1 FROM usuarios WHERE username = ?");
        //  sacamos tambien sus roles
        usuario.setAuthoritiesByUsernameQuery(
            "SELECT u.username, r.rol FROM usuarios_roles ur " +
            "INNER JOIN usuarios u ON u.idusuario = ur.idusuario " +
            "INNER JOIN roles r ON r.idrol = ur.idrol " +
            "WHERE u.username = ?");
        return usuario;
    }

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // recursos publicos
                .requestMatchers("/images/**", "/bootstrap/**", "/imagenes/**", "/fotos/**", "/css/**", "/js/**", "/webjars/**").permitAll()
                .requestMatchers("/", "/login", "/usuarios/signup", "/usuarios/save", "/articulos/view/**").permitAll()

                // solo admin
                .requestMatchers("/usuarios/listar", "/usuarios/crear", "/usuarios/guardar",
                    "/usuarios/editar/**", "/usuarios/actualizar", "/usuarios/eliminar/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/marcas/**").hasAuthority("ROLE_ADMIN")

                // articulos: borrar solo admin, crear/editar admin y autor
                .requestMatchers("/articulos/eliminar/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/articulos/listar", "/articulos/crear", "/articulos/guardar",
                    "/articulos/editar/**", "/articulos/actualizar").hasAnyAuthority("ROLE_ADMIN", "ROLE_AUTOR")

                // comentarios: escribir todos, gestionar solo admin
                .requestMatchers("/comentarios/guardar/**").authenticated()
                .requestMatchers("/comentarios/eliminar/**", "/comentarios/editar/**").hasAuthority("ROLE_ADMIN")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
