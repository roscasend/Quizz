# Backend

Spring Boot microservices live here.

Start backend dependencies:

```bash
docker compose -f backend/docker-compose.yml up -d quiz-postgres
```

Run the current backend service from the repository root:

```bash
./mvnw -pl backend/quiz-service spring-boot:run
```

Current services:

```text
quiz-service
```

Planned services:

```text
api-gateway
identity-service
attempt-service
notification-service
analytics-service
```
