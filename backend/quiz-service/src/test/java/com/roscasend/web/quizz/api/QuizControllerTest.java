package com.roscasend.web.quizz.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getQuizReturnsDisplayDataWithoutCorrectFlags() throws Exception {
        mockMvc.perform(get("/api/quizzes/java-basics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("java-basics"))
                .andExpect(jsonPath("$.title").value("Java Basics"))
                .andExpect(jsonPath("$.type").value("CHAPTER_TEST"))
                .andExpect(jsonPath("$.categoryName").value("Java Basics"))
                .andExpect(jsonPath("$.questions[0].id").value("q1"))
                .andExpect(jsonPath("$.questions[0].answers[0].id").value("a"))
                .andExpect(content().string(not(containsString("correct"))));
    }

    @Test
    void getCategoriesReturnsChapterCategories() throws Exception {
        mockMvc.perform(get("/api/quizzes/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Java Basics"));
    }

    @Test
    void getQuizzesCanFilterByChapterCategory() throws Exception {
        mockMvc.perform(get("/api/quizzes")
                        .param("categoryName", "Java Basics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("java-basics"))
                .andExpect(jsonPath("$[0].type").value("CHAPTER_TEST"))
                .andExpect(jsonPath("$[0].categoryName").value("Java Basics"));
    }

    @Test
    void getQuizzesCanFilterFinalTests() throws Exception {
        mockMvc.perform(get("/api/quizzes")
                        .param("type", "FINAL_TEST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("java-final-sample"))
                .andExpect(jsonPath("$[0].type").value("FINAL_TEST"))
                .andExpect(jsonPath("$[0].categoryName").doesNotExist());
    }

    @Test
    void submitQuizReturnsEvaluationResult() throws Exception {
        mockMvc.perform(post("/api/quizzes/java-basics/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                  "answersByQuestionId": {
                                    "q1": ["a"],
                                    "q2": ["var-name", "int-name"]
                                  }
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.correctCount").value(2))
                .andExpect(jsonPath("$.totalQuestions").value(2))
                .andExpect(jsonPath("$.reviews[0].correct").value(true))
                .andExpect(jsonPath("$.reviews[1].correct").value(true));
    }

    @Test
    void addQuestionCreatesQuestionWithAnswers() throws Exception {
        mockMvc.perform(post("/api/quizzes/java-final-sample/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                  "id": "q-added-crud",
                                  "text": "Which keyword prevents reassignment?",
                                  "code": null,
                                  "explanation": "final prevents reassignment after initialization.",
                                  "type": "SINGLE_CHOICE",
                                  "answers": [
                                    {
                                      "id": "final-keyword",
                                      "text": "final",
                                      "correct": true
                                    },
                                    {
                                      "id": "static-keyword",
                                      "text": "static",
                                      "correct": false
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("q-added-crud"))
                .andExpect(jsonPath("$.text").value("Which keyword prevents reassignment?"))
                .andExpect(jsonPath("$.answers[0].id").value("final-keyword"))
                .andExpect(content().string(not(containsString("correct"))));

        mockMvc.perform(get("/api/quizzes/java-final-sample"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("q-added-crud")));
    }

    @Test
    void addAnswerAppendsAnswerToQuestion() throws Exception {
        mockMvc.perform(post("/api/quizzes/java-final-sample/questions/qf1/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                  "id": "interpreted-only",
                                  "text": "Java is interpreted only.",
                                  "correct": false
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("interpreted-only"))
                .andExpect(jsonPath("$.text").value("Java is interpreted only."))
                .andExpect(content().string(not(containsString("correct"))));

        mockMvc.perform(get("/api/quizzes/java-final-sample"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("interpreted-only")));
    }

    @Test
    void addQuestionRejectsInvalidAnswerSet() throws Exception {
        mockMvc.perform(post("/api/quizzes/java-final-sample/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                {
                                  "id": "q-invalid-single-choice",
                                  "text": "Which answers are correct?",
                                  "explanation": "Only one correct answer is allowed for single choice questions.",
                                  "type": "SINGLE_CHOICE",
                                  "answers": [
                                    {
                                      "id": "q-invalid-a",
                                      "text": "A",
                                      "correct": true
                                    },
                                    {
                                      "id": "q-invalid-b",
                                      "text": "B",
                                      "correct": true
                                    }
                                  ]
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unknownQuizReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/quizzes/missing"))
                .andExpect(status().isNotFound());
    }
}
