package com.roscasend.web.quizz.api;

import com.roscasend.web.quizz.service.QuizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/{quizId}")
    public QuizDto getQuiz(@PathVariable String quizId) {
        return quizService.getQuiz(quizId);
    }

    @PostMapping("/{quizId}/submissions")
    public QuizResultDto submitQuiz(
            @PathVariable String quizId,
            @RequestBody(required = false) QuizSubmissionRequest request) {
        return quizService.submitQuiz(quizId, request);
    }
}
