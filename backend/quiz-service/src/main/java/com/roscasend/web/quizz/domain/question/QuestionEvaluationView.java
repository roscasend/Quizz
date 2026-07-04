package com.roscasend.web.quizz.domain.question;

import com.roscasend.web.quizz.domain.evaluation.EvaluationStrategy;

public interface QuestionEvaluationView extends QuestionView {
    EvaluationStrategy getEvaluationStrategy();
}
