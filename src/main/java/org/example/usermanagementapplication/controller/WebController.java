package org.example.usermanagementapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Serve the registration form
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";  // Points to register.html
    }

    // Serve the login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // Points to login.html
    }

    // Serve a simple dashboard for authenticated users
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";  // Points to dashboard.html
    }
}