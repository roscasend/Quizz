package com.roscasend.web.quizz.service;

import com.roscasend.web.quizz.domain.Quiz;
import com.roscasend.web.quizz.domain.answer.Answer;
import com.roscasend.web.quizz.domain.answer.AnswerException;
import com.roscasend.web.quizz.database.AnswerEntity;
import com.roscasend.web.quizz.database.QuestionEntity;
import com.roscasend.web.quizz.database.QuizCategoryEntity;
import com.roscasend.web.quizz.database.QuizCategoryRepository;
import com.roscasend.web.quizz.database.QuizEntity;
import com.roscasend.web.quizz.database.QuizRepository;
import com.roscasend.web.quizz.database.QuizTypeEntity;
import com.roscasend.web.quizz.domain.question.QuestionBuilder;
import com.roscasend.web.quizz.domain.question.QuestionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class QuizCatalog {
    private final QuizRepository quizRepository;
    private final QuizCategoryRepository quizCategoryRepository;

    public QuizCatalog(QuizRepository quizRepository, QuizCategoryRepository quizCategoryRepository) {
        this.quizRepository = quizRepository;
        this.quizCategoryRepository = quizCategoryRepository;
    }

    @Transactional(readOnly = true)
    public Optional<QuizDefinition> findById(String quizId) {
        return quizRepository.findById(quizId).map(this::toQuizDefinition);
    }

    @Transactional(readOnly = true)
    public List<QuizSummaryDefinition> findQuizzes(String categoryName, QuizTypeEntity quizType) {
        List<QuizEntity> quizzes;
        if (categoryName != null && quizType != null) {
            quizzes = quizRepository.findByCategory_CategoryNameAndQuizTypeOrderByTitleAsc(categoryName, quizType);
        } else if (categoryName != null) {
            quizzes = quizRepository.findByCategory_CategoryNameOrderByTitleAsc(categoryName);
        } else if (quizType != null) {
            quizzes = quizRepository.findByQuizTypeOrderByTitleAsc(quizType);
        } else {
            quizzes = quizRepository.findAllByOrderByTitleAsc();
        }
        return quizzes.stream()
                .map(this::toQuizSummary)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> findCategories() {
        return quizCategoryRepository.findAllByOrderByCategoryNameAsc().stream()
                .map(QuizCategoryEntity::getCategoryName)
                .toList();
    }

    private QuizDefinition toQuizDefinition(QuizEntity quizEntity) {
        try {
            Quiz quiz = new Quiz();
            for (QuestionEntity questionEntity : quizEntity.getQuestions()) {
                quiz.addQuestion(toQuestion(questionEntity).build());
            }
            return new QuizDefinition(
                    quizEntity.getId(),
                    quizEntity.getTitle(),
                    quizEntity.getQuizType().name(),
                    categoryName(quizEntity.getCategory()),
                    quiz);
        } catch (AnswerException | QuestionException e) {
            throw new IllegalStateException("Failed to load quiz from database", e);
        }
    }

    private QuizSummaryDefinition toQuizSummary(QuizEntity quizEntity) {
        return new QuizSummaryDefinition(
                quizEntity.getId(),
                quizEntity.getTitle(),
                quizEntity.getQuizType().name(),
                categoryName(quizEntity.getCategory()));
    }

    private String categoryName(QuizCategoryEntity categoryEntity) {
        return categoryEntity == null ? null : categoryEntity.getCategoryName();
    }

    private QuestionBuilder toQuestion(QuestionEntity questionEntity) throws AnswerException, QuestionException {
        QuestionBuilder builder = switch (questionEntity.getQuestionType()) {
            case SINGLE_CHOICE -> QuestionBuilder.singleChoice(questionEntity.getId());
            case MULTIPLE_CHOICE -> QuestionBuilder.multipleChoice(questionEntity.getId());
        };

        builder.text(questionEntity.getQuestionText())
                .code(questionEntity.getCode())
                .explanation(questionEntity.getExplanation());

        for (AnswerEntity answerEntity : questionEntity.getAnswers()) {
            builder.answer(new Answer(answerEntity.getId(), answerEntity.getAnswerText(), answerEntity.isCorrect()));
        }

        return builder;
    }
}
