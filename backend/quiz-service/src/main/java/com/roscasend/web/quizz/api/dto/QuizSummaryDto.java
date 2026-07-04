package com.roscasend.web.quizz.api.dto;

public record QuizSummaryDto(
        String id,
        String title,
        String type,
        String categoryName) {
}
