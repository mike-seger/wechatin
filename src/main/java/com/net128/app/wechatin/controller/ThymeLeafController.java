package com.net128.app.wechatin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThymeLeafController {
    @Value("${spring.application.name}")
    private String appName;

    @Value("${wechatin.qr:}")
    private String qr;
 
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("qr", qr);
        return "index";
    }
}