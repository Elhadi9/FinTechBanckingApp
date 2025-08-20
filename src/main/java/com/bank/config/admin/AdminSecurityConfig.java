package com.bank.config.admin;

import com.bank.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSecurityConfig {

    @Autowired
    private AdminService adminService;  // ✅ use AdminService here

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(adminService); // ✅ not userService
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }
}
