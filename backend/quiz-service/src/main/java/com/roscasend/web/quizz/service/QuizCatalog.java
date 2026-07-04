package com.roscasend.web.quizz.service;

import com.roscasend.web.quizz.Quizz;
import com.roscasend.web.quizz.answer.Answer;
import com.roscasend.web.quizz.answer.AnswerException;
import com.roscasend.web.quizz.question.QuestionBuilder;
import com.roscasend.web.quizz.question.QuestionException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class QuizCatalog {
    private final Map<String, QuizDefinition> quizzes;

    public QuizCatalog() {
        this.quizzes = Map.of("java-basics", javaBasicsQuiz());
    }

    public Optional<QuizDefinition> findById(String quizId) {
        return Optional.ofNullable(quizzes.get(quizId));
    }

    private QuizDefinition javaBasicsQuiz() {
        try {
            Quizz quizz = new Quizz();
            quizz.addQuestion(QuestionBuilder.singleChoice("q1")
                    .text("What is the result of String immutability?")
                    .code("String s = \"a\";\ns.concat(\"b\");\nSystem.out.print(s);")
                    .explanation("String is immutable; concat returns a new String that is ignored here.")
                    .answer(Answer.correct("a", "a"))
                    .answer(Answer.incorrect("ab", "ab"))
                    .build());
            quizz.addQuestion(QuestionBuilder.multipleChoice("q2")
                    .text("Which declarations are valid local variable declarations?")
                    .explanation("var is valid for local variables with an initializer; explicit int is also valid.")
                    .answer(Answer.correct("var-name", "var x = 1;"))
                    .answer(Answer.correct("int-name", "int x = 1;"))
                    .answer(Answer.incorrect("var-no-init", "var x;"))
                    .build());
            return new QuizDefinition("java-basics", "Java Basics", quizz);
        } catch (AnswerException | QuestionException e) {
            throw new IllegalStateException("Failed to create sample quiz", e);
        }
    }
}
