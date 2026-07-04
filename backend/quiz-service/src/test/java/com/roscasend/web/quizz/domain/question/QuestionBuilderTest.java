package com.roscasend.web.quizz.domain.question;

import com.roscasend.web.quizz.domain.answer.Answer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionBuilderTest {
    @Test
    void buildsSingleChoiceQuestionWithOptionalCode() throws Exception {
        Question question = QuestionBuilder.singleChoice("q1")
                .text("What prints?")
                .code("System.out.println(\"a\");")
                .explanation("The string literal is printed.")
                .answer(Answer.correct("a", "a"))
                .answer(Answer.incorrect("b", "b"))
                .build();

        assertEquals("q1", question.getId());
        assertEquals("What prints?", question.getQuestionText());
        assertEquals("System.out.println(\"a\");", question.getCode().orElseThrow());
        assertEquals("The string literal is printed.", question.getExplanation());
        assertEquals(2, question.getAnswers().size());
    }

    @Test
    void rejectsDuplicateAnswerIds() throws Exception {
        QuestionBuilder builder = QuestionBuilder.singleChoice("q1")
                .text("Pick one")
                .answer(Answer.correct("a", "A"));

        QuestionException exception = assertThrows(
                QuestionException.class,
                () -> builder.answer(Answer.incorrect("a", "Another A")));

        assertEquals("you cannot add the same answer twice", exception.getMessage());
    }

    @Test
    void validatesSingleChoiceRules() throws Exception {
        QuestionBuilder builder = QuestionBuilder.singleChoice("q1")
                .text("Pick one")
                .answer(Answer.correct("a", "A"))
                .answer(Answer.correct("b", "B"));

        QuestionException exception = assertThrows(QuestionException.class, builder::build);

        assertEquals(
                "Single choice questions require one correct answer and at least one incorrect answer",
                exception.getMessage());
    }

    @Test
    void returnedAnswersCannotChangeQuestion() throws Exception {
        Question question = QuestionBuilder.singleChoice("q1")
                .text("Pick one")
                .answer(Answer.correct("a", "A"))
                .answer(Answer.incorrect("b", "B"))
                .build();

        assertThrows(UnsupportedOperationException.class, () -> question.getAnswers().clear());
        assertEquals(2, question.getAnswers().size());
        assertFalse(question.getCode().isPresent());
    }

    @Test
    void validatesMultipleChoiceRules() throws Exception {
        QuestionBuilder builder = QuestionBuilder.multipleChoice("q1")
                .text("Pick all")
                .answer(Answer.correct("a", "A"));

        QuestionException exception = assertThrows(QuestionException.class, builder::build);

        assertEquals(
                "Multiple choice questions require at least one correct and one incorrect answer",
                exception.getMessage());
    }

    @Test
    void multipleChoiceAllowsMoreThanOneCorrectAnswer() throws Exception {
        Question question = QuestionBuilder.multipleChoice("q1")
                .text("Pick all")
                .answer(Answer.correct("a", "A"))
                .answer(Answer.correct("b", "B"))
                .answer(Answer.incorrect("c", "C"))
                .build();

        assertTrue(question.getEvaluationStrategy().evaluate(question.getAnswers(), java.util.Set.of("a", "b")));
        assertFalse(question.getEvaluationStrategy().evaluate(question.getAnswers(), java.util.Set.of("a")));
    }
}
