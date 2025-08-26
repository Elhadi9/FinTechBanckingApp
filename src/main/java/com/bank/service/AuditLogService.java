package com.bank.service;

import com.bank.model.AuditLog;
import com.bank.model.User;
import com.bank.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private UserService userService; // Inject UserService to fetch User by email

    public void logAction(String action, String performedByEmail, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        // Fetch User by email
        User user = userService.findByEmail(performedByEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + performedByEmail));
        log.setUser(user); // Set User object instead of performedBy
        log.setTimestamp(LocalDateTime.now());
        log.setDetails(details);
        auditLogRepository.save(log);
    }

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }
}