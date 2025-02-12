package com.example.autenticacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .build();
    } */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Cross-Site Request Forgery
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/home/**").permitAll()
                        .requestMatchers("api/admin/**").hasRole("ADMIN")
                        .requestMatchers("api/cliente/**").hasRole("CLIENTE")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // ALWAYS - IF_REQUIRED - NEVER - STATELESS
                        .invalidSessionUrl("/login")
                        .sessionFixation(fixation -> fixation
                                .migrateSession()
                        )
                        .sessionConcurrency(concurrency -> concurrency
                                .maximumSessions(1)
                                .expiredUrl("/login")
                                .sessionRegistry(datosSession())
                        )
                )
                .formLogin(from -> from
                        .permitAll()
                        .successHandler(validacionExitosa())
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user = User.withUsername("user")
                .password("{noop}12345") // El {noop} se usa para no encriptar la contraseña
                .roles("ADMIN", "CLIENTE")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin")
                .roles("ADMIN")
                .build();

        UserDetails cliente = User.withUsername("cliente")
                .password("{noop}cliente")
                .roles("CLIENTE")
                .build();

        return new InMemoryUserDetailsManager(user, admin, cliente);
    }

    @Bean
    public SessionRegistry datosSession() {
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler validacionExitosa() {
        return ((request, response, authentication) -> {
            response.sendRedirect("/api/principal");
        });
    }
}
