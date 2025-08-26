package com.bank.service;

import com.bank.model.SystemConfig;
import com.bank.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SystemConfigService {

    @Autowired
    private SystemConfigRepository configRepository;

    public SystemConfig updateConfig(String key, String value, String description) {
        SystemConfig config = configRepository.findByKey(key);
        if (config == null) {
            config = new SystemConfig();
            config.setKey(key);
        }
        config.setValue(value);
        config.setDescription(description);
        return configRepository.save(config);
    }

    public List<SystemConfig> getAllConfigs() {
        return configRepository.findAll();
    }
}