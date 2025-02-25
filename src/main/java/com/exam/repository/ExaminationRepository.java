package com.exam.repository;

import com.exam.entity.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    @Query("SELECT e FROM Examination e WHERE e.active = true AND e.name LIKE %:name%")
    List<Examination> findByNameContaining(String name);

    @Query("SELECT e FROM Examination e WHERE e.active = true")
    List<Examination> findAllActive();
}