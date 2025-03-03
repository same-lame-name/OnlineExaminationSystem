package com.exam.config;

import com.exam.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.exam.service.CustomUserDetailsService;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/logo.jpeg", "/photo.jpeg", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("STUDENT")
                // Modified teacher matcher to check both role and approval status
                .requestMatchers("/teacher/**").access(this::checkTeacherApproval)
                .requestMatchers("/", "/landing", "/login", "/register").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")           // Trigger logout on /logout
                .logoutSuccessUrl("/landing?logout") // Redirect to custom logout page
                .invalidateHttpSession(true)    // Invalidate session
                .clearAuthentication(true)      // Clear authentication
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }

    private AuthorizationDecision checkTeacherApproval(
            Supplier<Authentication> authentication,
            RequestAuthorizationContext context) {
        Authentication auth = authentication.get();

        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        // Check if user has TEACHER role
        boolean hasTeacherRole = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_TEACHER"));

        if (!hasTeacherRole) {
            return new AuthorizationDecision(false);
        }

        if (auth.getPrincipal() instanceof User) { // Check for custom User entity
            User user = (User) auth.getPrincipal();
            return new AuthorizationDecision(user.isApproved()); // Uses UserDetails.isEnabled()
        }

        return new AuthorizationDecision(false);
    }
}
