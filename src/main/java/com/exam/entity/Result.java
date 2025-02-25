package com.exam.entity;

import jakarta.persistence.*;

@Entity
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id") // Foreign key column for student
    private User student;

    @ManyToOne
    @JoinColumn(name = "examination_id") // Foreign key column for examination
    private Examination examination;

    private int score;

    private String dateTaken; // Stored as String for simplicity, could use LocalDateTime with converter if preferred

    // Constructors
    public Result() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    public Examination getExamination() { return examination; }
    public void setExamination(Examination examination) { this.examination = examination; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getDateTaken() { return dateTaken; }
    public void setDateTaken(String dateTaken) { this.dateTaken = dateTaken; }
}