package com.bank.controller;

import com.bank.model.Account;
import com.bank.service.TransactionService;
import com.bank.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    // Constructor injection
    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @PostMapping("/transfer")
    public String transferMoney(
            @RequestParam("senderAccountNumber") String senderAccountNumber,
            @RequestParam("receiverAccountNumber") String receiverAccountNumber,
            @RequestParam("amount") BigDecimal amount) {

        transactionService.transferMoney(senderAccountNumber, receiverAccountNumber, amount);

        return "redirect:/user/dashboard";
    }
}
