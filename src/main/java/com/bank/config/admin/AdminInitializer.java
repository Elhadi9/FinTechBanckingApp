package com.bank.config.admin;

import com.bank.model.admin.Admin;
import com.bank.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminInitializer {

    @Autowired
    private AdminService adminService;

    @Bean
    public ApplicationRunner initializeAdmin() {
        return args -> {
            // Create default admin account if none exists
            if (adminService.getAdminByUsername("admin").isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                admin.setPassword("admin123"); // Will be encoded
                admin.setEmail("admin@bank.com");
                adminService.registerAdmin(admin);
                System.out.println("Default admin account created: admin/admin123");
            }
        };
    }
}