package com.roscasend.web.quizz.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "quiz_answers")
public class AnswerEntity {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionEntity question;

    @Column(name = "answer_order", nullable = false)
    private int answerOrder;

    @Column(name = "answer_text", nullable = false, length = 1000)
    private String answerText;

    @Column(nullable = false)
    private boolean correct;

    protected AnswerEntity() {
    }

    public AnswerEntity(String id, int answerOrder, String answerText, boolean correct) {
        this.id = id;
        this.answerOrder = answerOrder;
        this.answerText = answerText;
        this.correct = correct;
    }

    void assignToQuestion(QuestionEntity question) {
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public int getAnswerOrder() {
        return answerOrder;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean isCorrect() {
        return correct;
    }
}
