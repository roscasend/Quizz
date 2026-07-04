package com.roscasend.web.quizz.domain.evaluation;

import com.roscasend.web.quizz.domain.answer.AnswerView;
import com.roscasend.web.quizz.domain.question.QuestionException;

import java.util.List;
import java.util.Set;

public interface EvaluationStrategy {
    boolean evaluate(List<? extends AnswerView> answers, Set<String> selectedAnswerIds);

    void validate(List<? extends AnswerView> answers) throws QuestionException;
}
