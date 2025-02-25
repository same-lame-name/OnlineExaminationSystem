package com.exam.service;

import com.exam.entity.Examination;
import com.exam.entity.Result;
import com.exam.repository.ExaminationRepository;
import com.exam.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private ResultRepository resultRepository;

    public List<Examination> getAllExams() {
        return examinationRepository.findAll();
    }

    public void addExam(Examination examination) {
        examinationRepository.save(examination);
    }

    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }
}