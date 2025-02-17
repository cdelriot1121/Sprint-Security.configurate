package com.example.autenticacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api")
public class PrincipalController {

    @Autowired
    private SessionRegistry sessionRegistry;

    @GetMapping("/principal")
    public String principal() {
        return "principal";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/admin/home")
    public String admin() {
        return "admin";
    }

    @GetMapping("/cliente/home")
    public String cliente() {
        return "cliente";
    }
}


    /* @GetMapping("/session") //METODO PARA VER LAS SESIONES
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
    } */

