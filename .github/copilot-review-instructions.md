## Java

- Parameterized queries and safe string handling — no format strings with user input
- Never return `null` where `Optional` is appropriate — flag `orElse(null)` patterns
- Flag `.get()` on Optional without `.isPresent()` guard
- Prefer `var` for local variables where the type is obvious from context
- Use `@AllArgsConstructor` + `private final` for constructor injection (Lombok)
- Flag mutable fields in `@Service` classes (thread safety risk)

## Conventional Commits

- Commit messages must follow conventional commits format
- Valid types: `feat`, `fix`, `ci`, `docs`, `refactor`, `test`, `chore`, `build`
- Breaking changes: use `!` suffix (e.g., `feat!:`) or `BREAKING CHANGE:` footer
- Flag commits without a type prefix

## Norwegian text (all `.md` and user-facing strings)

- Use Norwegian bokmål for user-facing text
- Avoid unnecessary anglicisms when good Norwegian alternatives exist

## Security

- No secrets, tokens, or credentials in code — use `.env` with spring-dotenv
- Flag any hardcoded URLs containing credentials
- RestClient calls must go through `Auth.client()` for CAS auth

## Over-editing

Flag changes where the diff is disproportionate to the stated goal:

- Renamed variables or parameters not related to the fix
- Restructured working code without justification
- Added refactoring outside the PR scope
