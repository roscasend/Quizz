CREATE TABLE quizzes (
    id VARCHAR(80) PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);

CREATE TABLE quiz_questions (
    id VARCHAR(80) PRIMARY KEY,
    quiz_id VARCHAR(80) NOT NULL,
    question_order INTEGER NOT NULL,
    question_text VARCHAR(1000) NOT NULL,
    code VARCHAR(2000),
    explanation VARCHAR(2000) NOT NULL DEFAULT '',
    question_type VARCHAR(40) NOT NULL,
    CONSTRAINT fk_quiz_questions_quiz
        FOREIGN KEY (quiz_id) REFERENCES quizzes (id)
);

CREATE INDEX idx_quiz_questions_quiz_id
    ON quiz_questions (quiz_id);

CREATE TABLE quiz_answers (
    id VARCHAR(80) PRIMARY KEY,
    question_id VARCHAR(80) NOT NULL,
    answer_order INTEGER NOT NULL,
    answer_text VARCHAR(1000) NOT NULL,
    correct BOOLEAN NOT NULL,
    CONSTRAINT fk_quiz_answers_question
        FOREIGN KEY (question_id) REFERENCES quiz_questions (id)
);

CREATE INDEX idx_quiz_answers_question_id
    ON quiz_answers (question_id);
