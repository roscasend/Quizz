package com.roscasend.web.quizz;

import com.roscasend.web.quizz.question.QuestionEvaluationView;
import com.roscasend.web.quizz.question.QuestionView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Quizz {
    private final List<QuestionEvaluationView> questions;

    public Quizz() {
        questions = new ArrayList<>();
    }

    public void addQuestion(QuestionEvaluationView question) {
        questions.add(question);
    }

    public List<QuestionView> getQuestions() {
        return List.copyOf(questions);
    }

    List<QuestionEvaluationView> getEvaluationQuestions() {
        return List.copyOf(questions);
    }

    public QuizResult evaluate(Map<String, Set<String>> responsesByQuestionId) {
        return new QuizEvaluator().evaluate(this, responsesByQuestionId);
    }
}
