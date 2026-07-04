package com.roscasend.web.quizz.domain;

import com.roscasend.web.quizz.domain.question.QuestionEvaluationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class QuizEvaluator {
    public QuizResult evaluate(Quiz quiz, Map<String, Set<String>> responsesByQuestionId) {
        Map<String, Set<String>> safeResponses = responsesByQuestionId == null ? Map.of() : responsesByQuestionId;
        List<QuestionReview> reviews = new ArrayList<>();
        int correct = 0;

        for (QuestionEvaluationView question : quiz.getEvaluationQuestions()) {
            Set<String> selectedAnswerIds = safeResponses.getOrDefault(question.getId(), Set.of());
            boolean ok = question.getEvaluationStrategy().evaluate(question.getAnswers(), selectedAnswerIds);
            if (ok) {
                correct++;
            }
            reviews.add(new QuestionReview(question, selectedAnswerIds, ok));
        }

        return new QuizResult(correct, quiz.getEvaluationQuestions().size(), reviews);
    }
}
