package com.example.autenticacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.autenticacion.repository.ClienteRepository;

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
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll() 
                            .requestMatchers("/api/home/**").permitAll()
                            .requestMatchers("/api/admin/home").hasRole("ADMIN")
                            .requestMatchers("/api/cliente/home").hasRole("CLIENTE")
                            .requestMatchers("/api/principal").authenticated()
                            .anyRequest().authenticated()
            )
            .exceptionHandling(handling -> handling
                    .accessDeniedPage("/error/403")) 
            .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                            .invalidSessionUrl("/login")
                            .sessionFixation(fixation -> fixation.migrateSession())
                            .sessionConcurrency(concurrency -> concurrency
                                            .maximumSessions(1)
                                            .expiredUrl("/login")
                                            .sessionRegistry(datosSession())
                            )
            )
            .formLogin(from -> from
                            .loginPage("/login")
                            .permitAll()
                            .successHandler(validacionExitosa())
            )
            .build();
    }



    @Bean
    public UserDetailsService userDetailsService(ClienteRepository clienteRepository) {
        return new CustomerUserDetailService(clienteRepository);
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService
    userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(authenticationProvider(userDetailsService, passwordEncoder))
        .build();
    }


    @Bean
    public SessionRegistry datosSession() {
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler validacionExitosa() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().toString();
            
            if (role.contains("ADMIN")) {
                response.sendRedirect("/api/admin/home"); 
            } else if (role.contains("CLIENTE")) {
                response.sendRedirect("/api/cliente/home"); 
            } else {
                response.sendRedirect("/api/principal"); 
            }
        };
    }
    
    
}
