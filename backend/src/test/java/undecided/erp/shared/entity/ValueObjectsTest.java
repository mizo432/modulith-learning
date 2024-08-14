package undecided.erp.shared.entity;

import static org.assertj.core.api.Assertions.assertThat;
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
}
