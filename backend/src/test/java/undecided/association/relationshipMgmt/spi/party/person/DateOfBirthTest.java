package undecided.association.relationshipMgmt.spi.party.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DateOfBirthTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な日付を指定するとインスタンスが作成される")
    void shouldCreateInstanceWhenValidDate() {
      LocalDate date = LocalDate.of(1990, 5, 15);
      DateOfBirth dateOfBirth = DateOfBirth.of(date);

      assertThat(dateOfBirth).isNotNull();
      assertThat(dateOfBirth.value()).isEqualTo(date);
    }

    @Test
    @DisplayName("nullを指定するとIllegalArgumentExceptionがスローされる")
    void shouldThrowExceptionWhenNull() {
      assertThatThrownBy(() -> DateOfBirth.of(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("dateOfBirth must not be null");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のインスタンスが作成される")
    void shouldCreateEmptyInstance() {
      DateOfBirth dateOfBirth = DateOfBirth.empty();

      assertThat(dateOfBirth).isNotNull();
      assertThat(dateOfBirth.value()).isNull();
    }
  }

  @Nested
  @DisplayName("デフォルトコンストラクタのテスト")
  class DefaultConstructorTest {

    @Test
    @DisplayName("nullの値を持つインスタンスが作成される")
    void shouldCreateInstanceWithNullValue() {
      DateOfBirth dateOfBirth = new DateOfBirth();

      assertThat(dateOfBirth).isNotNull();
      assertThat(dateOfBirth.value()).isNull();
    }
  }

  @Nested
  @DisplayName("valueメソッドのテスト")
  class ValueMethodTest {

    @Test
    @DisplayName("保持している値を返す")
    void shouldReturnHeldValue() {
      LocalDate date = LocalDate.of(2000, 1, 1);
      DateOfBirth dateOfBirth = DateOfBirth.of(date);

      assertThat(dateOfBirth.value()).isEqualTo(date);
    }

    @Test
    @DisplayName("空のインスタンスではnullを返す")
    void shouldReturnNullForEmptyInstance() {
      DateOfBirth dateOfBirth = DateOfBirth.empty();

      assertThat(dateOfBirth.value()).isNull();
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("値をマスクして返す")
    void shouldReturnMaskedValue() {
      LocalDate date = LocalDate.of(1990, 5, 15);
      DateOfBirth dateOfBirth = DateOfBirth.of(date);

      assertThat(dateOfBirth.toString()).isEqualTo("1990-**-**");
    }

    @Test
    @DisplayName("nullの場合はnullを返す")
    void shouldReturnNullWhenValueIsNull() {
      DateOfBirth dateOfBirth = DateOfBirth.empty();

      assertThat(dateOfBirth.toString()).isNull();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      LocalDate date = LocalDate.of(1990, 5, 15);
      DateOfBirth first = DateOfBirth.of(date);
      DateOfBirth second = DateOfBirth.of(date);

      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      DateOfBirth first = DateOfBirth.of(LocalDate.of(1990, 5, 15));
      DateOfBirth second = DateOfBirth.of(LocalDate.of(2000, 1, 1));

      assertThat(first).isNotEqualTo(second);
    }

    @Test
    @DisplayName("空のインスタンス同士は等しい")
    void shouldBeEqualWhenBothEmpty() {
      DateOfBirth first = DateOfBirth.empty();
      DateOfBirth second = DateOfBirth.empty();

      assertThat(first).isEqualTo(second);
    }
  }

  @Nested
  @DisplayName("DateOfBirthConverterのテスト")
  class ConverterTest {

    private final DateOfBirth.DateOfBirthConverter converter =
        new DateOfBirth.DateOfBirthConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("属性からLocalDateに変換する")
      void shouldConvertToDatabaseColumn() {
        LocalDate date = LocalDate.of(1990, 5, 15);
        DateOfBirth dateOfBirth = DateOfBirth.of(date);

        assertThat(converter.convertToDatabaseColumn(dateOfBirth)).isEqualTo(date);
      }

      @Test
      @DisplayName("null属性はnullを返す")
      void shouldReturnNullForNullAttribute() {
        assertThat(converter.convertToDatabaseColumn(null)).isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("LocalDateから属性に変換する")
      void shouldConvertToEntityAttribute() {
        LocalDate date = LocalDate.of(1990, 5, 15);
        DateOfBirth result = converter.convertToEntityAttribute(date);

        assertThat(result).isNotNull();
        assertThat(result.value()).isEqualTo(date);
      }

      @Test
      @DisplayName("nullデータベースデータは空のインスタンスを返す")
      void shouldReturnEmptyForNullDatabaseData() {
        DateOfBirth result = converter.convertToEntityAttribute(null);

        assertThat(result).isNotNull();
        assertThat(result.value()).isNull();
      }
    }
  }
}
