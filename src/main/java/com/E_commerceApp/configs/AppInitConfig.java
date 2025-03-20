package com.E_commerceApp.configs;

import com.E_commerceApp.constant.PredefinedRole;
import com.E_commerceApp.models.Role;
import com.E_commerceApp.models.User;
import com.E_commerceApp.repositories.RoleRepository;
import com.E_commerceApp.repositories.UserRepository;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
public class AppInitConfig {
    @NonFinal
    static final String ADMIN_USER_NAME = "admin";
    @NonFinal
    static final String ADMIN_USER_PASSWORD = "admin";
    private static final Logger log = LoggerFactory.getLogger(AppInitConfig.class);
    private final PasswordEncoder passwordEncoder;

    public AppInitConfig(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
//    @ConditionalOnProperty(
//            prefix = "spring",
//            value = "datasource.driver-class-name",
//            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Application started!");
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_USER_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);
            }
        };
    }
}
