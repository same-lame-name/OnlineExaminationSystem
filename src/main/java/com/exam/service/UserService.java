package com.exam.service;

import com.exam.entity.Examination;
import com.exam.entity.Result;
import com.exam.repository.ExaminationRepository;
import com.exam.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private ResultRepository resultRepository;

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
