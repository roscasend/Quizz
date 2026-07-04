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
                .andExpect(jsonPath("$.questions[0].id").value("q1"))
                .andExpect(jsonPath("$.questions[0].answers[0].id").value("a"))
                .andExpect(content().string(not(containsString("correct"))));
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
    void unknownQuizReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/quizzes/missing"))
                .andExpect(status().isNotFound());
    }
}
