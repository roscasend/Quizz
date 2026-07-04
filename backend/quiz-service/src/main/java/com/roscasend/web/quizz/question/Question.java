package com.roscasend.web.quizz.question;

import com.roscasend.web.quizz.answer.Answer;
import com.roscasend.web.quizz.answer.AnswerView;
import com.roscasend.web.quizz.evaluation.EvaluationStrategy;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Question implements QuestionEvaluationView {
    private final String id;
    private final String questionText;
    private final String code;
    private final String explanation;
    private final List<Answer> answers;
    private final EvaluationStrategy evaluationStrategy;

    Question(
            String id,
            String questionText,
            String code,
            String explanation,
            List<Answer> answers,
            EvaluationStrategy evaluationStrategy) throws QuestionException {
        if (id == null || id.isBlank()) {
            throw new QuestionException("Question id cannot be null or empty");
        }
        if (questionText == null || questionText.isBlank()) {
            throw new QuestionException("Question text cannot be null or empty");
        }
        this.id = id;
        this.questionText = questionText;
        this.code = code == null ? "" : code;
        this.explanation = explanation == null ? "" : explanation;
        this.answers = List.copyOf(answers);
        this.evaluationStrategy = Objects.requireNonNull(evaluationStrategy, "evaluationStrategy");
        this.evaluationStrategy.validate(this.answers);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getQuestionText() {
        return questionText;
    }

    @Override
    public Optional<String> getCode() {
        return code.isBlank() ? Optional.empty() : Optional.of(code);
    }

    @Override
    public String getExplanation() {
        return explanation;
    }

    @Override
    public List<AnswerView> getAnswers() {
        return List.copyOf(answers);
    }

    @Override
    public EvaluationStrategy getEvaluationStrategy() {
        return evaluationStrategy;
    }
}
