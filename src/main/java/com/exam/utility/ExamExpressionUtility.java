package com.exam.utility;

import com.exam.entity.Examination;
import com.exam.entity.Subscription;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExamExpressionUtility {
    public boolean hasSubscription(List<Subscription> subscriptions, Examination exam) {
        return subscriptions.stream()
            .anyMatch(sub -> sub.getExamination().getId().equals(exam.getId()));
    }
}
