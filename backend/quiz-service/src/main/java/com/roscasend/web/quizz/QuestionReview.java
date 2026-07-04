package com.roscasend.web.quizz;

import com.roscasend.web.quizz.answer.AnswerView;
import com.roscasend.web.quizz.question.QuestionView;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class QuestionReview {

    private final QuestionView question;
    private final Set<String> selectedAnswerIds;
    private final boolean correct;

    public QuestionReview(QuestionView question, Set<String> selectedAnswerIds, boolean correct) {
        this.question = Objects.requireNonNull(question, "question");
        this.selectedAnswerIds = selectedAnswerIds == null ? Set.of() : Set.copyOf(selectedAnswerIds);
        this.correct = correct;
    }

    public QuestionView getQuestion() {
        return question;
    }

    public Set<String> getSelectedAnswerIds() {
        return selectedAnswerIds;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getQuestionText() {
        return question.getQuestionText();
    }

    public String getExplanation() {
        return question.getExplanation();
    }

    public Set<String> getCorrectAnswerTexts() {
        return question.getAnswers().stream()
                .filter(AnswerView::isCorrect)
                .map(AnswerView::getText)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
