package com.github.mizo432.shared.logging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.mizo432.shared.common.exception.ExceptionLevel;
import com.github.mizo432.shared.common.exception.SystemException;
import com.github.mizo432.shared.spring.logging.DefaultExceptionLevelResolver;
import com.github.mizo432.shared.spring.logging.ExceptionCodeResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@code DefaultExceptionLevelResolverTest} は、DefaultExceptionLevelResolver の単体テストクラスです。
 *
 * <p>規約要件: 1. @Tag("small") を付与 2. クラス・メソッドともに日本語 @DisplayName を付与 3. 対象メソッドごとに @Nested クラスでグループ化
 * 4. メソッド名は should で開始し、アンダースコアを使用しない 5. package-private 可視性 6. null や境界値のテストを含める
 */
@Tag("small")
@DisplayName("DefaultExceptionLevelResolverの単体テスト")
@ExtendWith(MockitoExtension.class)
class DefaultExceptionLevelResolverTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("デフォルトコンストラクタでインスタンスが生成されること")
    void shouldCreateInstanceWithDefaultConstructor() {
      // Act
      DefaultExceptionLevelResolver resolver = new DefaultExceptionLevelResolver();

      // Assert
      assertThat(resolver).isNotNull();
    }

    @Test
    @DisplayName("パラメータ付きコンストラクタでインスタンスが生成されること")
    void shouldCreateInstanceWithParameterizedConstructor() {
      // Arrange
      ExceptionCodeResolver exceptionCodeResolver = mock(ExceptionCodeResolver.class);

      // Act
      DefaultExceptionLevelResolver resolver =
          new DefaultExceptionLevelResolver(exceptionCodeResolver);

      // Assert
      assertThat(resolver).isNotNull();
    }
  }

  @Nested
  @DisplayName("resolveExceptionLevelメソッドのテスト")
  class ResolveExceptionLevelTest {

    @Test
    @DisplayName("例外コードがEで始まる場合、ERRORレベルを返すこと")
    void shouldReturnErrorWhenExceptionCodeStartsWithE() {
      // Arrange
      ExceptionCodeResolver exceptionCodeResolver = mock(ExceptionCodeResolver.class);
      SystemException exception = new SystemException("E001", "test");
      when(exceptionCodeResolver.resolveExceptionCode(exception)).thenReturn("E001");
      DefaultExceptionLevelResolver resolver =
          new DefaultExceptionLevelResolver(exceptionCodeResolver);

      // Act
      ExceptionLevel level = resolver.resolveExceptionLevel(exception);

      // Assert
      assertThat(level).isEqualTo(ExceptionLevel.ERROR);
    }

    @Test
    @DisplayName("例外コードがeで始まる小文字の場合、ERRORレベルを返すこと")
    void shouldReturnErrorWhenExceptionCodeStartsWithLowercaseE() {
      // Arrange
      ExceptionCodeResolver exceptionCodeResolver = mock(ExceptionCodeResolver.class);
      SystemException exception = new SystemException("e001", "test");
      when(exceptionCodeResolver.resolveExceptionCode(exception)).thenReturn("e001");
      DefaultExceptionLevelResolver resolver =
          new DefaultExceptionLevelResolver(exceptionCodeResolver);

      // Act
      ExceptionLevel level = resolver.resolveExceptionLevel(exception);

      // Assert
      assertThat(level).isEqualTo(ExceptionLevel.ERROR);
    }

    @Test
    @DisplayName("例外コードがWで始まる場合、WARNレベルを返すこと")
    void shouldReturnWarnWhenExceptionCodeStartsWithW() {
      // Arrange
      ExceptionCodeResolver exceptionCodeResolver = mock(ExceptionCodeResolver.class);
      SystemException exception = new SystemException("W001", "test");
      when(exceptionCodeResolver.resolveExceptionCode(exception)).thenReturn("W001");
      DefaultExceptionLevelResolver resolver =
          new DefaultExceptionLevelResolver(exceptionCodeResolver);

      // Act
      ExceptionLevel level = resolver.resolveExceptionLevel(exception);

      // Assert
      assertThat(level).isEqualTo(ExceptionLevel.WARN);
    }

    @Test
    @DisplayName("例外コードがwで始まる小文字の場合、WARNレベルを返すこと")
    void shouldReturnWarnWhenExceptionCodeStartsWithLowercaseW() {
      // Arrange
      ExceptionCodeResolver exceptionCodeResolver = mock(ExceptionCodeResolver.class);
      SystemException exception = new SystemException("w001", "test");
      when(exceptionCodeResolver.resolveExceptionCode(exception)).thenReturn("w001");
      DefaultExceptionLevelResolver resolver =
          new DefaultExceptionLevelResolver(exceptionCodeResolver);

      // Act
      ExceptionLevel level = resolver.resolveExceptionLevel(exception);

      // Assert
      assertThat(level).isEqualTo(ExceptionLevel.WARN);
    }

    @Test
    @DisplayName("例外コードがIで始まる場合、INFOレベルを返すこと")
    void shouldReturnInfoWhenExceptionCodeStartsWithI() {
      // Arrange
      ExceptionCodeResolver exceptionCodeResolver = mock(ExceptionCodeResolver.class);
      SystemException exception = new SystemException("I001", "test");
      when(exceptionCodeResolver.resolveExceptionCode(exception)).thenReturn("I001");
      DefaultExceptionLevelResolver resolver =
          new DefaultExceptionLevelResolver(exceptionCodeResolver);

      // Act
      ExceptionLevel level = resolver.resolveExceptionLevel(exception);

      // Assert
      assertThat(level).isEqualTo(ExceptionLevel.INFO);
    }

    @Test
    @DisplayName("例外コードがiで始まる小文字の場合、INFOレベルを返すこと")
    void shouldReturnInfoWhenExceptionCodeStartsWithLowercaseI() {
      // Arrange
      ExceptionCodeResolver exceptionCodeResolver = mock(ExceptionCodeResolver.class);
      SystemException exception = new SystemException("i001", "test");
      when(exceptionCodeResolver.resolveExceptionCode(exception)).thenReturn("i001");
      DefaultExceptionLevelResolver resolver =
          new DefaultExceptionLevelResolver(exceptionCodeResolver);

      // Act
      ExceptionLevel level = resolver.resolveExceptionLevel(exception);

      // Assert
      assertThat(level).isEqualTo(ExceptionLevel.INFO);
    }

    @Test
    @DisplayName("例外コードが予期しない文字で始まる場合、ERRORレベルを返すこと")
    void shouldReturnErrorWhenExceptionCodeStartsWithUnexpectedCharacter() {
      // Arrange
      ExceptionCodeResolver exceptionCodeResolver = mock(ExceptionCodeResolver.class);
      SystemException exception = new SystemException("X001", "test");
      when(exceptionCodeResolver.resolveExceptionCode(exception)).thenReturn("X001");
      DefaultExceptionLevelResolver resolver =
          new DefaultExceptionLevelResolver(exceptionCodeResolver);

      // Act
      ExceptionLevel level = resolver.resolveExceptionLevel(exception);

      // Assert
      assertThat(level).isEqualTo(ExceptionLevel.ERROR);
    }

    @Test
    @DisplayName("例外コードがnullの場合、ERRORレベルを返すこと")
    void shouldReturnErrorWhenExceptionCodeIsNull() {
      // Arrange
      ExceptionCodeResolver exceptionCodeResolver = mock(ExceptionCodeResolver.class);
      SystemException exception = new SystemException("E001", "test");
      when(exceptionCodeResolver.resolveExceptionCode(exception)).thenReturn(null);
      DefaultExceptionLevelResolver resolver =
          new DefaultExceptionLevelResolver(exceptionCodeResolver);

      // Act
      ExceptionLevel level = resolver.resolveExceptionLevel(exception);

      // Assert
      assertThat(level).isEqualTo(ExceptionLevel.ERROR);
    }

    @Test
    @DisplayName("例外コードが空文字列の場合、ERRORレベルを返すこと")
    void shouldReturnErrorWhenExceptionCodeIsEmpty() {
      // Arrange
      ExceptionCodeResolver exceptionCodeResolver = mock(ExceptionCodeResolver.class);
      SystemException exception = new SystemException("E001", "test");
      when(exceptionCodeResolver.resolveExceptionCode(exception)).thenReturn("");
      DefaultExceptionLevelResolver resolver =
          new DefaultExceptionLevelResolver(exceptionCodeResolver);

      // Act
      ExceptionLevel level = resolver.resolveExceptionLevel(exception);

      // Assert
      assertThat(level).isEqualTo(ExceptionLevel.ERROR);
    }

    @Test
    @DisplayName("ExceptionCodeResolverがnullの場合、ERRORレベルを返すこと")
    void shouldReturnErrorWhenExceptionCodeResolverIsNull() {
      // Arrange
      DefaultExceptionLevelResolver resolver = new DefaultExceptionLevelResolver();
      SystemException exception = new SystemException("E001", "test");

      // Act
      ExceptionLevel level = resolver.resolveExceptionLevel(exception);

      // Assert
      assertThat(level).isEqualTo(ExceptionLevel.ERROR);
    }
  }
}
