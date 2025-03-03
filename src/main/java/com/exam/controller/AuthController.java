package com.exam.controller;

import com.exam.entity.User;
import com.exam.repository.UserRepository;
import com.exam.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    // Redirect root ("/") to "/landing"
    @GetMapping("/")
    public String redirectToLanding() {
        return "redirect:/landing";
    }

    // Serve the landing page
    @GetMapping("/landing")
    public String landingPage() {
        return "landing"; // Maps to src/main/resources/templates/landing.html
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        }

        if (request.isUserInRole("ROLE_TEACHER")) {
            return "redirect:/teacher/dashboard";
        }
        return "home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        // Set approval status based on role
        if (user.getRole() == User.Role.STUDENT) {
            user.setApproved(true); // Students are auto-approved
        } else if (user.getRole() == User.Role.TEACHER) {
            user.setApproved(false); // Teachers need admin approval
        } else {
            // Handle unexpected role (shouldn't happen with dropdown, but for safety)
            model.addAttribute("error", "Invalid role selected");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userService.registerUser(user);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}