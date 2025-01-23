// H2 Configuration Adjustment for Spring Boot

package com.att.acceptance.movie_theater.config;

import com.att.acceptance.movie_theater.entity.User;
import com.att.acceptance.movie_theater.entity.RoleEnum;
import com.att.acceptance.movie_theater.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2SetupConfig {

    @Bean
    CommandLineRunner initDatabase(UserService userService) {
        return args -> {
            // Check if any user with admin role exists
            boolean adminExists = userService.getUserById(1L).getRoles().contains(RoleEnum.ROLE_ADMIN);

            if (!adminExists) {
                User admin = new User();
                admin.setName("Admin User");
                admin.setEmail("admin@example.com");
                admin.setPassword("password"); // Ideally, passwords should be hashed
                admin.addRole(RoleEnum.ROLE_ADMIN);
                userService.registerUser(admin, RoleEnum.ROLE_ADMIN);
            }
        };
    }
}
