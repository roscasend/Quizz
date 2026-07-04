package com.roscasend.web.quizz.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizCategoryRepository extends JpaRepository<QuizCategoryEntity, String> {
    List<QuizCategoryEntity> findAllByOrderByCategoryNameAsc();
}
