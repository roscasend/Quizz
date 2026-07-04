INSERT INTO quizzes (id, title)
VALUES ('java-basics', 'Java Basics');

INSERT INTO quiz_questions (
    id,
    quiz_id,
    question_order,
    question_text,
    code,
    explanation,
    question_type
) VALUES (
    'q1',
    'java-basics',
    1,
    'What is the result of String immutability?',
    'String s = "a"; s.concat("b"); System.out.print(s);',
    'String is immutable; concat returns a new String that is ignored here.',
    'SINGLE_CHOICE'
);

INSERT INTO quiz_answers (id, question_id, answer_order, answer_text, correct)
VALUES
    ('a', 'q1', 1, 'a', TRUE),
    ('ab', 'q1', 2, 'ab', FALSE);

INSERT INTO quiz_questions (
    id,
    quiz_id,
    question_order,
    question_text,
    code,
    explanation,
    question_type
) VALUES (
    'q2',
    'java-basics',
    2,
    'Which declarations are valid local variable declarations?',
    NULL,
    'var is valid for local variables with an initializer; explicit int is also valid.',
    'MULTIPLE_CHOICE'
);

INSERT INTO quiz_answers (id, question_id, answer_order, answer_text, correct)
VALUES
    ('var-name', 'q2', 1, 'var x = 1;', TRUE),
    ('int-name', 'q2', 2, 'int x = 1;', TRUE),
    ('var-no-init', 'q2', 3, 'var x;', FALSE);
