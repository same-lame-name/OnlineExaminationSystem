package com.exam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.exam.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
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
}
//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http
//            .authorizeHttpRequests(authorize -> authorize
//                    .requestMatchers("/teacher/**").hasRole("TEACHER") // Changed from ADMIN to TEACHER
//                    .requestMatchers("/user/**").hasRole("USER")
//                    .requestMatchers("/", "/landing", "/login", "/register").permitAll()
//                    .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                    .loginPage("/login")
//                    .defaultSuccessUrl("/home", true)
//                    .permitAll()
//            )
//            .logout(logout -> logout
//                    .logoutUrl("/logout")
//                    .logoutSuccessUrl("/landing?logout")
//                    .invalidateHttpSession(true)
//                    .clearAuthentication(true)
//                    .permitAll()
//            );
//    return http.build();
//}
