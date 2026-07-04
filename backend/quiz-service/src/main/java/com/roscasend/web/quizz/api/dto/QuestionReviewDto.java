package com.roscasend.web.quizz.api.dto;

import java.util.Set;

public record QuestionReviewDto(
        String questionId,
        String questionText,
        Set<String> selectedAnswerIds,
        boolean correct,
        Set<String> correctAnswerTexts,
        String explanation) {
}
