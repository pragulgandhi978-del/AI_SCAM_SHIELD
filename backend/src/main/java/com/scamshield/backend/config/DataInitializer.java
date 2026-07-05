package com.scamshield.backend.config;

import com.scamshield.backend.entity.Admin;
import com.scamshield.backend.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds the default admin account on startup using BCrypt (correct hash every time).
 * Password: admin1234  — change in production.
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AdminRepository adminRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AdminRepository adminRepo, PasswordEncoder passwordEncoder) {
        this.adminRepo       = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (adminRepo.findByUsername("admin").isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin1234"));
            adminRepo.save(admin);
            log.info("Default admin account created (username: admin)");
        }
    }
}
