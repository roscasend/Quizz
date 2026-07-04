package com.roscasend.web.quizz.api;

import java.util.List;

public record QuizDto(
        String id,
        String title,
        List<QuestionDto> questions) {
}
