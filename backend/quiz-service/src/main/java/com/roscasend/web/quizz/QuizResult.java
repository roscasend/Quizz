package com.roscasend.web.quizz;

import java.util.List;

public final class QuizResult {

    private final int correctCount;
    private final int totalQuestions;
    private final List<QuestionReview> reviews;

    public QuizResult(int correctCount, int totalQuestions, List<QuestionReview> reviews) {
        if (correctCount < 0 || totalQuestions < 0) {
            throw new IllegalArgumentException("counts must be non-negative");
        }
        if (correctCount > totalQuestions) {
            throw new IllegalArgumentException("correctCount cannot exceed totalQuestions");
        }
        this.correctCount = correctCount;
        this.totalQuestions = totalQuestions;
        this.reviews = List.copyOf(reviews);
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public List<QuestionReview> getReviews() {
        return reviews;
    }
}
