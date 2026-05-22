# Project Information for Agents

This document provides technical details for developers and AI agents working on this project.

## Build and Configuration

- **Build System**: Gradle
- **Java Version**: OpenJDK 26 (Configured via Gradle Toolchain in `backend/build.gradle.kts`).
- **Project Structure**: Modular Monolith using Spring Modulith.
    - `backend`: Core business logic and infrastructure.
    - `frontend`: React/MUI based SPA.
    - `api-gateway`: Routing.
    - `service-registry`: Netflix Eureka.
    - `authorization`: Security/Auth.
- **Database**: PostgreSQL (managed via Flyway in some modules).

### Key Commands

- Build project: `./gradlew build`
- Run backend: `./gradlew :backend:bootRun`
- Run Small tests (Unit tests): `./gradlew :backend:test`
- Run Medium tests (Integration tests): `./gradlew :backend:mediumTest`
- Run Large tests (System/Load tests): `./gradlew :backend:largeTest`

## Testing Information

### Test Strategy

- **Framework**: JUnit 5, AssertJ.
- **Categorization**: Tests are tagged with `@Tag("small")`, `@Tag("medium")`, or `@Tag("large")`.
- **Modulith Verification**: `SpringModulithTest` is used for verifying module boundaries.

### Guidelines for Adding Tests

- **Naming**: Method names must start with `should` and avoid underscores (e.g.,
  `shouldReturnCorrectValue`).
- **Annotations**:
    - Use `@DisplayName` with Japanese descriptions for both classes and methods.
    - Use `@Tag` to specify the test size.
- **Structure**:
    - Nest tests within `@Nested` classes named after the target method (e.g.,
      `class AddMethodTest`).
    - Use `package-private` (no modifier) for test classes and methods.
- **Coverage**: Include boundary tests, such as `null` arguments.

### Demonstration Test Example

```java

@Tag("small")
@DisplayName("Strings2.IsEmptyのテスト")
class Strings2IsEmptyTest {

  private final Strings2.IsEmpty isEmpty = new Strings2.IsEmpty();

  @Nested
  @DisplayName("testメソッドのテスト")
  class TestMethodTest {

    @Test
    @DisplayName("nullが渡された場合、trueを返すこと")
    void shouldReturnTrueWhenInputIsNull() {
      boolean result = isEmpty.test(null);
      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("空文字列が渡された場合、trueを返すこと")
    void shouldReturnTrueWhenInputIsEmpty() {
      boolean result = isEmpty.test("");
      assertThat(result).isTrue();
    }
  }
}
```

## Additional Development Information

- **Code Style**:
    - Follows Google Java Style.
    - Extensive use of Lombok (`@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@UtilityClass`).
    - Use of JSpecify for nullability annotations (`@NonNull`).
- **Architectural Patterns**:
    - Onion Architecture (Domain, Business, Infrastructure, Presentation).
    - Domain-Driven Design (DDD) principles.
- **Modulith Documentation**: Running `./gradlew :backend:test` generates documentation in
  `backend/build/spring-modulith-docs`.
- **Common Utilities**: Frequently used utility classes are located in
  `undecided.shared.common.primitive` (e.g., `Strings2`, `Ints`, `Objects2`).
