package undecided.generic.sampleBatch.internal.batch.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("SampleItemCsvDtoのテスト")
class SampleItemCsvDtoTest {

  @Nested
  @DisplayName("フィールドのgetter/setterテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("すべてのフィールドのgetter/setterが正常に動作すること")
    void shouldGetAndSetAllFields() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();

      // Act
      dto.setItemCode("ITEM001");
      dto.setItemName("サンプル商品A");
      dto.setCategory("食品");
      dto.setPrice("1000");

      // Assert
      assertThat(dto.getItemCode()).isEqualTo("ITEM001");
      assertThat(dto.getItemName()).isEqualTo("サンプル商品A");
      assertThat(dto.getCategory()).isEqualTo("食品");
      assertThat(dto.getPrice()).isEqualTo("1000");
    }

    @Test
    @DisplayName("デフォルトではすべてのフィールドがnullであること")
    void shouldHaveNullFieldsByDefault() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto();

      // Assert
      assertThat(dto.getItemCode()).isNull();
      assertThat(dto.getItemName()).isNull();
      assertThat(dto.getCategory()).isNull();
      assertThat(dto.getPrice()).isNull();
    }

    @Test
    @DisplayName("全引数コンストラクタで初期化できること")
    void shouldInitializeWithAllArgsConstructor() {
      // Act
      SampleItemCsvDto dto = new SampleItemCsvDto("ITEM001", "サンプル商品A", "食品", "1000");

      // Assert
      assertThat(dto.getItemCode()).isEqualTo("ITEM001");
      assertThat(dto.getItemName()).isEqualTo("サンプル商品A");
      assertThat(dto.getCategory()).isEqualTo("食品");
      assertThat(dto.getPrice()).isEqualTo("1000");
    }
  }

  @Nested
  @DisplayName("EqualsAndHashCodeのテスト")
  class EqualsAndHashCodeTest {

    @Test
    @DisplayName("同じ値のDTOは等しいと判定されること")
    void shouldBeEqualWhenValuesMatch() {
      // Arrange
      SampleItemCsvDto dto1 = new SampleItemCsvDto("ITEM001", "商品A", "食品", "1000");
      SampleItemCsvDto dto2 = new SampleItemCsvDto("ITEM001", "商品A", "食品", "1000");

      // Assert
      assertThat(dto1).isEqualTo(dto2);
      assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    @DisplayName("異なる値のDTOは等しくないこと")
    void shouldNotBeEqualWhenValuesDiffer() {
      // Arrange
      SampleItemCsvDto dto1 = new SampleItemCsvDto("ITEM001", "商品A", "食品", "1000");
      SampleItemCsvDto dto2 = new SampleItemCsvDto("ITEM002", "商品B", "飲料", "2000");

      // Assert
      assertThat(dto1).isNotEqualTo(dto2);
    }

    @Test
    @DisplayName("nullフィールドを含むDTOも等価比較できること")
    void shouldBeEqualWithNullFields() {
      // Arrange
      SampleItemCsvDto dto1 = new SampleItemCsvDto("ITEM001", null, null, null);
      SampleItemCsvDto dto2 = new SampleItemCsvDto("ITEM001", null, null, null);

      // Assert
      assertThat(dto1).isEqualTo(dto2);
    }
  }

  @Nested
  @DisplayName("ToStringのテスト")
  class ToStringTest {

    @Test
    @DisplayName("toStringがフィールド名を含む文字列を返すこと")
    void shouldReturnStringWithFieldNames() {
      // Arrange
      SampleItemCsvDto dto = new SampleItemCsvDto("ITEM001", "商品A", "食品", "1000");

      // Act
      String result = dto.toString();

      // Assert
      assertThat(result).contains("ITEM001");
      assertThat(result).contains("商品A");
      assertThat(result).contains("食品");
      assertThat(result).contains("1000");
    }
  }

  @Nested
  @DisplayName("Serializableのテスト")
  class SerializableTest {

    @Test
    @DisplayName("Serializableインターフェースを実装していること")
    void shouldImplementSerializable() {
      // Arrange & Assert
      assertThat(new SampleItemCsvDto()).isInstanceOf(java.io.Serializable.class);
    }
  }
}
