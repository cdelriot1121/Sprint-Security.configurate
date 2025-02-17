package com.example.autenticacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
    @RequestMapping("/error/403")
    public String handle403() {
        return "error/403";  
    }

}
