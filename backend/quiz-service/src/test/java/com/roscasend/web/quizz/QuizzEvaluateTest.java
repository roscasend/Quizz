package com.roscasend.web.quizz;

import com.roscasend.web.quizz.answer.Answer;
import com.roscasend.web.quizz.question.Question;
import com.roscasend.web.quizz.question.QuestionBuilder;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuizzEvaluateTest {

    @Test
    void evaluateSingleAndMulti() throws Exception {
        Question q1 = QuestionBuilder.singleChoice("q1")
                .text("Pick one")
                .explanation("Only A is correct.")
                .answer(Answer.incorrect("b", "B"))
                .answer(Answer.correct("a", "A"))
                .build();

        Question q2 = QuestionBuilder.multipleChoice("q2")
                .text("Pick all correct")
                .answer(Answer.correct("x", "x"))
                .answer(Answer.correct("y", "y"))
                .answer(Answer.incorrect("z", "z"))
                .build();

        Quizz quizz = new Quizz();
        quizz.addQuestion(q1);
        quizz.addQuestion(q2);

        QuizResult result = quizz.evaluate(Map.of("q1", Set.of("a"), "q2", Set.of("x", "y")));

        assertEquals(2, result.getCorrectCount());
        assertEquals(2, result.getTotalQuestions());
        assertTrue(result.getReviews().get(0).isCorrect());
        assertEquals(Set.of("a"), result.getReviews().get(0).getSelectedAnswerIds());
        assertEquals("Only A is correct.", result.getReviews().get(0).getExplanation());
        assertTrue(result.getReviews().get(1).isCorrect());
    }

    @Test
    void evaluateWithMapMissingAnswerCountsAsWrong() throws Exception {
        Question q1 = QuestionBuilder.singleChoice("q1")
                .text("Pick one")
                .answer(Answer.incorrect("no", "no"))
                .answer(Answer.correct("yes", "yes"))
                .build();

        Quizz quizz = new Quizz();
        quizz.addQuestion(q1);

        QuizResult result = quizz.evaluate(Map.of());

        assertEquals(0, result.getCorrectCount());
        assertFalse(result.getReviews().get(0).isCorrect());
        assertEquals("yes", result.getReviews().get(0).getCorrectAnswerTexts().iterator().next());
    }

    @Test
    void evaluateMultiOrderInsensitive() throws Exception {
        Question q = QuestionBuilder.multipleChoice("q1")
                .text("Pick all")
                .answer(Answer.correct("a", "a"))
                .answer(Answer.correct("b", "b"))
                .answer(Answer.incorrect("c", "c"))
                .build();

        Quizz quizz = new Quizz();
        quizz.addQuestion(q);

        assertEquals(1, quizz.evaluate(Map.of("q1", Set.of("b", "a"))).getCorrectCount());
        assertEquals(0, quizz.evaluate(Map.of("q1", Set.of("a", "c"))).getCorrectCount());
    }
}
