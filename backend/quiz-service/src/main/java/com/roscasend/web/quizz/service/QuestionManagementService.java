package com.roscasend.web.quizz.service;

import com.roscasend.web.quizz.api.AnswerCreateRequest;
import com.roscasend.web.quizz.api.QuestionCreateRequest;
import com.roscasend.web.quizz.api.dto.AnswerDto;
import com.roscasend.web.quizz.api.dto.QuestionDto;
import com.roscasend.web.quizz.database.AnswerEntity;
import com.roscasend.web.quizz.database.AnswerRepository;
import com.roscasend.web.quizz.database.QuestionEntity;
import com.roscasend.web.quizz.database.QuestionRepository;
import com.roscasend.web.quizz.database.QuestionTypeEntity;
import com.roscasend.web.quizz.database.QuizEntity;
import com.roscasend.web.quizz.database.QuizRepository;
import com.roscasend.web.quizz.domain.answer.Answer;
import com.roscasend.web.quizz.domain.answer.AnswerException;
import com.roscasend.web.quizz.domain.question.QuestionBuilder;
import com.roscasend.web.quizz.domain.question.QuestionException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionManagementService {
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QuestionManagementService(
            QuizRepository quizRepository,
            QuestionRepository questionRepository,
            AnswerRepository answerRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    public QuestionDto addQuestion(String quizId, QuestionCreateRequest request) {
        if (request == null) {
            throw badRequest("Question request body is required");
        }

        QuizEntity quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> notFound("Quiz not found"));
        String questionId = idOrGenerated(request.id());
        List<AnswerDraft> answers = toAnswerDrafts(request.answers());

        validateQuestion(questionId, request.text(), request.code(), request.explanation(), request.type(), answers);
        ensureQuestionIdAvailable(questionId);
        ensureAnswerIdsAvailable(answers);

        int questionOrder = nextQuestionOrder(quizId);
        QuestionEntity question = new QuestionEntity(
                questionId,
                quiz,
                questionOrder,
                request.text(),
                request.code(),
                request.explanation(),
                request.type());

        int answerOrder = 1;
        for (AnswerDraft answer : answers) {
            question.addAnswer(new AnswerEntity(answer.id(), answerOrder++, answer.text(), answer.correct()));
        }

        return toQuestionDto(questionRepository.save(question));
    }

    @Transactional
    public AnswerDto addAnswer(String quizId, String questionId, AnswerCreateRequest request) {
        if (request == null) {
            throw badRequest("Answer request body is required");
        }

        QuestionEntity question = questionRepository.findByIdAndQuiz_Id(questionId, quizId)
                .orElseThrow(() -> notFound("Question not found"));
        AnswerDraft answer = toAnswerDraft(request);

        validateQuestionWithAddedAnswer(question, answer);
        ensureAnswerIdAvailable(answer.id());

        int answerOrder = nextAnswerOrder(questionId);
        AnswerEntity answerEntity = new AnswerEntity(answer.id(), answerOrder, answer.text(), answer.correct());
        question.addAnswer(answerEntity);

        return toAnswerDto(answerEntity);
    }

    private void validateQuestion(
            String questionId,
            String text,
            String code,
            String explanation,
            QuestionTypeEntity type,
            List<AnswerDraft> answers) {
        try {
            QuestionBuilder builder = builderFor(type, questionId)
                    .text(text)
                    .code(code)
                    .explanation(explanation);
            for (AnswerDraft answer : answers) {
                builder.answer(new Answer(answer.id(), answer.text(), answer.correct()));
            }
            builder.build();
        } catch (AnswerException | QuestionException e) {
            throw badRequest(e.getMessage());
        }
    }

    private void validateQuestionWithAddedAnswer(QuestionEntity question, AnswerDraft answer) {
        List<AnswerDraft> answers = new ArrayList<>();
        for (AnswerEntity answerEntity : question.getAnswers()) {
            answers.add(new AnswerDraft(answerEntity.getId(), answerEntity.getAnswerText(), answerEntity.isCorrect()));
        }
        answers.add(answer);
        validateQuestion(
                question.getId(),
                question.getQuestionText(),
                question.getCode(),
                question.getExplanation(),
                question.getQuestionType(),
                answers);
    }

    private QuestionBuilder builderFor(QuestionTypeEntity type, String questionId) {
        if (type == null) {
            throw badRequest("Question type is required");
        }
        return switch (type) {
            case SINGLE_CHOICE -> QuestionBuilder.singleChoice(questionId);
            case MULTIPLE_CHOICE -> QuestionBuilder.multipleChoice(questionId);
        };
    }

    private void ensureQuestionIdAvailable(String questionId) {
        if (questionRepository.existsById(questionId)) {
            throw conflict("Question id already exists");
        }
    }

    private void ensureAnswerIdsAvailable(List<AnswerDraft> answers) {
        for (AnswerDraft answer : answers) {
            ensureAnswerIdAvailable(answer.id());
        }
    }

    private void ensureAnswerIdAvailable(String answerId) {
        if (answerRepository.existsById(answerId)) {
            throw conflict("Answer id already exists");
        }
    }

    private int nextQuestionOrder(String quizId) {
        return questionRepository.findTopByQuiz_IdOrderByQuestionOrderDesc(quizId)
                .map(question -> question.getQuestionOrder() + 1)
                .orElse(1);
    }

    private int nextAnswerOrder(String questionId) {
        return answerRepository.findTopByQuestion_IdOrderByAnswerOrderDesc(questionId)
                .map(answer -> answer.getAnswerOrder() + 1)
                .orElse(1);
    }

    private List<AnswerDraft> toAnswerDrafts(List<AnswerCreateRequest> requests) {
        if (requests == null) {
            return List.of();
        }

        List<AnswerDraft> answers = new ArrayList<>();
        for (AnswerCreateRequest request : requests) {
            answers.add(toAnswerDraft(request));
        }
        return answers;
    }

    private AnswerDraft toAnswerDraft(AnswerCreateRequest request) {
        if (request == null) {
            throw badRequest("Answer cannot be null");
        }
        return new AnswerDraft(idOrGenerated(request.id()), request.text(), request.correct());
    }

    private String idOrGenerated(String requestedId) {
        if (requestedId == null || requestedId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return requestedId;
    }

    private QuestionDto toQuestionDto(QuestionEntity question) {
        return new QuestionDto(
                question.getId(),
                question.getQuestionText(),
                blankToNull(question.getCode()),
                question.getAnswers().stream()
                        .map(this::toAnswerDto)
                        .toList());
    }

    private AnswerDto toAnswerDto(AnswerEntity answer) {
        return new AnswerDto(answer.getId(), answer.getAnswerText());
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private ResponseStatusException badRequest(String reason) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, reason);
    }

    private ResponseStatusException notFound(String reason) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, reason);
    }

    private ResponseStatusException conflict(String reason) {
        return new ResponseStatusException(HttpStatus.CONFLICT, reason);
    }

    private record AnswerDraft(String id, String text, boolean correct) {
    }
}
