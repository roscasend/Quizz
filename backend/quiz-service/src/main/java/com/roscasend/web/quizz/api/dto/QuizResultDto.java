package com.roscasend.web.quizz.api.dto;

import java.util.List;

public record QuizResultDto(
        int correctCount,
        int totalQuestions,
        List<QuestionReviewDto> reviews) {
}
