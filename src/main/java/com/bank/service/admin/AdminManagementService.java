package com.bank.service.admin;

import com.bank.model.User;
import com.bank.model.Account;
import com.bank.repository.UserRepository;
import com.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    // User management methods
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> searchUsers(String searchTerm) {
        return userRepository.findByUsernameContainingOrEmailContaining(searchTerm);
    }

    public void deactivateUser(Long userId) {
        // Implementation for deactivating user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Add deactivation logic here - you might need to add a status field to User entity
        userRepository.save(user);
    }

    // Account management methods
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> getUserAccounts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return accountRepository.findByUser(user);
    }

    public void suspendAccount(Long accountId) {
        // Implementation for suspending account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        // Add suspension logic here - you might need to add a status field to Account entity
        accountRepository.save(account);
    }
}