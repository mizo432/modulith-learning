package undecided.shared.sample.jpa.entity.embeddable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("SamplePhoneNumberの単体テスト")
class SamplePhoneNumberTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な値で電話番号インスタンスが作成される")
    void shouldCreatePhoneNumberWithValidValue() {
      // Arrange & Act
      SamplePhoneNumber phoneNumber = new SamplePhoneNumber("090-1234-5678");

      // Assert
      assertThat(phoneNumber).isNotNull();
      assertThat(phoneNumber.value()).isEqualTo("090-1234-5678");
    }
  }

  @Nested
  @DisplayName("ofファクトリメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("正常な値で電話番号インスタンスが作成される")
    void shouldCreatePhoneNumberWithOfMethod() {
      // Act
      SamplePhoneNumber phoneNumber = SamplePhoneNumber.of("080-9876-5432");

      // Assert
      assertThat(phoneNumber.value()).isEqualTo("080-9876-5432");
    }

    @Test
    @DisplayName("nullを渡すとEMPTYが返される")
    void shouldReturnEmptyWhenNull() {
      // Act
      SamplePhoneNumber phoneNumber = SamplePhoneNumber.of(null);

      // Assert
      assertThat(phoneNumber).isEqualTo(SamplePhoneNumber.EMPTY);
      assertThat(phoneNumber.value()).isEmpty();
    }
  }

  @Nested
  @DisplayName("EMPTY定数のテスト")
  class EmptyConstantTest {

    @Test
    @DisplayName("EMPTYは空文字列の値を持つ")
    void shouldHaveEmptyValue() {
      // Assert
      assertThat(SamplePhoneNumber.EMPTY.value()).isEmpty();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      // Arrange
      SamplePhoneNumber first = new SamplePhoneNumber("090-1234-5678");
      SamplePhoneNumber second = new SamplePhoneNumber("090-1234-5678");

      // Assert
      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      // Arrange
      SamplePhoneNumber first = new SamplePhoneNumber("090-1234-5678");
      SamplePhoneNumber second = new SamplePhoneNumber("080-9876-5432");

      // Assert
      assertThat(first).isNotEqualTo(second);
    }
  }
}
