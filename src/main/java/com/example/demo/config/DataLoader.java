package com.example.demo.config;

import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {

        if (roleRepository.findByName("CLIENTE").isEmpty()) {
            roleRepository.save(Role.builder().name("CLIENTE").build());
        }

        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(Role.builder().name("ADMIN").build());
        }

        if (roleRepository.findByName("OPERADOR").isEmpty()) {
            roleRepository.save(Role.builder().name("OPERADOR").build());
        }
    }
}
