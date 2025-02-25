package com.exam.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Examination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price; // New field for exam price


    @Column(name = "active", nullable = false)
    private boolean active = true; // Default to active (true)

    @OneToMany(mappedBy = "examination")
    private List<Question> questions;

    // Constructors
    public Examination() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}