package com.roscasend.web.quizz.api;

public record AnswerCreateRequest(
        String id,
        String text,
        boolean correct) {
}
