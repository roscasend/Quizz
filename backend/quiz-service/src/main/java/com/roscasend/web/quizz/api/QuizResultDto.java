package com.roscasend.web.quizz.api;

import java.util.List;

public record QuizResultDto(
        int correctCount,
        int totalQuestions,
        List<QuestionReviewDto> reviews) {
}
