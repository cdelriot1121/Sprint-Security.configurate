package com.example.autenticacion.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String password = "cliente";
        String encoder = passwordEncoder.encode(password);

        System.out.println("La contraseña encriptada es: ".concat(encoder));
    }
}
