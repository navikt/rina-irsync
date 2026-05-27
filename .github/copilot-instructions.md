# Copilot Instructions for rina-irsync

## Repository Overview

CLI tool for synchronising IR (Institutional Repository) data in RINA 2024.
Built as a Spring Shell application, compiled to GraalVM native image, distributed as platform binaries via GitHub Releases.

## Tech Stack

- Java 25 with GraalVM native image
- Maven (wrapper: `./mvnw`)
- Spring Boot 4.0.6 + Spring Shell
- RestClient for HTTP communication with RINA CPI
- Lombok (`@Slf4j`, `@AllArgsConstructor`, `@Value`)
- jMolecules (architecture annotations)
- JSpecify (nullability)
- spring-dotenv for local configuration
- CAS authentication (TGT → ST → session cookie)

## Key Patterns

### RestClient with CAS auth

All HTTP calls go through `Auth.client()` which handles the full CAS authentication flow
(TGT → Service Ticket → XSRF cookie). Services use this factory method to get an authenticated RestClient.

### Retry with @Retryable

External HTTP calls use `@Retryable` with exponential backoff for resilience against
transient failures from CPI.

### Spring Shell commands

User-facing operations are Spring Shell `@Command` methods in `IrCommands`.
These orchestrate service calls and log results.

### Optional handling

- Never call `.get()` without prior `.isPresent()` check
- Prefer `.orElse()`, `.orElseThrow()`, `.map()`, `.ifPresent()`
- Return `Optional<T>` instead of nullable return types for new code

### Functional interfaces

- Prefer functional interfaces (`Function`, `Predicate`, `Consumer`, `Supplier`) over abstract classes for single-method contracts
- Use method references where they improve readability

## Minimal Editing

When fixing a bug or implementing a feature, change only what is necessary.
Do not rename variables, restructure working code, or refactor beyond the task at hand.
Keep diffs small and focused so they are easy to review.
