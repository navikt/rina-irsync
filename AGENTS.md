# AGENTS.md — rina-irsync

CLI-verktøy for synkronisering av IR (Institutional Repository) i RINA 2024.
Spring Shell-applikasjon som kjøres som GraalVM native image.

## Build & Test Commands

```bash
./mvnw -B test                          # Run tests
./mvnw -B -ntp compile -DskipTests=true # Compile only
./mvnw -B -Pnative native:compile       # Build native image
```

## Project Structure

```text
src/main/java/no/nav/ten/rina/admin/irsync/
├── App.java                  # Spring Boot application entry point
├── IrCommands.java           # Spring Shell commands (order, install)
├── SynchronisationService.java # IR sync operations via RestClient
├── OrganisationsService.java # Organisation resource management
└── util/Auth.java            # CAS authentication and RestClient factory
```

## Tech Stack

- **Java 25** with GraalVM native image
- **Maven** (wrapper: `./mvnw`)
- **Spring Boot 4.0.6** with Spring Shell
- **RestClient** for HTTP calls to RINA CPI
- **Lombok** (`@Slf4j`, `@AllArgsConstructor`)
- **jMolecules** for domain architecture annotations
- **JSpecify** for nullability annotations
- **Semver4j** for version comparison
- **jsonschema2pojo** for generated model classes
- **release-please** for automated versioning and releases

## Code Style

### Java Conventions

- Prefer `var` for local variables where type is obvious
- Use `@AllArgsConstructor` + `private final` fields for constructor injection
- Use `@Retryable` for resilient HTTP calls
- Prefer functional interfaces over abstract classes
- **Optional:** Never call `.get()` without `.isPresent()` check — prefer `.orElse()`, `.orElseThrow()`, `.map()`, `.ifPresent()`

### Minimal Editing

When fixing a bug or implementing a feature, change only what is necessary.
Do not rename variables, restructure working code, or refactor beyond the task at hand.
Keep diffs small and focused so they are easy to review.

## Git Workflow

- Trunk-based: all work on `main`
- Conventional commits (parsed by release-please): `feat:`, `fix:`, `ci:`, `docs:`, `refactor:`, `test:`
- release-please creates release PRs and tags automatically

## Boundaries

### ✅ Always

- Run tests after changes
- Follow existing code patterns in the project
- Preserve existing code structure — do not reorganize or refactor beyond the task
- Validate all external input
- Use conventional commit messages

### ⚠️ Ask First

- Changing CAS authentication mechanism
- Adding new dependencies
- Changing native image configuration

### 🚫 Never

- Commit secrets or credentials (use `.env` and spring-dotenv)
- Return `null` from methods that could return `Optional`
- Skip input validation on external boundaries
