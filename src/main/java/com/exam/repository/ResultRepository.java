package com.exam.repository;

import com.exam.entity.Result;
import com.exam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByExaminationNameContaining(String name);
    List<Result> findByStudentUsernameContaining(String username);

    List<Result> findByStudent(User student);
}