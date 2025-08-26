package com.bank.controller;

import org.springframework.ui.Model;
import com.bank.model.Account;
import com.bank.model.User;
import com.bank.service.AccountService;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional; // <-- add this

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
public class UserDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public String userDashboard(Authentication authentication, Model model) {
        String username = authentication.getName();

        User user = userService.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Account> accounts = accountService.getUserAccounts(user);

        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
        return "dashboard";  // This is your regular user dashboard
    }

    @PostMapping("/accounts/{id}/suspend")
    public String suspendOwnAccount(@PathVariable Long id,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        String username = authentication.getName();

        Account account = Optional.ofNullable(accountService.getAccountById(id))
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Security check: user can only suspend their own accounts
        if (!account.getUser().getEmail().equals(username)) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/user/dashboard";
        }

        accountService.updateOwnAccountStatus(id, Account.AccountStatus.SUSPENDED, username);
        redirectAttributes.addFlashAttribute("success", "Account suspended successfully");
        return "redirect:/user/dashboard";
    }

    @PostMapping("/accounts/{id}/activate")
    public String activateOwnAccount(@PathVariable Long id,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        String username = authentication.getName();

        Account account = Optional.ofNullable(accountService.getAccountById(id))
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Security check: user can only activate their own accounts
        if (!account.getUser().getEmail().equals(username)) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/user/dashboard";
        }

        accountService.updateOwnAccountStatus(id, Account.AccountStatus.ACTIVE, username);
        redirectAttributes.addFlashAttribute("success", "Account activated successfully");
        return "redirect:/user/dashboard";
    }
}
