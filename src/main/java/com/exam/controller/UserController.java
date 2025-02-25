package com.exam.controller;

import com.exam.dto.ExamSubmission;
import com.exam.entity.*;
import com.exam.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired private ExaminationRepository examRepo;
    @Autowired private SubscriptionRepository subRepo;
    @Autowired private QuestionRepository questionRepo;
    @Autowired private ResultRepository resultRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/exams")
    public String viewExams(Model model, Principal principal) {
        User student = userRepo.findByUsername(principal.getName());
        model.addAttribute("exams", examRepo.findAllActive());
        List<Subscription> subscriptions = subRepo.findByStudent(student);
        model.addAttribute("subscriptions", subscriptions != null ? subscriptions : Collections.emptyList());
        return "user/exams";
    }

    @GetMapping("/exam-purchase")
    public String purchaseForm(@RequestParam Long examId, Model model) {
        model.addAttribute("exam", examRepo.findById(examId).orElseThrow());
        return "user/exam-purchase";
    }

    @PostMapping("/exam/purchase")
    public String purchaseExam(@RequestParam Long examId, Principal principal) {
        User student = userRepo.findByUsername(principal.getName());
        Examination exam = examRepo.findById(examId).orElseThrow();
        Subscription sub = new Subscription();
        sub.setStudent(student);
        sub.setExamination(exam);
        sub.setActive(true);
        subRepo.save(sub);
        return "redirect:/user/exams";
    }

    @GetMapping("/exam-take")
    public String takeExam(@RequestParam Long examId, Model model, Principal principal) {
        User student = userRepo.findByUsername(principal.getName());
        Subscription sub = subRepo.findByStudentAndExaminationId(student, examId);
        if (sub == null || !sub.isActive()) {
            return "redirect:/user/exams";
        }
        ExamSubmission submission = new ExamSubmission();
        submission.setExamId(examId);
        model.addAttribute("examSubmission", submission);
        model.addAttribute("exam", examRepo.findById(examId).orElseThrow());
        model.addAttribute("questions", questionRepo.findByExaminationId(examId));
        return "user/exam-take";
    }

    @PostMapping("/exam/submit")
    public String submitExam(@ModelAttribute("examSubmission") ExamSubmission submission,
                           Principal principal) {
        List<String> answers = submission.getAnswers();
        Long examId = submission.getExamId();
        User student = userRepo.findByUsername(principal.getName());
        Examination exam = examRepo.findById(examId).orElseThrow();
        List<Question> questions = questionRepo.findByExaminationId(examId);

        int score = 0;
        for (int i = 0; i < questions.size() && i < answers.size(); i++) {
            if (questions.get(i).getCorrectAnswer().equals(answers.get(i))) {
                score++;
            }
        }

        Result result = new Result();
        result.setStudent(student);
        result.setExamination(exam);
        result.setScore(score);
        result.setDateTaken(LocalDateTime.now().toString());
        resultRepo.save(result);

        return "redirect:/user/results";
    }

    @GetMapping("/results")
    public String viewResults(Model model, Principal principal) {
        User student = userRepo.findByUsername(principal.getName());
        model.addAttribute("results", resultRepo.findByStudent(student));
        return "user/results";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        User student = userRepo.findByUsername(principal.getName());
        model.addAttribute("student", student);
        return "user/profile";
    }

    @PostMapping("/password")
    public String changePassword(@RequestParam String newPassword, Principal principal) {
        User student = userRepo.findByUsername(principal.getName());
        student.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(student);
        return "redirect:/user/profile";
    }
}