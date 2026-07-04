package com.roscasend.web.quizz.database;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class QuizEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private QuizCategoryEntity category;

    @Enumerated(EnumType.STRING)
    @Column(name = "quiz_type", nullable = false)
    private QuizTypeEntity quizType;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("questionOrder ASC")
    private List<QuestionEntity> questions = new ArrayList<>();

    protected QuizEntity() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public QuizCategoryEntity getCategory() {
        return category;
    }

    public QuizTypeEntity getQuizType() {
        return quizType;
    }

    public List<QuestionEntity> getQuestions() {
        return List.copyOf(questions);
    }
}
