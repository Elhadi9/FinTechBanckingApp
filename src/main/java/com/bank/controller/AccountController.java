package com.bank.controller;

import com.bank.model.Account;
import com.bank.model.User;
import com.bank.service.AccountService;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<User> userOpt = userService.getUserByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Account> accounts = accountService.getUserAccounts(user);

            model.addAttribute("user", user);
            model.addAttribute("accounts", accounts);
        }

        return "dashboard";
    }

    @PostMapping("/createAccount")
    public String createAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<User> userOpt = userService.getUserByEmail(email);
        if (userOpt.isPresent()) {
            accountService.createAccount(userOpt.get());
        }

        return "redirect:/dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accountNumber,
                          @RequestParam BigDecimal amount) {
        accountService.deposit(accountNumber, amount);
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accountNumber,
                           @RequestParam BigDecimal amount) {
        accountService.withdraw(accountNumber, amount);
        return "redirect:/dashboard";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAccount,
                           @RequestParam String toAccount,
                           @RequestParam BigDecimal amount) {
        accountService.transfer(fromAccount, toAccount, amount);
        return "redirect:/dashboard";
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestParam String accountNumber,
                                @RequestParam(required = false) String transferToAccount,
                                Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<User> userOpt = userService.getUserByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Account> accounts = accountService.getUserAccounts(user);
            Optional<Account> accountOpt = accountService.getAccountByNumber(accountNumber);
            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                // account has money but no transfer account given
                if (account.getBalance().compareTo(BigDecimal.ZERO) > 0 &&
                        (transferToAccount == null || transferToAccount.isEmpty())) {

                    model.addAttribute("user", user);
                    model.addAttribute("accounts", accounts);
                    model.addAttribute("error", "Account has money, please transfer it first to another account!");
                    return "dashboard";
                }
                // prevent deleting last account
                if (accounts.size() <= 1) {
                    model.addAttribute("user", user);
                    model.addAttribute("accounts", accounts);
                    model.addAttribute("error", "You cannot delete your only account.");
                    return "dashboard";
                }
                // if balance exists, transfer before deletion
                if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                    accountService.transfer(accountNumber, transferToAccount, account.getBalance());
                }
                accountService.deleteAccount(accountNumber);
            }
        }
        return "redirect:/dashboard";
    }
}