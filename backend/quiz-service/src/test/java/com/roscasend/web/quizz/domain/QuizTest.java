package com.roscasend.web.quizz.domain;

import com.roscasend.web.quizz.domain.answer.Answer;
import com.roscasend.web.quizz.domain.question.Question;
import com.roscasend.web.quizz.domain.question.QuestionBuilder;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuizTest {

    @Test
    public void testAddQuestion() throws Exception {
        Quiz quiz = new Quiz();
        Question question = QuestionBuilder.singleChoice("age")
                .text("Ce varsta ai?")
                .answer(Answer.incorrect("13", "13 ani"))
                .answer(Answer.incorrect("14", "14 ani"))
                .answer(Answer.incorrect("15", "15 ani"))
                .answer(Answer.incorrect("16", "16 ani"))
                .answer(Answer.correct("17", "17 ani"))
                .build();

        quiz.addQuestion(question);
        QuizResult result = quiz.evaluate(Map.of("age", Set.of("17")));

        assertEquals(1, quiz.getQuestions().size());
        assertEquals(1, result.getCorrectCount());
        assertTrue(result.getReviews().get(0).isCorrect());
    }

    @Test
    void returnedQuestionListCannotMutateQuiz() throws Exception {
        Quiz quiz = new Quiz();
        quiz.addQuestion(QuestionBuilder.singleChoice("q1")
                .text("Pick one")
                .answer(Answer.correct("a", "A"))
                .answer(Answer.incorrect("b", "B"))
                .build());

        assertThrows(UnsupportedOperationException.class, () -> quiz.getQuestions().clear());
        assertEquals(1, quiz.getQuestions().size());
    }
}
