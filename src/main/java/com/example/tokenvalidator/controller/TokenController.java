package com.example.tokenvalidator.controller;

import com.example.tokenvalidator.config.TokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TokenController {

    @Autowired
    private TokenConfig tokenConfig;

    @GetMapping("/")
    public String validateToken(Model model) {
        boolean isValid = tokenConfig.isTokenValid();
        model.addAttribute("isValid", isValid);
        return "index";
    }
} 