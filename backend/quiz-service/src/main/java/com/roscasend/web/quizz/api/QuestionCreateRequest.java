package com.roscasend.web.quizz.api;

import com.roscasend.web.quizz.database.QuestionTypeEntity;

import java.util.List;

public record QuestionCreateRequest(
        String id,
        String text,
        String code,
        String explanation,
        QuestionTypeEntity type,
        List<AnswerCreateRequest> answers) {
}
