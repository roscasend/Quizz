package com.roscasend.web.quizz.question;

import com.roscasend.web.quizz.evaluation.EvaluationStrategy;

public interface QuestionEvaluationView extends QuestionView {
    EvaluationStrategy getEvaluationStrategy();
}
