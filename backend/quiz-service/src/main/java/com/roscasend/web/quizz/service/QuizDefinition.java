package com.roscasend.web.quizz.service;

import com.roscasend.web.quizz.domain.Quiz;

public record QuizDefinition(
        String id,
        String title,
        String type,
        String categoryName,
        Quiz quiz) {
}
