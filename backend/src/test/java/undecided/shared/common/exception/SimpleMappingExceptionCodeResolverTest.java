package undecided.shared.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.LinkedHashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("SimpleMappingExceptionCodeResolver のテスト")
@Tag("small")
class SimpleMappingExceptionCodeResolverTest {

  // Dummy exception classes for testing
  static class SpecificException extends RuntimeException {}

  @Nested
  @DisplayName("resolveExceptionCode メソッドのテスト")
  class ResolveExceptionCodeTest {

    @Test
    @DisplayName("例外が null の場合、defaultExceptionCode を返す")
    void shouldReturnDefaultExceptionCodeWhenExceptionIsNull() {
      // Arrange
      SimpleMappingExceptionCodeResolver resolver = new SimpleMappingExceptionCodeResolver();
      resolver.setDefaultExceptionCode("DEFAULT_CODE");

      // Act
      String result = resolver.resolveExceptionCode(null);

      // Assert
      assertThat(result).isEqualTo("DEFAULT_CODE");
    }

    @Test
    @DisplayName("exceptionMappings に一致する例外マッピングがある場合、対応するコードを返す")
    void shouldReturnMappedCodeWhenExceptionMatches() {
      // Arrange
      SimpleMappingExceptionCodeResolver resolver = new SimpleMappingExceptionCodeResolver();
      LinkedHashMap<String, String> mappings = new LinkedHashMap<>();
      mappings.put("SpecificException", "SPECIFIC_CODE");
      resolver.setExceptionMappings(mappings);

      Exception exception = new SpecificException();

      // Act
      String result = resolver.resolveExceptionCode(exception);

      // Assert
      assertThat(result).isEqualTo("SPECIFIC_CODE");
    }

    @Test
    @DisplayName("exceptionMappings の中に一致する例外がない場合、defaultExceptionCode を返す")
    void shouldReturnDefaultCodeWhenNoMappingsMatch() {
      // Arrange
      SimpleMappingExceptionCodeResolver resolver = new SimpleMappingExceptionCodeResolver();
      resolver.setDefaultExceptionCode("DEFAULT_CODE");
      LinkedHashMap<String, String> mappings = new LinkedHashMap<>();
      mappings.put("NonMatchingException", "NON_MATCHING_CODE");
      resolver.setExceptionMappings(mappings);

      Exception exception = new SpecificException();

      // Act
      String result = resolver.resolveExceptionCode(exception);

      // Assert
      assertThat(result).isEqualTo("DEFAULT_CODE");
    }

    @Test
    @DisplayName("例外の祖先クラスが exceptionMappings に一致する場合、対応するコードを返す")
    void shouldReturnMappedCodeWhenSuperclassMatches() {
      // Arrange
      SimpleMappingExceptionCodeResolver resolver = new SimpleMappingExceptionCodeResolver();
      LinkedHashMap<String, String> mappings = new LinkedHashMap<>();
      mappings.put("RuntimeException", "RUNTIME_CODE");
      resolver.setExceptionMappings(mappings);

      Exception exception = new SpecificException();

      // Act
      String result = resolver.resolveExceptionCode(exception);

      // Assert
      assertThat(result).isEqualTo("RUNTIME_CODE");
    }
  }
}
