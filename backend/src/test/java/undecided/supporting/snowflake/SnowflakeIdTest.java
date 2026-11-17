package undecided.supporting.snowflake;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("SnowflakeIdクラスのテスト")
@Tag("small")
class SnowflakeIdTest {

  @Nested
  @DisplayName("equalsメソッドのテスト")
  class EqualsMethod {

    @Test
    @DisplayName("同じ値の別インスタンスは等しいと判定されること")
    void shouldBeEqualWithDifferentInstancesSameValue() {
      // Arrange
      SnowflakeId id1 = SnowflakeId.of(100L);
      SnowflakeId id2 = SnowflakeId.of(100L);

      // Act & Assert
      assertThat(id1).isEqualTo(id2);
      assertThat(id1.equals(id2)).isTrue();
      assertThat(id2.equals(id1)).isTrue();
    }

    @Test
    @DisplayName("同じインスタンスは自身と等しいこと")
    void shouldBeEqualWithItself() {
      // Arrange
      SnowflakeId id = SnowflakeId.of(100L);

      // Act & Assert
      assertThat(id.equals(id)).isTrue();
    }

    @Test
    @DisplayName("異なる値のインスタンスは等しくないこと")
    void shouldNotBeEqualWithDifferentValues() {
      // Arrange
      SnowflakeId id1 = SnowflakeId.of(100L);
      SnowflakeId id2 = SnowflakeId.of(200L);

      // Act & Assert
      assertThat(id1).isNotEqualTo(id2);
      assertThat(id1.equals(id2)).isFalse();
    }

    @Test
    @DisplayName("EMPTY同士は等しいこと")
    void emptyInstancesShouldBeEqual() {
      // Arrange & Assert
      assertThat(SnowflakeId.EMPTY).isEqualTo(SnowflakeId.EMPTY);
      assertThat(SnowflakeId.EMPTY.equals(SnowflakeId.empty())).isTrue();
    }

    @Test
    @DisplayName("new SnowflakeId(null)で生成したインスタンスもEMPTYと等しいこと")
    void reconstructNullShouldBeEqualToEmpty() {
      // Arrange
      SnowflakeId reconstructed = SnowflakeId.reconstruct(null);

      // Act & Assert
      assertThat(reconstructed).isEqualTo(SnowflakeId.EMPTY);
    }

    @Test
    @DisplayName("null引数に対してfalseを返すこと")
    void shouldReturnFalseForNullArgument() {
      // Arrange
      SnowflakeId id = SnowflakeId.of(100L);

      // Act & Assert
      assertThat(id.equals(null)).isFalse();
    }

    @Test
    @DisplayName("EMPTYのequalsにnullを渡すとfalseを返すこと")
    void emptyShouldReturnFalseForNullArgument() {
      // Act & Assert
      assertThat(SnowflakeId.EMPTY.equals(null)).isFalse();
    }

    @Test
    @DisplayName("異なる型のオブジェクトに対してfalseを返すこと")
    void shouldReturnFalseForDifferentType() {
      // Arrange
      SnowflakeId id = SnowflakeId.of(100L);

      // Act & Assert
      assertThat(id.equals(100L)).isFalse();
      assertThat(id.equals("100")).isFalse();
    }

    @Test
    @DisplayName("EMPTYが非空のSnowflakeIdと等しくないこと")
    void emptyShouldNotBeEqualToNonEmpty() {
      // Arrange
      SnowflakeId id = SnowflakeId.of(100L);

      // Act & Assert
      assertThat(SnowflakeId.EMPTY).isNotEqualTo(id);
      assertThat(id).isNotEqualTo(SnowflakeId.EMPTY);
    }
  }

  @Nested
  @DisplayName("hashCodeメソッドのテスト")
  class HashCodeMethod {

    @Test
    @DisplayName("同じ値の別インスタンスは同じhashCodeを返すこと")
    void shouldHaveSameHashCodeWithSameValue() {
      // Arrange
      SnowflakeId id1 = SnowflakeId.of(100L);
      SnowflakeId id2 = SnowflakeId.of(100L);

      // Act & Assert
      assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    @DisplayName("EMPTY同士は同じhashCodeを返すこと")
    void emptyShouldHaveSameHashCode() {
      // Act & Assert
      assertThat(SnowflakeId.EMPTY.hashCode()).isEqualTo(SnowflakeId.empty().hashCode());
    }

    @Test
    @DisplayName("new SnowflakeId(null)で生成したインスタンスはEMPTYと同じhashCodeを返すこと")
    void reconstructNullShouldHaveSameHashCodeAsEmpty() {
      // Arrange
      SnowflakeId reconstructed = SnowflakeId.reconstruct(null);

      // Act & Assert
      assertThat(reconstructed.hashCode()).isEqualTo(SnowflakeId.EMPTY.hashCode());
    }
  }
}
