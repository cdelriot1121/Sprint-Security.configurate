package com.example.autenticacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginMapping {
    @GetMapping("/login")
    public String Login(){
        return "login";
    }
    
}
