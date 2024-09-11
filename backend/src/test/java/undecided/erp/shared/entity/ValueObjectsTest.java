package undecided.erp.shared.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ValueObjectsクラスの")
class ValueObjectsTest {

  @Nested
  @DisplayName("isAllValuesNotEmptyメソッドのテスト")
  class IsAllValuesNotEmptyTest {

    @Test
    @DisplayName("全てのValueObjectが空でない場合、真を返す")
    void shouldReturnTrueWhenAllValueObjectAreNotEmpty() {
      ValueObject mockValueObject1 = mock(ValueObject.class);
      ValueObject mockValueObject2 = mock(ValueObject.class);

      when(mockValueObject1.isEmpty()).thenReturn(false);
      when(mockValueObject2.isEmpty()).thenReturn(false);

      boolean result = ValueObject.ValueObjects.isAllValuesNotEmpty(mockValueObject1,
          mockValueObject2);

      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("少なくとも一つはValueObjectが空の場合、偽を返す")
    void shouldReturnFalseWhenAnyValueObjectAreEmpty() {
      ValueObject mockValueObject1 = mock(ValueObject.class);
      ValueObject mockValueObject2 = mock(ValueObject.class);

      when(mockValueObject1.isEmpty()).thenReturn(false);
      when(mockValueObject2.isEmpty()).thenReturn(true);

      boolean result = ValueObject.ValueObjects.isAllValuesNotEmpty(mockValueObject1,
          mockValueObject2);

      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("全てのValueObjectが空の場合、偽を返す")
    void shouldReturnFalseWhenAllValueObjectAreEmpty() {
      ValueObject mockValueObject1 = mock(ValueObject.class);
      ValueObject mockValueObject2 = mock(ValueObject.class);

      when(mockValueObject1.isEmpty()).thenReturn(true);
      when(mockValueObject2.isEmpty()).thenReturn(true);

      boolean result = ValueObject.ValueObjects.isAllValuesNotEmpty(mockValueObject1,
          mockValueObject2);

      assertThat(result).isFalse();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyTest {

    @Test
    @DisplayName("ValueObjectが空の場合、真を返す")
    void shouldReturnTrueWhenValueObjectIsEmpty() {
      ValueObject mockValueObject = mock(ValueObject.class);

      when(mockValueObject.isEmpty()).thenReturn(true);

      boolean result = ValueObject.ValueObjects.isEmpty(mockValueObject);

      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("ValueObjectが空でない場合、偽を返す")
    void shouldReturnFalseWhenValueObjectIsNotEmpty() {
      ValueObject mockValueObject = mock(ValueObject.class);

      when(mockValueObject.isEmpty()).thenReturn(false);

      boolean result = ValueObject.ValueObjects.isEmpty(mockValueObject);

      assertThat(result).isFalse();
    }
  }

  @Nested
  @DisplayName("nonEmptyメソッドのテスト")
  class NonEmptyTest {

    @Test
    @DisplayName("ValueObjectが空の時、偽を返す")
    void shouldReturnFalseWhenValueObjectIsEmpty() {
      ValueObject mockValueObject = mock(ValueObject.class);

      when(mockValueObject.isEmpty()).thenReturn(true);

      boolean result = ValueObject.ValueObjects.nonEmpty(mockValueObject);

      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("ValueObjectが空でない時、真を返す")
    void shouldReturnTrueWhenValueObjectIsNotEmpty() {
      ValueObject mockValueObject = mock(ValueObject.class);

      when(mockValueObject.isEmpty()).thenReturn(false);

      boolean result = ValueObject.ValueObjects.nonEmpty(mockValueObject);

      assertThat(result).isTrue();
    }
  }

  @Nested
  @DisplayName("checkNotEmptyメソッドのテスト")
  class CheckNotEmptyTest {

    @Test
    @DisplayName("ValueObjectが空の場合、例外をスローする")
    void shouldThrowExceptionWhenValueObjectIsEmpty() {
      ValueObject
          mockValueObject = mock(ValueObject.class);

      when(mockValueObject.isEmpty()).thenReturn(true);

      assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
        ValueObject.ValueObjects.checkNotEmpty(mockValueObject, RuntimeException::new);
      });
    }

    @Test
    @DisplayName("ValueObjectが空でない場合、そのValueObjectを返す")
    void shouldReturnTheValueObjectWhenValueObjectIsNotEmpty() {
      ValueObject mockValueObject = mock(ValueObject.class);

      when(mockValueObject.isEmpty()).thenReturn(false);

      ValueObject result = ValueObject.ValueObjects.checkNotEmpty(mockValueObject,
          RuntimeException::new);

      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(mockValueObject);
    }

    @Test
    @DisplayName("ValueObjectの引数がnullの場合、ヌルポインター例外をスローする")
    void shouldThrowNullPointerExceptionWhenValueObjectArgumentIsNull() {
      assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> {
        ValueObject.ValueObjects.checkNotEmpty(null, RuntimeException::new);
      });
    }
  }

  @Nested
  @DisplayName("defaultIfNullメソッドのテスト")
  class DefaultIfNullTest {

    @Test
    @DisplayName("引数のValueObjectがNullの場合、デフォルト値を返す")
    void shouldReturnDefaultValueWhenValueObjectIsNull() {
      ValueObject mockDefaultValue = mock(ValueObject.class);

      ValueObject result = ValueObject.ValueObjects.defaultIfNull(null, mockDefaultValue);

      assertThat(result).isEqualTo(mockDefaultValue);
    }

    @Test
    @DisplayName("引数のValueObjectがNullではない場合、引数のValueObjectを返す")
    void shouldReturnValueObjectWhenValueObjectIsNotNull() {
      ValueObject mockValueObject = mock(ValueObject.class);
      ValueObject mockDefaultValue = mock(ValueObject.class);

      ValueObject result = ValueObject.ValueObjects.defaultIfNull(mockValueObject,
          mockDefaultValue);

      assertThat(result).isEqualTo(mockValueObject);
    }
  }

  @Nested
  @DisplayName("defaultIfEmptyメソッドのテスト")
  class DefaultIfEmptyTest {

    @Test
    @DisplayName("引数のValueObjectがNullの場合、デフォルト値を返す")
    void shouldReturnDefaultValueWhenValueObjectIsNull() {
      ValueObject mockDefaultValue = mock(ValueObject.class);

      ValueObject result = ValueObject.ValueObjects.defaultIfEmpty(null, mockDefaultValue);

      assertThat(result).isEqualTo(mockDefaultValue);
    }

    @Test
    @DisplayName("引数のValueObjectが空の場合、デフォルト値を返す")
    void shouldReturnDefaultValueWhenValueObjectIsEmpty() {
      ValueObject mockValueObject = mock(ValueObject.class);
      ValueObject mockDefaultValue = mock(ValueObject.class);

      when(mockValueObject.isEmpty()).thenReturn(true);

      ValueObject result = ValueObject.ValueObjects.defaultIfEmpty(mockValueObject,
          mockDefaultValue);

      assertThat(result).isEqualTo(mockDefaultValue);
    }

    @Test
    @DisplayName("引数のValueObjectがNullでも空でもない場合、引数のValueObjectを返す")
    void shouldReturnValueObjectWhenValueObjectIsNotNullAndNotEmpty() {
      ValueObject mockValueObject = mock(ValueObject.class);
      ValueObject mockDefaultValue = mock(ValueObject.class);

      when(mockValueObject.isEmpty()).thenReturn(false);

      ValueObject result = ValueObject.ValueObjects.defaultIfEmpty(mockValueObject,
          mockDefaultValue);

      assertThat(result).isEqualTo(mockValueObject);
    }
  }
}
