package com.roscasend.web.quizz.api;

import java.util.List;

public record QuestionDto(
        String id,
        String text,
        String code,
        List<AnswerDto> answers) {
}
