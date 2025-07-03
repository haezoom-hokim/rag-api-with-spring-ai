# rag-api-with-spring-ai

Spring Boot 기반의 RAG(Retrieval-Augmented Generation) API 데모 프로젝트입니다.  
외부 LLM(예: Ollama) 및 벡터DB(Qdrant)와 연동하여, 사용자 임베딩 관리와 RAG 기반 질의응답 기능을 제공합니다.

---

## 🛠️ 기술스택

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Qdrant (벡터DB)**
- **Ollama (로컬 LLM 서버)**
- **Docker Compose** (개발 환경용)
- **Gradle** (빌드 도구)

---

## 📁 주요 디렉토리 및 파일 구조
```
src/
├── main/
│   ├── java/
│   │   └── io/rag/demo/ragapidemo/
│   │       ├── rag/
│   │       │   ├── RagController.java         # RAG API 엔드포인트
│   │       │   ├── RagService.java            # RAG 핵심 비즈니스 로직
│   │       │   ├── RagDetails.java            # RAG 세부 정보 모델
│   │       │   └── dto/
│   │       │       ├── RagQueryRequest.java   # 질의 요청 DTO
│   │       │       └── RagFilteredRequest.java# 필터 질의 DTO
│   │       ├── user/
│   │       │   ├── User.java                  # 사용자 엔티티
│   │       │   ├── UserController.java        # 사용자 관련 API
│   │       │   ├── UserEmbeddingService.java  # 임베딩 벡터 관리 서비스
│   │       │   └── UserRepository.java        # 사용자 JPA 리포지토리
│   │       └── RagApiDemoApplication.java     # 메인 클래스
│   └── resources/
│       ├── application.yml                    # 전체 환경설정
│       └── db/
│           └── user.sql                       # 사용자 테이블 초기화 스크립트
└── test/
└── java/io/rag/demo/ragapidemo/
└── RagApiDemoApplicationTests.java    # 테스트 코드
```

---

## ⚙️ 주요 설정 (application.yml)

- **서버**
    - 포트: 기본값 (8080)
    - HTTP/2 활성화
    - UTF-8 인코딩 강제

- **DB (PostgreSQL)**
    - URL: `jdbc:postgresql://localhost:5432/ragdb`
    - 사용자/비밀번호: `rag` / `ragpass`
    - Hibernate DDL: validate
    - Dialect: PostgreSQL

- **JPA**
    - SQL 출력 및 포맷팅 활성화

- **AI 연동**
    - **Qdrant (벡터DB)**
        - host: `localhost`
        - port: `6334`
        - TLS 미사용
        - 스키마 자동 초기화
    - **Ollama (로컬 LLM 서버)**
        - base-url: `http://localhost:11434`
        - chat 모델: `gemma2:2b-instruct-q4_0`
        - embedding 모델: `nomic-embed-text`

- **로깅**
    - Spring AI, Qdrant: DEBUG
    - 전체: INFO

---

## 📝 데이터베이스 초기화

- `src/main/resources/db/user.sql`  
  사용자 테이블 등 DB 초기화 스크립트 제공

---

## 🚀 실행 방법

1. **환경 변수 및 설정 확인**  
   `src/main/resources/application.yml`에서 DB, Qdrant, Ollama 등 외부 서비스 연결 정보를 확인/수정하세요.

2. **(선택) 도커 환경 실행**
   ```bash
   docker-compose up -d
   ```
   (PostgreSQL, Qdrant, Ollama 등 필요시)

3. **Spring Boot 애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

4. **API 테스트**  
   Swagger UI, Postman 등으로 API 엔드포인트를 호출하여 기능을 확인할 수 있습니다.

---

## 📢 참고

- 벡터DB(Qdrant)와 LLM(Ollama) 서버가 반드시 실행 중이어야 정상 동작합니다.
- API 명세 및 예시는 각 Controller 파일을 참고하세요.
- 추가적인 설정 및 확장은 `application.yml`을 통해 가능합니다.
