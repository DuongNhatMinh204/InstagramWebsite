package com.nminh.websiteinstagram.config;

import com.nminh.websiteinstagram.entity.User;
import com.nminh.websiteinstagram.enums.Role;
import com.nminh.websiteinstagram.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner init(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByPhone("111").isEmpty()) {
                User user = new User();
                user.setPhone("111");
                user.setPassword(passwordEncoder.encode("111"));
                user.setEmail("admin@admin.com");
                user.setBirthday(LocalDate.of(1990, 1, 1));
                user.setFullName("Admin");
                user.setNickName("Admin");
                user.setStatus(1);
                user.setRole(Role.ADMIN);
                userRepository.save(user);
                log.info("admin user has been created with default password: 111, please change it");
            }
        };
    }
}
