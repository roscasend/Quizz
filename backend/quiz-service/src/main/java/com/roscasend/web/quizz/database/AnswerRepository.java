package com.roscasend.web.quizz.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<AnswerEntity, String> {
    Optional<AnswerEntity> findTopByQuestion_IdOrderByAnswerOrderDesc(String questionId);
}
