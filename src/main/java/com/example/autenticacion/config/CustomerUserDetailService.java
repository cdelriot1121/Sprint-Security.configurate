package com.example.autenticacion.config;

import com.example.autenticacion.entity.Cliente;
import com.example.autenticacion.repository.ClienteRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    public CustomerUserDetailService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("Intentando autenticar al usuario: ".concat(username));

        Cliente cliente = clienteRepository.findByUsername(username).orElseThrow(() -> {
            System.out.println("Usuario no encontrado en la base de datos ".concat(username));
            return new UsernameNotFoundException("Usuario no encontrado");
        });

        System.out.println("Usuario encontrado ".concat(cliente.getUsername()));

        return new User(
                cliente.getUsername(),
                cliente.getPassword(),
                cliente.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
    }
}
