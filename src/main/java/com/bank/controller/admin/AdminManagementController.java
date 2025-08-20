package com.bank.controller.admin;

import com.bank.model.User;
import com.bank.model.Account;
import com.bank.service.admin.AdminManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/management")
public class AdminManagementController {

    @Autowired
    private AdminManagementService adminManagementService;

    @GetMapping("/users")
    public String listUsers(Model model,
                            @RequestParam(required = false) String search) {
        List<User> users;
        if (search != null && !search.trim().isEmpty()) {
            users = adminManagementService.searchUsers(search);
        } else {
            users = adminManagementService.getAllUsers();
        }

        model.addAttribute("users", users);
        model.addAttribute("searchTerm", search);
        return "admin/users";
    }

    @GetMapping("/users/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = adminManagementService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Account> accounts = adminManagementService.getUserAccounts(id);

        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
        return "admin/user-details";
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id) {
        adminManagementService.deactivateUser(id);
        return "redirect:/admin/management/users";
    }

    @GetMapping("/accounts")
    public String listAccounts(Model model) {
        List<Account> accounts = adminManagementService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "admin/accounts";
    }

    @PostMapping("/accounts/{id}/suspend")
    public String suspendAccount(@PathVariable Long id) {
        adminManagementService.suspendAccount(id);
        return "redirect:/admin/management/accounts";
    }
}