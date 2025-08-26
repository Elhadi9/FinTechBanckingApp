package com.bank.controller;

import com.bank.model.Transaction;
import com.bank.model.TransactionStatus;
import com.bank.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/transactions")
public class AdminTransactionController {

    private final TransactionService transactionService;

    public AdminTransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public String listTransactions(@RequestParam(required = false) TransactionStatus status, Model model) {
        List<Transaction> transactions = (status == null)
                ? transactionService.findAll()
                : transactionService.findByStatus(status);

        model.addAttribute("transactions", transactions);
        model.addAttribute("statuses", TransactionStatus.values());
        return "admin/transactions";
    }

    @PostMapping("/{id}/flag")
    public String flagTransaction(@PathVariable Long id, @RequestParam String reason) {
        transactionService.flagTransaction(id, reason);
        return "redirect:/admin/transactions";
    }

    @PostMapping("/{id}/rollback")
    public String rollbackTransaction(@PathVariable Long id) {
        transactionService.rollbackTransaction(id);
        return "redirect:/admin/transactions";
    }

    @PostMapping("/{id}/approve")
    public String approveTransaction(@PathVariable Long id) {
        transactionService.approveTransaction(id);
        return "redirect:/admin/transactions";
    }
}
