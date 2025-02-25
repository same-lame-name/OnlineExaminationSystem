package com.exam.controller;

import com.exam.entity.*;
import com.exam.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired private ExaminationRepository examRepo;
    @Autowired private QuestionRepository questionRepo;
    @Autowired private ResultRepository resultRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

@GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    // View all exams (list view)
    @GetMapping("/exams")
    public String viewExams(Model model) {
        model.addAttribute("exams", examRepo.findAllActive());
        return "admin/exams";
    }

    // Search exams
    @GetMapping("/exams/search")
    public String searchExams(@RequestParam String name, Model model) {
        model.addAttribute("exams", examRepo.findByNameContaining(name));
        return "admin/exams";
    }

    // Prepare add exam form (NEW GET METHOD)
    @GetMapping("/exam/add")
    public String addExamForm(Model model) {
        model.addAttribute("newExam", new Examination()); // Provide empty exam object
        return "admin/exam-add";
    }

    // Handle add exam submission
    @PostMapping("/exam/add")
    public String addExam(@ModelAttribute("newExam") Examination exam) {
        exam.setActive(true);
        examRepo.save(exam);
        return "redirect:/admin/exams";
    }

    // Prepare edit exam form (NEW GET METHOD)
    @GetMapping("/exam/edit")
    public String editExamForm(@RequestParam("id") Long id, Model model) {
        Examination exam = examRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid exam ID"));
        model.addAttribute("exam", exam);
        return "admin/exam-edit"; // New template for editing a single exam
    }

    // Handle edit exam submission
    @PostMapping("/exam/edit")
    public String editExam(@ModelAttribute("exam") Examination exam) {
        examRepo.save(exam);
        return "redirect:/admin/exams";
    }

    // Delete exam
    @PostMapping("/exam/delete")
    public String deleteExam(@RequestParam("id") Long id) {
        Examination exam = examRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid exam ID"));
        exam.setActive(false); // Soft delete by setting active to false
        examRepo.save(exam);
        return "redirect:/admin/exams";
    }

    // Questions
    @GetMapping("/questions")
    public String viewQuestions(Model model) {
        model.addAttribute("exams", examRepo.findAll());
        model.addAttribute("newQuestion", new Question());
        return "admin/questions";
    }

    @PostMapping("/question/add")
    public String addQuestion(@ModelAttribute Question question, @RequestParam Long examId) {
        Examination exam = examRepo.findById(examId).orElseThrow();
        question.setExamination(exam);
        questionRepo.save(question);
        return "redirect:/admin/questions";
    }

    // Results
    @GetMapping("/results")
    public String viewResults(Model model) {
        model.addAttribute("results", resultRepo.findAll());
        return "admin/results";
    }

    @GetMapping("/results/search")
    public String searchResults(@RequestParam(required = false) String examName,
                              @RequestParam(required = false) String studentName, Model model) {
        if (examName != null && !examName.isEmpty()) {
            model.addAttribute("results", resultRepo.findByExaminationNameContaining(examName));
        } else if (studentName != null && !studentName.isEmpty()) {
            model.addAttribute("results", resultRepo.findByStudentUsernameContaining(studentName));
        } else {
            model.addAttribute("results", resultRepo.findAll());
        }
        return "admin/results";
    }

    // Profile
    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        User admin = userRepo.findByUsername(principal.getName());
        model.addAttribute("admin", admin);
        return "admin/profile";
    }

    @PostMapping("/password")
    public String changePassword(@RequestParam String newPassword, Principal principal) {
        User admin = userRepo.findByUsername(principal.getName());
        admin.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(admin);
        return "redirect:/admin/profile";
    }
}