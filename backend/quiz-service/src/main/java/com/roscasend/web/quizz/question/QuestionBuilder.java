package com.roscasend.web.quizz.question;

import com.roscasend.web.quizz.answer.Answer;
import com.roscasend.web.quizz.evaluation.EvaluationStrategy;
import com.roscasend.web.quizz.evaluation.MultipleChoiceEvaluation;
import com.roscasend.web.quizz.evaluation.SingleChoiceEvaluation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class QuestionBuilder {
    private final String id;
    private final EvaluationStrategy evaluationStrategy;
    private String questionText;
    private String code = "";
    private String explanation = "";
    private final List<Answer> answers = new ArrayList<>();

    private QuestionBuilder(String id, EvaluationStrategy evaluationStrategy) {
        this.id = id;
        this.evaluationStrategy = evaluationStrategy;
    }

    public static QuestionBuilder singleChoice(String id) {
        return new QuestionBuilder(id, new SingleChoiceEvaluation());
    }

    public static QuestionBuilder multipleChoice(String id) {
        return new QuestionBuilder(id, new MultipleChoiceEvaluation());
    }

    public QuestionBuilder text(String questionText) {
        this.questionText = questionText;
        return this;
    }

    public QuestionBuilder code(String code) {
        this.code = code == null ? "" : code;
        return this;
    }

    public QuestionBuilder explanation(String explanation) {
        this.explanation = explanation == null ? "" : explanation;
        return this;
    }

    public QuestionBuilder answer(Answer answer) throws QuestionException {
        for (Answer existing : answers) {
            if (existing.equals(answer)) {
                throw new QuestionException("you cannot add the same answer twice");
            }
        }
        answers.add(answer);
        return this;
    }

    public Question build() throws QuestionException {
        ensureUniqueAnswerIds();
        return new Question(id, questionText, code, explanation, answers, evaluationStrategy);
    }

    private void ensureUniqueAnswerIds() throws QuestionException {
        Set<String> ids = new HashSet<>();
        for (Answer answer : answers) {
            if (!ids.add(answer.getId())) {
                throw new QuestionException("you cannot add the same answer twice");
            }
        }
    }
}
