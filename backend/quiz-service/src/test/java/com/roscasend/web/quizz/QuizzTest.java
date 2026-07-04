package com.roscasend.web.quizz;

import com.roscasend.web.quizz.answer.Answer;
import com.roscasend.web.quizz.question.Question;
import com.roscasend.web.quizz.question.QuestionBuilder;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuizzTest {

    @Test
    public void testAddQuestion() throws Exception {
        Quizz quizz = new Quizz();
        Question question = QuestionBuilder.singleChoice("age")
                .text("Ce varsta ai?")
                .answer(Answer.incorrect("13", "13 ani"))
                .answer(Answer.incorrect("14", "14 ani"))
                .answer(Answer.incorrect("15", "15 ani"))
                .answer(Answer.incorrect("16", "16 ani"))
                .answer(Answer.correct("17", "17 ani"))
                .build();

        quizz.addQuestion(question);
        QuizResult result = quizz.evaluate(Map.of("age", Set.of("17")));

        assertEquals(1, quizz.getQuestions().size());
        assertEquals(1, result.getCorrectCount());
        assertTrue(result.getReviews().get(0).isCorrect());
    }

    @Test
    void returnedQuestionListCannotMutateQuiz() throws Exception {
        Quizz quizz = new Quizz();
        quizz.addQuestion(QuestionBuilder.singleChoice("q1")
                .text("Pick one")
                .answer(Answer.correct("a", "A"))
                .answer(Answer.incorrect("b", "B"))
                .build());

        assertThrows(UnsupportedOperationException.class, () -> quizz.getQuestions().clear());
        assertEquals(1, quizz.getQuestions().size());
    }
}
