package com.exam.service;

import com.exam.entity.Examination;
import com.exam.entity.Result;
import com.exam.repository.ExaminationRepository;
import com.exam.repository.ResultRepository;
import com.exam.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.exam.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        return userRepository.save(user);
    }

    public List<User> getPendingTeacherRequests() {
        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == User.Role.TEACHER && !u.isApproved())
                .collect(Collectors.toList());
    }

    public void approveTeacher(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getRole() == User.Role.TEACHER) {
            user.setApproved(true);
            userRepository.save(user);
        }
    }

    @Transactional
    public void deleteTeacherRequest(String username) {
        userRepository.deleteByUsername(username);
    }

    public List<Examination> getAvailableExams() {
        return examinationRepository.findAll();
    }

    public Examination getExamById(Long id) {
        return examinationRepository.findById(id).orElseThrow(() -> new RuntimeException("Exam not found"));
    }

    public List<Result> getUserResults() {
        // For simplicity, return all results; ideally filter by logged-in user
        return resultRepository.findAll();
    }
}
