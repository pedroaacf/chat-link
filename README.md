# ChatLink

ChatLink é um protótipo de chat (somente mensagens) feito com **Spring Boot**, **WebSocket (STOMP)** e **JPA/Postgres**.

---

## Visão geral rápida
- API REST para criar conversas e buscar histórico.
- WebSocket (STOMP) para enviar e receber mensagens em tempo real.
- Persistência em **Postgres** via JPA/Hibernate.
- Testes de integração com **Testcontainers** (levanta Postgres num container para os testes).
- Pipeline CI com **GitHub Actions** (roda `mvn test`).

---

## Tecnologias
- Java 21
- Spring Boot 3.x (Web, WebSocket, Data JPA, Validation)
- PostgreSQL
- Testcontainers (testes)
- Maven
- Docker & Docker Compose
- GitHub Actions (CI)

---

## Pré-requisitos (para o desenvolvedor)
- Java JDK 21 instalado
- Maven (opcional se usar `./mvnw`)
- Docker Desktop (necessário para rodar com Docker e para Testcontainers)
- Git
- Conta no GitHub (para usar o CI/push)

> No Windows: recomendado Docker Desktop com backend WSL2 ativo.

---
## Como rodar (3 modos)

> Execute a partir da raiz do projeto

### 1) (RECOMENDADO) Rodar com Docker Compose — app + Postgres
1. Abrir terminal na raiz do projeto  
2. Executar:
`` docker compose up --build``

Acesse a aplicação em: http://localhost:8080

3. Parar e remover:
``docker compose down``

- Se quiser rodar em background:
``docker compose up -d --build``
## 2) Rodar localmente sem Docker (profile dev usa H2 in-memory)

Linux / macOS:
``SPRING_PROFILES_ACTIVE=dev ./mvnw spring-boot:run``

Windows PowerShell:
``$env:SPRING_PROFILES_ACTIVE = "dev"
.\mvnw.cmd spring-boot:run``

## 3) Híbrido — DB em Docker + app local (IDE)
Levantar só o DB via compose:
``docker compose up -d db``

Depois rode a aplicação no IntelliJ (ou via mvn spring-boot:run) — o app irá se conectar a ``jdbc:postgresql://localhost:5432/chatdb.``

## Endpoints principais e uso
### Criar conversa (REST)
- POST /api/conversations
- Request: sem body
- Response (JSON):

``{``
  ``"id": "uuid",``
``  "token": "6ed765da-851f-46d1-8cff-10219d77bdfc",``
``  "link": "/index.html?token=6ed765da-851f-46d1-8cff-10219d77bdfc"``
``}``

Exemplo curl:
``curl -X POST "http://localhost:8080/api/conversations"``

### Buscar histórico de mensagens (REST)
- GET /api/conversations/{token}/messages
- Retorna MessageResponseDto[] (lista de mensagens, tipicamente paginável se implementado).
- Exemplo:
``curl "http://localhost:8080/api/conversations/SEU_TOKEN/messages"``

### WebSocket / STOMP (tempo real)
- Endpoint WebSocket (SockJS): /ws (conecte com SockJS + Stomp)
- Destino de envio (client -> servidor): /app/conversation/{token}/message
Envie um JSON com senderName e content.
- Destino de broadcast (servidor -> clientes): /topic/conversation.{token}
Clientes inscritos neste tópico recebem as mensagens persistidas.
- Tópico de erro (opcional): /topic/conversation.{token}.error

Exemplo de payload (enviado pelo cliente STOMP):
``{``
``  "senderName": "Pedro",``
``  "content": "Olá!"``
``}``

### Exemplo de fluxo (UI de teste index.html)
1. Crie uma conversa via REST fazendo um post em http://localhost:8080/api/conversations.
Exemplo com `curl`:
``curl -X POST http://localhost:8080/api/conversations``
3. Abra http://localhost:8080/index.html?token=SEU_TOKEN em duas abas.
4. Clique Connect (conecta ao /ws).
5. Envie mensagem — deve aparecer em ambas as abas e ser persistida.

### Testes
Rodar todos os testes (requer Docker)
- Linux/mac:
``./mvnw -U clean test``

- Windows PowerShell
``.\mvnw.cmd -U clean test``
