package com.roscasend.web.quizz.domain.answer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnswerTest {
    @Test
    void rejectsMissingId() {
        AnswerException exception = assertThrows(
                AnswerException.class,
                () -> new Answer("", "text", true));

        assertEquals("Answer id cannot be null or empty", exception.getMessage());
    }

    @Test
    void rejectsMissingText() {
        AnswerException exception = assertThrows(
                AnswerException.class,
                () -> new Answer("a", "", true));

        assertEquals("Answer cannot be null or empty", exception.getMessage());
    }

    @Test
    void createsCorrectAndIncorrectAnswers() throws AnswerException {
        Answer correct = Answer.correct("a", "A");
        Answer incorrect = Answer.incorrect("b", "B");

        assertEquals("a", correct.getId());
        assertEquals("A", correct.getText());
        assertTrue(correct.isCorrect());
        assertFalse(incorrect.isCorrect());
    }
}
