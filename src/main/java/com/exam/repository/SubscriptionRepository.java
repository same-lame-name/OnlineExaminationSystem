package com.exam.repository;

import com.exam.entity.Subscription;
import com.exam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByStudentAndExaminationId(User student, Long examId);
    List<Subscription> findByStudent(User student);
}
