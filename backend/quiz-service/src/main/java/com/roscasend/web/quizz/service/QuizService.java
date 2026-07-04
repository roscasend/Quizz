package com.roscasend.web.quizz.service;

import com.roscasend.web.quizz.QuestionReview;
import com.roscasend.web.quizz.QuizResult;
import com.roscasend.web.quizz.api.AnswerDto;
import com.roscasend.web.quizz.api.QuestionDto;
import com.roscasend.web.quizz.api.QuestionReviewDto;
import com.roscasend.web.quizz.api.QuizDto;
import com.roscasend.web.quizz.api.QuizResultDto;
import com.roscasend.web.quizz.api.QuizSubmissionRequest;
import com.roscasend.web.quizz.answer.AnswerView;
import com.roscasend.web.quizz.question.QuestionView;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class QuizService {
    private final QuizCatalog quizCatalog;

    public QuizService(QuizCatalog quizCatalog) {
        this.quizCatalog = quizCatalog;
    }

    public QuizDto getQuiz(String quizId) {
        QuizDefinition quizDefinition = getQuizDefinition(quizId);
        List<QuestionDto> questions = quizDefinition.quiz().getQuestions().stream()
                .map(this::toQuestionDto)
                .toList();
        return new QuizDto(quizDefinition.id(), quizDefinition.title(), questions);
    }

    public QuizResultDto submitQuiz(String quizId, QuizSubmissionRequest request) {
        QuizDefinition quizDefinition = getQuizDefinition(quizId);
        Map<String, Set<String>> submittedAnswers =
                request == null || request.answersByQuestionId() == null
                        ? Map.of()
                        : request.answersByQuestionId();
        QuizResult result = quizDefinition.quiz().evaluate(submittedAnswers);
        return toResultDto(result);
    }

    private QuizDefinition getQuizDefinition(String quizId) {
        return quizCatalog.findById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }

    private QuestionDto toQuestionDto(QuestionView question) {
        List<AnswerDto> answers = question.getAnswers().stream()
                .map(this::toAnswerDto)
                .toList();
        return new QuestionDto(
                question.getId(),
                question.getQuestionText(),
                question.getCode().orElse(null),
                answers);
    }

    private AnswerDto toAnswerDto(AnswerView answer) {
        return new AnswerDto(answer.getId(), answer.getText());
    }

    private QuizResultDto toResultDto(QuizResult result) {
        return new QuizResultDto(
                result.getCorrectCount(),
                result.getTotalQuestions(),
                result.getReviews().stream()
                        .map(this::toReviewDto)
                        .toList());
    }

    private QuestionReviewDto toReviewDto(QuestionReview review) {
        return new QuestionReviewDto(
                review.getQuestion().getId(),
                review.getQuestionText(),
                review.getSelectedAnswerIds(),
                review.isCorrect(),
                review.getCorrectAnswerTexts(),
                review.getExplanation());
    }
}
