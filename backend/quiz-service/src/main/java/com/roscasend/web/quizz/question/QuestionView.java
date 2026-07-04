package com.roscasend.web.quizz.question;

import com.roscasend.web.quizz.answer.AnswerView;

import java.util.List;
import java.util.Optional;

public interface QuestionView {
    String getId();

    String getQuestionText();

    Optional<String> getCode();

    String getExplanation();

    List<AnswerView> getAnswers();
}
