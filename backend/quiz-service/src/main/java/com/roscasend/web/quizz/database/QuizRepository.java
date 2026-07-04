package com.roscasend.web.quizz.database;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends JpaRepository<QuizEntity, String> {
    @Override
    @EntityGraph(attributePaths = {"category", "questions", "questions.answers"})
    Optional<QuizEntity> findById(String id);

    @EntityGraph(attributePaths = "category")
    List<QuizEntity> findAllByOrderByTitleAsc();

    @EntityGraph(attributePaths = "category")
    List<QuizEntity> findByCategory_CategoryNameOrderByTitleAsc(String categoryName);

    @EntityGraph(attributePaths = "category")
    List<QuizEntity> findByQuizTypeOrderByTitleAsc(QuizTypeEntity quizType);

    @EntityGraph(attributePaths = "category")
    List<QuizEntity> findByCategory_CategoryNameAndQuizTypeOrderByTitleAsc(String categoryName, QuizTypeEntity quizType);
}
