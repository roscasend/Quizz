# Quiz Platform

Enterprise learning project built as a monorepo.

## Structure

```text
quiz-platform
├── backend
│   └── quiz-service
├── frontend
├── mobile
├── infrastructure
└── docs
```

## Current Service

`backend/quiz-service` is the first Spring Boot microservice. It exposes quiz display and quiz submission endpoints.

Run all backend tests from the repository root:

```bash
./mvnw clean test
```

Run the quiz service:

```bash
./mvnw -pl backend/quiz-service spring-boot:run
```

Available endpoints:

```text
GET  http://localhost:8080/api/quizzes/java-basics
POST http://localhost:8080/api/quizzes/java-basics/submissions
```
