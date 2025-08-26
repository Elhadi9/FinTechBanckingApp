package com.bank.repository;

import com.bank.model.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    SystemConfig findByKey(String key);
}