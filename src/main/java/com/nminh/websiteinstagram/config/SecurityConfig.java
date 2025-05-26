package com.nminh.websiteinstagram.config;

import com.nminh.websiteinstagram.security.CustomUserDetailsService;
import com.nminh.websiteinstagram.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter ;
    private final CustomUserDetailsService userDetailsService ;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/v1/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/v1/user/**","/api/chat/**","/v1/user/chat/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/chat/**","/v1/user/chat/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/api/auth/**",
                                        "/ws/**","/v1/auth/**",
                                        "/v1/user/search/**",
                                        "/api/images/upload",
                                        "/login",
                                        "register",
                                        "/home",
                                        "/images/**",
                                        "/css/**",
                                        "/js/**",
                                        "/favicon.ico",
                                        "/*.jpg",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui.html",
                                        "/swagger-resources/**",
                                        "/webjars/**"
                                ).permitAll()
                                .requestMatchers(this::isStaticResource).permitAll()

                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) ;

        return http.build();
    }

    private boolean isStaticResource(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.endsWith(".css") ||
                path.endsWith(".js") ||
                path.endsWith(".jpg") ||
                path.endsWith(".png");
    }
    @Bean
    public AuthenticationManager authManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/favicon.ico",
                "/error",
                "/static/**",
                "/resources/**"
        );
    }

}