package com.exam.dto;

import java.util.List;

public class ExamSubmission {
    private Long examId;
    private List<String> answers;

    // Getters and setters
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }
    public List<String> getAnswers() { return answers; }
    public void setAnswers(List<String> answers) { this.answers = answers; }
}
