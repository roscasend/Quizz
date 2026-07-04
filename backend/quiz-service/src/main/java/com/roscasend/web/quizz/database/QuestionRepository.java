package com.roscasend.web.quizz.database;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<QuestionEntity, String> {
    @EntityGraph(attributePaths = {"quiz", "answers"})
    Optional<QuestionEntity> findByIdAndQuiz_Id(String id, String quizId);

    Optional<QuestionEntity> findTopByQuiz_IdOrderByQuestionOrderDesc(String quizId);
}
