package com.roscasend.web.quizz.api.dto;

import java.util.List;

public record QuizDto(
        String id,
        String title,
        String type,
        String categoryName,
        List<QuestionDto> questions) {
}
