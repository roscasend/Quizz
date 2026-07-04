package com.roscasend.web.quizz.domain.answer;

import java.util.Objects;

public final class Answer implements AnswerView {
    private final String id;
    private final String text;
    private final boolean correct;

    public Answer(String id, String text, boolean correct) throws AnswerException {
        if (id == null || id.isBlank()) {
            throw new AnswerException("Answer id cannot be null or empty");
        }
        if (text == null || text.isBlank()) {
            throw new AnswerException("Answer cannot be null or empty");
        }
        this.id = id;
        this.text = text;
        this.correct = correct;
    }

    public static Answer correct(String id, String text) throws AnswerException {
        return new Answer(id, text, true);
    }

    public static Answer incorrect(String id, String text) throws AnswerException {
        return new Answer(id, text, false);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public boolean isCorrect() {
        return correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer answer)) return false;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
