package com.roscasend.web.quizz.api;

import java.util.Map;
import java.util.Set;

public record QuizSubmissionRequest(Map<String, Set<String>> answersByQuestionId) {
}
