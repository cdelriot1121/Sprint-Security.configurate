package com.example.autenticacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PrincipalController {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/principal")
    public String principal() {
        return "Hello World!";
    }

    @GetMapping("/home")
    public String home() {
        return "Pagina Libre";
    }

    @GetMapping("/admin/home")
    public String admin() {
        return "Pagina Admin";
    }

    @GetMapping("/cliente/home")
    public String cliente() {
        return "Pagina Cliente";
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetallesSession() {

        String sessionId = "";
        User userObjtect = null;

        List<Object> sessions = sessionRegistry.getAllPrincipals();

        for (Object session : sessions) {
            if (session instanceof User) {
                userObjtect = (User) session;
            }

            List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(session, false);

            for (SessionInformation sessionInformation : sessionInformations) {
                sessionId = sessionInformation.getSessionId();
            }
        }

        Map<String, Object> response =new HashMap<>();
        response.put("sessionId", sessionId);
        response.put("sessionUser", userObjtect);

        return ResponseEntity.ok(response);
    }
}
