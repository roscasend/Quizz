package com.roscasend.web.quizz.evaluation;

import com.roscasend.web.quizz.answer.AnswerView;
import com.roscasend.web.quizz.question.QuestionException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class SingleChoiceEvaluation implements EvaluationStrategy {
    @Override
    public boolean evaluate(List<? extends AnswerView> answers, Set<String> selectedAnswerIds) {
        if (selectedAnswerIds == null || selectedAnswerIds.size() != 1) {
            return false;
        }
        return correctAnswerIds(answers).equals(selectedAnswerIds);
    }

    @Override
    public void validate(List<? extends AnswerView> answers) throws QuestionException {
        long correct = answers.stream().filter(AnswerView::isCorrect).count();
        long incorrect = answers.stream().filter(answer -> !answer.isCorrect()).count();
        if (correct != 1 || incorrect < 1) {
            throw new QuestionException("Single choice questions require one correct answer and at least one incorrect answer");
        }
    }

    private Set<String> correctAnswerIds(List<? extends AnswerView> answers) {
        return answers.stream()
                .filter(AnswerView::isCorrect)
                .map(AnswerView::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
