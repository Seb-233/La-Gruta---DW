package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminDashboard(HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/la_gruta/login";
        }
        return "admin-dashboard"; // templates/admin-dashboard.html
    }
}

