package com.github.mizo432.shared.sample.jpa.entity.embeddable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("SampleAddressの単体テスト")
class SampleAddressTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数で住所インスタンスが作成される")
    void shouldCreateAddressWithValidArguments() {
      // Arrange & Act
      SampleAddress address = new SampleAddress("日本", "東京都", "渋谷区", "神宮前1-2-3");

      // Assert
      assertThat(address).isNotNull();
      assertThat(address.getCountry()).isEqualTo("日本");
      assertThat(address.getPrefecture()).isEqualTo("東京都");
      assertThat(address.getCity()).isEqualTo("渋谷区");
      assertThat(address.getStreet()).isEqualTo("神宮前1-2-3");
    }

    @Test
    @DisplayName("デフォルトコンストラクタでnullフィールドのインスタンスが作成される")
    void shouldCreateAddressWithDefaultConstructor() {
      // Arrange & Act
      SampleAddress address = new SampleAddress();

      // Assert
      assertThat(address.getCountry()).isNull();
      assertThat(address.getPrefecture()).isNull();
      assertThat(address.getCity()).isNull();
      assertThat(address.getStreet()).isNull();
    }
  }

  @Nested
  @DisplayName("ofファクトリメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("ofメソッドで住所インスタンスが作成される")
    void shouldCreateAddressWithOfMethod() {
      // Act
      SampleAddress address = SampleAddress.of("日本", "大阪府", "大阪市", "梅田1-1-1");

      // Assert
      assertThat(address.getCountry()).isEqualTo("日本");
      assertThat(address.getPrefecture()).isEqualTo("大阪府");
      assertThat(address.getCity()).isEqualTo("大阪市");
      assertThat(address.getStreet()).isEqualTo("梅田1-1-1");
    }

    @Test
    @DisplayName("null引数でもインスタンスが作成される")
    void shouldCreateAddressWithNullArguments() {
      // Act
      SampleAddress address = SampleAddress.of(null, null, null, null);

      // Assert
      assertThat(address).isNotNull();
      assertThat(address.getCountry()).isNull();
    }
  }

  @Nested
  @DisplayName("equalsとhashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値を持つインスタンスは等しい")
    void shouldBeEqualWhenSameValue() {
      // Arrange
      SampleAddress first = new SampleAddress("日本", "東京都", "渋谷区", "神宮前1-2-3");
      SampleAddress second = new SampleAddress("日本", "東京都", "渋谷区", "神宮前1-2-3");

      // Assert
      assertThat(first).isEqualTo(second);
      assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    @DisplayName("異なる値を持つインスタンスは等しくない")
    void shouldNotBeEqualWhenDifferentValue() {
      // Arrange
      SampleAddress first = new SampleAddress("日本", "東京都", "渋谷区", "神宮前1-2-3");
      SampleAddress second = new SampleAddress("日本", "大阪府", "大阪市", "梅田1-1-1");

      // Assert
      assertThat(first).isNotEqualTo(second);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringTest {

    @Test
    @DisplayName("各フィールドをスペース区切りで文字列として返す")
    void shouldReturnFieldsJoinedBySpace() {
      // Arrange
      SampleAddress address = new SampleAddress("日本", "東京都", "渋谷区", "神宮前1-2-3");

      // Act & Assert
      assertThat(address.toString()).isEqualTo("日本 東京都 渋谷区 神宮前1-2-3");
    }

    @Test
    @DisplayName("nullフィールドはnull文字列として出力される")
    void shouldReturnNullStringForNullFields() {
      // Arrange
      SampleAddress address = new SampleAddress();

      // Act & Assert
      assertThat(address.toString()).isEqualTo("null null null null");
    }
  }
}
