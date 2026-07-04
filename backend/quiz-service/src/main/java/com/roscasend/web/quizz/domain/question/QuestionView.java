package com.roscasend.web.quizz.domain.question;

import com.roscasend.web.quizz.domain.answer.AnswerView;

import java.util.List;
import java.util.Optional;

public interface QuestionView {
    String getId();

    String getQuestionText();

    Optional<String> getCode();

    String getExplanation();

    List<AnswerView> getAnswers();
}
