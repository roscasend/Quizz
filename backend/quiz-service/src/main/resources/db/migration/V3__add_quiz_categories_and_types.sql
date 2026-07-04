CREATE TABLE quiz_categories (
    id VARCHAR(80) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL DEFAULT '',
    display_order INTEGER NOT NULL
);

ALTER TABLE quizzes
    ADD COLUMN category_id VARCHAR(80);

ALTER TABLE quizzes
    ADD COLUMN quiz_type VARCHAR(40) NOT NULL DEFAULT 'CHAPTER_TEST';

INSERT INTO quiz_categories (id, name, description, display_order)
VALUES (
    'java-basics',
    'Java Basics',
    'Core Java language fundamentals.',
    1
);

UPDATE quizzes
SET category_id = 'java-basics',
    quiz_type = 'CHAPTER_TEST'
WHERE id = 'java-basics';

ALTER TABLE quizzes
    ADD CONSTRAINT fk_quizzes_category
        FOREIGN KEY (category_id) REFERENCES quiz_categories (id);

CREATE INDEX idx_quizzes_category_id
    ON quizzes (category_id);

CREATE INDEX idx_quizzes_quiz_type
    ON quizzes (quiz_type);

INSERT INTO quizzes (id, title, category_id, quiz_type)
VALUES ('java-final-sample', 'Java Final Practice Test', NULL, 'FINAL_TEST');

INSERT INTO quiz_questions (
    id,
    quiz_id,
    question_order,
    question_text,
    code,
    explanation,
    question_type
) VALUES (
    'qf1',
    'java-final-sample',
    1,
    'Which statements about Java are true?',
    NULL,
    'Java is strongly typed, and local variable type inference requires an initializer.',
    'MULTIPLE_CHOICE'
);

INSERT INTO quiz_answers (id, question_id, answer_order, answer_text, correct)
VALUES
    ('strongly-typed', 'qf1', 1, 'Java is strongly typed.', TRUE),
    ('var-requires-init', 'qf1', 2, 'var requires an initializer for local variables.', TRUE),
    ('multiple-inheritance', 'qf1', 3, 'Classes can extend multiple classes directly.', FALSE);
