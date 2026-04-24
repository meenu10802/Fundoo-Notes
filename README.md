# Fundoo Notes Backend

A robust, enterprise-grade Spring Boot backend for the Fundoo Notes application.

## 🏗️ Architecture & Development
This project is being developed using a strict Use-Case (UC) driven approach. It features a completely isolated `main` branch (for documentation only) and a well-structured develop branch for code integration.

All APIs are built adhering to the core layered architectural pattern:
`Client ➡️ Controller ➡️ DTO ➡️ Service ➡️ Repository ➡️ Database`

## ✅ Phase 1 Implemented Features (UC1 - UC10)

1. **UC1: Setup & Packages** - Built the foundational Spring Boot 3 structure enforcing rigid separation of concerns.

2. **UC2: Database Connectivity** - Configured MySQL connectivity and dynamic Hibernate DDL updates via `application.yml`.

3. **UC3: User Persistence** - Modelled the JPA `User` Entity and built out the abstract `UserRepository`.

4. **UC4: User Registration** - Engineered the Registration payload using rigid DTOs (`@Valid`) and a `GlobalExceptionHandler` allowing standard JSON error passing.

5. **UC5: Authentication** - Hardened database passwords using `BCryptPasswordEncoder` and configured dynamic JSON Web Token (JWT) issuing on Login.

6. **UC6: JWT Validation Pipeline** - Secured the microservice by building a `JwtAuthenticationFilter` (Bouncer) to intercept and parse headers, isolating the overall Spring Security Context.

7. **UC7: Note Entity Mapping** - Orchestrated the `Note` layer implementing decoupled Foreign Keys (`Long userId`) to evade recursion bugs and maximize efficiency, and set up state-flags (`isPinned`, etc.).

8. **UC8: Note Creation** - Devised a safe `POST` endpoint extracting the `Principal` JWT identity seamlessly to ensure a user can only create notes mapped directly to themselves.

9. **UC9: Note Dashboard Fetch** - Crafted a robust `GET` API fetching `List<Note>` mapped to the user, strictly protecting against IDOR attacks stringently handled automatically via the security context.

10. **UC10: Toggle Controllers** - Developed stateless `PUT` APIs enabling frontend clients to flip boolean flags for `Pin`, `Archive`, and `Trash` behaviors safely.

## 🚀 Phase 2 Implemented Features

11. **UC11: Aspect-Oriented Programming (AOP)**  
    Leveraged Spring AOP to modularize cross-cutting concerns such as logging, execution time tracking, and centralized exception handling. This reduced code duplication, improved maintainability, and provided better visibility into application behavior in a production environment.

12. **UC12: RabbitMQ Asynchronous Messaging**  
    Integrated a message-driven architecture using RabbitMQ to handle asynchronous operations. Implemented a Producer–Consumer pattern to process events like user registration and note creation, simulating real-time notification systems.

13. **UC13: Redis Caching (JWT + OTP + TTL)**  
    Implemented Redis as an in-memory data store to cache JWT tokens and OTPs with configurable TTL. This ensures fast authentication checks, reduced database load, and automatic expiration handling for secure sessions.

14. **UC14: Spring Cache Optimization**  
    Applied Spring Cache abstraction with Redis backend to cache user-specific note data. Designed proper cache eviction strategies to maintain data consistency after create/update operations, significantly improving API response time.

15. **UC15: JMS Messaging (ActiveMQ)**  
    Implemented Java Messaging Service (JMS) using ActiveMQ for conceptual understanding of messaging systems. Built queue-based communication using `JmsTemplate` and `@JmsListener` following the producer-consumer design pattern.

16. **UC16: Spring Batch (Excel Processing)**  
    Developed batch processing capability to import bulk notes from Excel files using Apache POI. Designed a pipeline to parse, process, and persist structured data efficiently, simulating enterprise-level data ingestion workflows.