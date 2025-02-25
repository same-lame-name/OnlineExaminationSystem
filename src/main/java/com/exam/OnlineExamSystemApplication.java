package com.exam;

import com.exam.entity.User;
import com.exam.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class OnlineExamSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineExamSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String username = "admin";
            String plainPassword = "password123";
            // Check if admin exists
            User existingAdmin = userRepository.findByUsername(username);
            if (existingAdmin == null) {
                User admin = new User();
                admin.setUsername(username);
                admin.setPassword(passwordEncoder.encode(plainPassword)); // Encrypt "admin123"
                admin.setEmail("admin@example.com");
                admin.setRole("ADMIN");
                userRepository.save(admin);
                System.out.println("Admin user created successfully:");
                System.out.println("Username: " + username);
                System.out.println("Password: " + plainPassword);
                System.out.println("Hashed Password: " + admin.getPassword());
            } else {
                System.out.println("Admin user already exists with username: " + username);
                System.out.println("Stored Hash: " + existingAdmin.getPassword());
            }
        };
    }
}