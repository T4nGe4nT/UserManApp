package org.example.usermanagementapplication.config;

import org.example.usermanagementapplication.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    // BCrypt password encoder bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // CSRF disabled for testing purposes
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users/register", "/login", "/h2-console/**", "/register").permitAll()  // Public access to register and login
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")  // Restrict admin routes
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")  // Allow USER and ADMIN
                        .anyRequest().authenticated()  // All other endpoints need authentication
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Serve login page at /login
                        .loginProcessingUrl("/login")  // Handle form POST to /login
                        .failureUrl("/login?error=true")  // Redirect on login failure
                        .defaultSuccessUrl("/dashboard", true)  // Redirect after successful login
                        .permitAll()  // Allow all users to access the login page
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")  // Redirect after logout
                        .permitAll()
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))  // Allow H2 console frames
                .build();
    }

}


