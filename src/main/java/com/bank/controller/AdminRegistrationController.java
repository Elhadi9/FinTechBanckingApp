package com.bank.controller;

import com.bank.model.User;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Restrict to admin only
public class AdminRegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showAdminRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", new String[]{"USER", "ADMIN"}); // Available roles
        return "admin/register";
    }

    @PostMapping("/register")
    public String registerAdminUser(@ModelAttribute("user") User user,
                                    @RequestParam String role,
                                    Model model) {
        try {
            user.setRole(role); // Set role from form
            userService.registerUser(user);
            model.addAttribute("successMessage", "User registered successfully!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            model.addAttribute("roles", new String[]{"USER", "ADMIN"});
            return "admin/register";
        }
    }
}