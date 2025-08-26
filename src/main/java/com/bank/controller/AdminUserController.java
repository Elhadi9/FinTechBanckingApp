package com.bank.controller;

import com.bank.model.*;
import com.bank.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private SystemConfigService configService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        try {
            // Get all users and accounts from the database
            List<User> users = userService.getAllUsers();
            List<Account> accounts = accountService.getAllAccounts();

            // Calculate statistics dynamically
            long totalUsers = users.size();
            long totalAccounts = accounts.size();

            // Calculate suspended users count
            long suspendedUsers = users.stream()
                    .filter(user -> user.getStatus() == User.UserStatus.SUSPENDED)
                    .count();

            // Calculate suspended accounts count
            long suspendedAccounts = accounts.stream()
                    .filter(account -> account.getStatus() == Account.AccountStatus.SUSPENDED)
                    .count();

            // Add statistics to the model
            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("totalAccounts", totalAccounts);
            model.addAttribute("suspendedUsers", suspendedUsers);
            model.addAttribute("suspendedAccounts", suspendedAccounts);

            // Add recent users and accounts for the tables
            model.addAttribute("users", users.stream().limit(5).collect(Collectors.toList()));
            model.addAttribute("accounts", accounts.stream().limit(5).collect(Collectors.toList()));

        } catch (Exception e) {
            logger.error("Error loading dashboard statistics: ", e);
            // Set default values in case of error
            model.addAttribute("totalUsers", 0);
            model.addAttribute("totalAccounts", 0);
            model.addAttribute("suspendedUsers", 0);
            model.addAttribute("suspendedAccounts", 0);
            model.addAttribute("users", new ArrayList<>());
            model.addAttribute("accounts", new ArrayList<>());
            model.addAttribute("errorMessage", "Unable to load dashboard statistics: " + e.getMessage());
        }
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model,
                            @RequestParam(required = false) String username,
                            @RequestParam(required = false) String email,
                            @RequestParam(required = false) User.UserStatus status) {
        try {
            List<User> users;
            if (username != null || email != null || status != null) {
                users = userService.searchUsers(username, email, status);
            } else {
                users = userService.getAllUsers();
            }
            model.addAttribute("users", users);
            model.addAttribute("statuses", User.UserStatus.values());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Unable to load users: " + e.getMessage());
            model.addAttribute("users", new ArrayList<>());
            model.addAttribute("statuses", User.UserStatus.values());
        }
        return "admin/users";  // Return admin users page
    }

    @GetMapping("/accounts")
    public String listAccounts(Model model,
                               @RequestParam(required = false) String accountNumber,
                               @RequestParam(required = false) Account.AccountStatus status) {
        try {
            List<Account> accounts;
            if (accountNumber != null || status != null) {
                accounts = accountService.searchAccounts(accountNumber, status);
            } else {
                accounts = accountService.getAllAccounts();
            }
            model.addAttribute("accounts", accounts != null ? accounts : new ArrayList<>());
            model.addAttribute("statuses", Account.AccountStatus.values());
        } catch (Exception e) {
            logger.error("Error in listAccounts: ", e);
            model.addAttribute("errorMessage", "Unable to load accounts: " + e.getMessage());
            model.addAttribute("accounts", new ArrayList<>());
            model.addAttribute("statuses", Account.AccountStatus.values());
        }
        return "admin/accounts";
    }

    @GetMapping("/roles")
    public String listRoles(Model model) {
        try {
            model.addAttribute("roles", roleService.getAllRoles() != null ? roleService.getAllRoles() : new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error in listRoles: ", e);
            model.addAttribute("errorMessage", "Unable to load roles: " + e.getMessage());
            model.addAttribute("roles", new ArrayList<>());
        }
        return "admin/roles";
    }

    @GetMapping("/configs")
    public String listConfigs(Model model) {
        try {
            model.addAttribute("configs", configService.getAllConfigs() != null ? configService.getAllConfigs() : new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error in listConfigs: ", e);
            model.addAttribute("errorMessage", "Unable to load configs: " + e.getMessage());
            model.addAttribute("configs", new ArrayList<>());
        }
        return "admin/configs";
    }

    @GetMapping("/audit-logs")
    public String listAuditLogs(Model model) {
        try {
            model.addAttribute("auditLogs", auditLogService.getAllAuditLogs() != null ? auditLogService.getAllAuditLogs() : new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error in listAuditLogs: ", e);
            model.addAttribute("errorMessage", "Unable to load audit logs: " + e.getMessage());
            model.addAttribute("auditLogs", new ArrayList<>());
        }
        return "admin/audit-logs";
    }

    @GetMapping("/export/users")
    public ResponseEntity<byte[]> exportUsers() {
        try {
            List<User> users = userService.getAllUsers();
            String csv = "ID,Username,Email,Status,Role\n" +
                    users.stream()
                            .map(u -> String.format("%d,%s,%s,%s,%s",
                                    u.getId(), u.getUsername(), u.getEmail(),
                                    u.getStatus() != null ? u.getStatus() : "UNKNOWN",
                                    u.getRole() != null ? u.getRole() : "UNKNOWN"))
                            .collect(Collectors.joining("\n"));
            byte[] csvBytes = csv.getBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=users.csv");
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in exportUsers: ", e);
            return new ResponseEntity<>(
                    ("Error exporting users: " + e.getMessage()).getBytes(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/export/transactions")
    public ResponseEntity<byte[]> exportTransactions() {
        try {
            List<Transaction> transactions = transactionService.findAll();
            String csv = "ID,Account ID,Amount,Timestamp,Status,Description\n" +
                    transactions.stream()
                            .map(t -> String.format("%d,%d,%d,%s,%s,%s,%s",
                                    t.getId(),
                                    t.getSenderAccount() != null ? t.getSenderAccount().getId() : null,
                                    t.getReceiverAccount() != null ? t.getReceiverAccount().getId() : null,
                                    t.getAmount(),
                                    t.getTimestamp(),
                                    t.getStatus(),
                                    t.getDescription()))
                            .collect(Collectors.joining("\n"));
            byte[] csvBytes = csv.getBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=transactions.csv");
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error in exportTransactions: ", e);
            return new ResponseEntity<>(
                    ("Error exporting transactions: " + e.getMessage()).getBytes(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{id}/status")
    public String updateUserStatus(@PathVariable("id") Long id,
                                   @RequestParam("status") String status,
                                   RedirectAttributes redirectAttributes) {
        try {
            User.UserStatus userStatus = User.UserStatus.valueOf(status);
            userService.updateUserStatus(id, userStatus);
            redirectAttributes.addFlashAttribute("success", "User status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user status: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
