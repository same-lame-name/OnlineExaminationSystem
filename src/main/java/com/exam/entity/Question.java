package com.exam.entity;

import jakarta.persistence.*;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText; // The question text
    private String optionA;     // Option A
    private String optionB;     // Option B
    private String optionC;     // Option C
    private String optionD;     // Option D
    private String correctAnswer; // Correct answer (e.g., "A", "B", "C", "D")

    @ManyToOne
    @JoinColumn(name = "examination_id")
    private Examination examination;

    // Constructors
    public Question() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public Examination getExamination() { return examination; }
    public void setExamination(Examination examination) { this.examination = examination; }
}