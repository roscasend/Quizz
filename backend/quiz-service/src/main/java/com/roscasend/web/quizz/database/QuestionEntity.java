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
@Table(name = "quiz_questions")
public class QuestionEntity {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizEntity quiz;

    @Column(name = "question_order", nullable = false)
    private int questionOrder;

    @Column(name = "question_text", nullable = false, length = 1000)
    private String questionText;

    @Column(length = 2000)
    private String code;

    @Column(nullable = false, length = 2000)
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionTypeEntity questionType;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("answerOrder ASC")
    private List<AnswerEntity> answers = new ArrayList<>();

    protected QuestionEntity() {
    }

    public QuestionEntity(
            String id,
            QuizEntity quiz,
            int questionOrder,
            String questionText,
            String code,
            String explanation,
            QuestionTypeEntity questionType) {
        this.id = id;
        this.quiz = quiz;
        this.questionOrder = questionOrder;
        this.questionText = questionText;
        this.code = code;
        this.explanation = explanation == null ? "" : explanation;
        this.questionType = questionType;
    }

    public void addAnswer(AnswerEntity answer) {
        answers.add(answer);
        answer.assignToQuestion(this);
    }

    public String getId() {
        return id;
    }

    public int getQuestionOrder() {
        return questionOrder;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCode() {
        return code;
    }

    public String getExplanation() {
        return explanation;
    }

    public QuestionTypeEntity getQuestionType() {
        return questionType;
    }

    public List<AnswerEntity> getAnswers() {
        return List.copyOf(answers);
    }
}
