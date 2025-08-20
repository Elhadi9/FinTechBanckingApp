package com.bank.controller.admin;

import com.bank.model.admin.Admin;
import com.bank.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String showAdminLoginForm() {
        return "admin/login";
    }

//    @PostMapping("/login")
//    public String processAdminLogin(@RequestParam String username,
//                                    @RequestParam String password,
//                                    RedirectAttributes redirectAttributes) {
//        try {
//            UserDetails admin = adminService.loadUserByUsername(username);
//
//            // Create authentication token
//            Authentication authentication = new UsernamePasswordAuthenticationToken(
//                    admin, null, admin.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            return "redirect:/admin/dashboard";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Invalid admin credentials");
//            return "redirect:/admin/login";
//        }
//    }

    @GetMapping("/register")
    public String showAdminRegistrationForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/register";
    }

    @PostMapping("/register")
    public String registerAdmin(@ModelAttribute("admin") Admin admin,
                                RedirectAttributes redirectAttributes) {
        try {
            adminService.registerAdmin(admin);
            redirectAttributes.addFlashAttribute("successMessage", "Admin registered successfully!");
            return "redirect:/admin/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/admin/register";
        }
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        // Check if user has admin role
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "admin/dashboard";
        }
        return "redirect:/admin/login";
    }

    @PostMapping("/logout")
    public String adminLogout() {
        SecurityContextHolder.clearContext();
        return "redirect:/admin/login?logout=true";
    }
}