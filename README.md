# VidShort Backend 🎬

Production-grade Spring Boot 3.3 backend for the AI Video Generation Platform (Runway/Sora-like studio), powered by the Google Gemini API (Veo 3 & Imagen 3), PostgreSQL, and Cloudflare R2 object storage.

---

## 🚀 Features

- **Authentication & Security**:
  - JWT-based authentication (`Bearer <token>`).
  - User API Key management with AES-256 encrypted storage for Gemini API keys.
- **Relational Project Management**:
  - Multi-scene storyboard architecture.
  - Character consistency studio with facial/outfit reference images.
- **AI Engine Integration**:
  - Gemini Veo Video Generation API (`predictLongRunning` polling architecture).
  - Gemini Imagen 3 for storyboard thumbnail generation.
  - Contextual prompt assembly combining camera movement, lighting, shot type, and character traits.
- **Cloudflare R2 Storage (S3-Compatible)**:
  - AWS SDK v2 integration for presigned uploads/downloads.
  - Zero-egress fee architecture within Cloudflare 10 GB free tier limits.
- **Production Observability**:
  - MDC Trace ID tracking (`X-Trace-Id`) across sync and `@Async` thread pools.
  - Rolling log appenders (daily + size rotated).
  - OpenApi 3.0 / Swagger UI documentation.

---

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.3.5 (Java 17)
- **Database**: PostgreSQL (Production/Dev), In-Memory H2 (Local zero-dependency testing)
- **Migrations**: Flyway (12 relational tables)
- **Storage**: AWS S3 SDK v2 (`software.amazon.awssdk:s3:2.29.15`)
- **JWT**: JJWT (`io.jsonwebtoken:jjwt:0.12.6`)
- **Documentation**: SpringDoc OpenAPI (`2.6.0`)

---

## 🏃 Getting Started Locally

### 1. In-Memory Mode (No external database required)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **H2 Web Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
  *(JDBC URL: `jdbc:h2:mem:aivideo_db`, Username: `sa`, Password: `<empty>`)*

---

### 2. PostgreSQL + Cloudflare R2 Mode

Set environment variables:
```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=aivideo_db
export DB_USER=postgres
export DB_PASSWORD=postgres

export CF_ACCOUNT_ID="your-cloudflare-account-id"
export CF_ACCESS_KEY="your-r2-access-key"
export CF_SECRET_KEY="your-r2-secret-key"
export CF_R2_BUCKET="ai-video-studio"
export CF_R2_PUBLIC_URL="https://assets.yourdomain.com"
export GEMINI_API_KEY="your-gemini-api-key"

mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 📜 API Documentation

When the application is running, open [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for interactive API documentation and testing.
