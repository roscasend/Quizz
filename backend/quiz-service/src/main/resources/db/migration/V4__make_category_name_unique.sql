ALTER TABLE quiz_categories
    ADD CONSTRAINT uq_quiz_categories_name UNIQUE (name);
