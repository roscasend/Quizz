package com.roscasend.web.quizz.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "quiz_categories")
public class QuizCategoryEntity {
    @Id
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String categoryName;

    protected QuizCategoryEntity() {
    }

    public String getCategoryName() {
        return categoryName;
    }
}
