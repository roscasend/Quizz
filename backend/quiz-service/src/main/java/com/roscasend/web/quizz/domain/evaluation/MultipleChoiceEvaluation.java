package com.roscasend.web.quizz.domain.evaluation;

import com.roscasend.web.quizz.domain.answer.AnswerView;
import com.roscasend.web.quizz.domain.question.QuestionException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class MultipleChoiceEvaluation implements EvaluationStrategy {
    @Override
    public boolean evaluate(List<? extends AnswerView> answers, Set<String> selectedAnswerIds) {
        Set<String> safeSelection = selectedAnswerIds == null ? Set.of() : selectedAnswerIds;
        return correctAnswerIds(answers).equals(safeSelection);
    }

    @Override
    public void validate(List<? extends AnswerView> answers) throws QuestionException {
        long correct = answers.stream().filter(AnswerView::isCorrect).count();
        long incorrect = answers.stream().filter(answer -> !answer.isCorrect()).count();
        if (correct < 1 || incorrect < 1 || answers.size() < 2) {
            throw new QuestionException("Multiple choice questions require at least one correct and one incorrect answer");
        }
    }

    private Set<String> correctAnswerIds(List<? extends AnswerView> answers) {
        return answers.stream()
                .filter(AnswerView::isCorrect)
                .map(AnswerView::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
