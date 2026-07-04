package com.roscasend.web.quizz.api;

import com.roscasend.web.quizz.api.dto.QuizDto;
import com.roscasend.web.quizz.api.dto.QuizResultDto;
import com.roscasend.web.quizz.api.dto.QuizSummaryDto;
import com.roscasend.web.quizz.database.QuizTypeEntity;
import com.roscasend.web.quizz.service.QuizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public List<QuizSummaryDto> getQuizzes(
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) QuizTypeEntity type) {
        return quizService.getQuizzes(categoryName, type);
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return quizService.getCategories();
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
