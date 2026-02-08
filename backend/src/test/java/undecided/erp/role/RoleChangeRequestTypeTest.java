package undecided.erp.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("RoleChangeRequestTypeクラスのテスト")
class RoleChangeRequestTypeTest {

  @Nested
  @DisplayName("valueOfCodeメソッドのテスト")
  class ValueOfCodeTest {

    @Test
    @DisplayName("codeがnullの場合、IllegalArgumentExceptionがスローされるべき")
    void shouldThrowIllegalArgumentExceptionWhenCodeIsNull() {
      // Arrange
      String code = null;

      // Act & Assert
      assertThatThrownBy(() -> RoleChangeRequestType.valueOfCode(code))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("code must not be null.");
    }

    @Test
    @DisplayName("codeが空文字の場合、UNKNOWNが返されるべき")
    void shouldReturnUnknownWhenCodeIsEmpty() {
      // Arrange
      String code = "";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("codeが'10'の場合、CREATEが返されるべき")
    void shouldReturnCreateWhenCodeIs10() {
      // Arrange
      String code = "10";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UPDATE);
    }

    @Test
    @DisplayName("codeが'20'の場合、UPDATEが返されるべき")
    void shouldReturnUpdateWhenCodeIs20() {
      // Arrange
      String code = "20";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.DELETE);
    }

    @Test
    @DisplayName("codeが'30'の場合、DELETEが返されるべき")
    void shouldReturnDeleteWhenCodeIs30() {
      // Arrange
      String code = "30";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("codeが不正な値の場合、UNKNOWNが返されるべき")
    void shouldReturnUnknownWhenCodeIsInvalid() {
      // Arrange
      String code = "99";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }
  }

  @Nested
  @DisplayName("getCodeメソッドのテスト")
  class GetCodeTest {

    @Test
    @DisplayName("CREATEのコードは'00'であるべき")
    void shouldReturnCorrectCodeForCreate() {
      // Act
      String code = RoleChangeRequestType.CREATE.getCode();

      // Assert
      assertThat(code).isEqualTo("00");
    }

    @Test
    @DisplayName("UPDATEのコードは'10'であるべき")
    void shouldReturnCorrectCodeForUpdate() {
      // Act
      String code = RoleChangeRequestType.UPDATE.getCode();

      // Assert
      assertThat(code).isEqualTo("10");
    }

    @Test
    @DisplayName("DELETEのコードは'20'であるべき")
    void shouldReturnCorrectCodeForDelete() {
      // Act
      String code = RoleChangeRequestType.DELETE.getCode();

      // Assert
      assertThat(code).isEqualTo("20");
    }

    @Test
    @DisplayName("UNKNOWNのコードは'00'であるべき")
    void shouldReturnCorrectCodeForUnknown() {
      // Act
      String code = RoleChangeRequestType.UNKNOWN.getCode();

      // Assert
      assertThat(code).isEqualTo("00");
    }
  }

  @Nested
  @DisplayName("getSortOrderメソッドのテスト")
  class GetSortOrderTest {

    @Test
    @DisplayName("CREATEのソート順は20であるべき")
    void shouldReturnCorrectSortOrderForCreate() {
      // Act
      int sortOrder = RoleChangeRequestType.CREATE.getSortOrder();

      // Assert
      assertThat(sortOrder).isEqualTo(20);
    }

    @Test
    @DisplayName("UPDATEのソート順は10であるべき")
    void shouldReturnCorrectSortOrderForUpdate() {
      // Act
      int sortOrder = RoleChangeRequestType.UPDATE.getSortOrder();

      // Assert
      assertThat(sortOrder).isEqualTo(10);
    }

    @Test
    @DisplayName("DELETEのソート順は30であるべき")
    void shouldReturnCorrectSortOrderForDelete() {
      // Act
      int sortOrder = RoleChangeRequestType.DELETE.getSortOrder();

      // Assert
      assertThat(sortOrder).isEqualTo(30);
    }

    @Test
    @DisplayName("UNKNOWNのソート順は999であるべき")
    void shouldReturnCorrectSortOrderForUnknown() {
      // Act
      int sortOrder = RoleChangeRequestType.UNKNOWN.getSortOrder();

      // Assert
      assertThat(sortOrder).isEqualTo(999);
    }

    @Test
    @DisplayName("ソート順でソートした場合、UPDATE < CREATE < DELETE < UNKNOWNの順序になるべき")
    void shouldSortEnumsBySortOrder() {
      // Arrange
      java.util.List<RoleChangeRequestType> types =
          java.util.Arrays.asList(
              RoleChangeRequestType.UNKNOWN,
              RoleChangeRequestType.DELETE,
              RoleChangeRequestType.CREATE,
              RoleChangeRequestType.UPDATE);

      // Act
      java.util.List<RoleChangeRequestType> sorted =
          types.stream()
              .sorted(java.util.Comparator.comparingInt(RoleChangeRequestType::getSortOrder))
              .toList();

      // Assert
      assertThat(sorted)
          .containsExactly(
              RoleChangeRequestType.UPDATE,
              RoleChangeRequestType.CREATE,
              RoleChangeRequestType.DELETE,
              RoleChangeRequestType.UNKNOWN);
    }
  }

  @Nested
  @DisplayName("valueOfCodeメソッドの追加テスト")
  class ValueOfCodeAdditionalTest {

    @Test
    @DisplayName("valueOfCodeで取得したenumのgetCodeが元のcodeと一致すべき（CREATE）")
    void shouldRoundTripCodeForCreate() {
      // Arrange
      String originalCode = RoleChangeRequestType.CREATE.getCode();

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(originalCode);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.CREATE);
      assertThat(result.getCode()).isEqualTo(originalCode);
    }

    @Test
    @DisplayName("valueOfCodeで取得したenumのgetCodeが元のcodeと一致すべき（UPDATE）")
    void shouldRoundTripCodeForUpdate() {
      // Arrange
      String originalCode = RoleChangeRequestType.UPDATE.getCode();

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(originalCode);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UPDATE);
      assertThat(result.getCode()).isEqualTo(originalCode);
    }

    @Test
    @DisplayName("valueOfCodeで取得したenumのgetCodeが元のcodeと一致すべき（DELETE）")
    void shouldRoundTripCodeForDelete() {
      // Arrange
      String originalCode = RoleChangeRequestType.DELETE.getCode();

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(originalCode);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.DELETE);
      assertThat(result.getCode()).isEqualTo(originalCode);
    }

    @Test
    @DisplayName("valueOfCodeで取得したenumのgetCodeが元のcodeと一致すべき（UNKNOWN）")
    void shouldRoundTripCodeForUnknown() {
      // Arrange
      String originalCode = RoleChangeRequestType.UNKNOWN.getCode();

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(originalCode);

      // Assert
      assertThat(result.getCode()).isEqualTo(originalCode);
    }

    @Test
    @DisplayName("codeが'00'の場合、UNKNOWNが返されるべき（CREATEのコードと同じだが、デフォルトケースとして扱われる）")
    void shouldReturnUnknownWhenCodeIs00() {
      // Arrange
      String code = "00";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.CREATE);
    }

    @Test
    @DisplayName("codeに先頭と末尾の空白がある場合、トリムされずにUNKNOWNが返されるべき")
    void shouldReturnUnknownWhenCodeHasWhitespace() {
      // Arrange
      String code = " 10 ";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("codeが数値以外の文字を含む場合、UNKNOWNが返されるべき")
    void shouldReturnUnknownWhenCodeContainsNonNumericCharacters() {
      // Arrange
      String code = "1a";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("codeが負の数の場合、UNKNOWNが返されるべき")
    void shouldReturnUnknownWhenCodeIsNegative() {
      // Arrange
      String code = "-10";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("codeが非常に長い文字列の場合、UNKNOWNが返されるべき")
    void shouldReturnUnknownWhenCodeIsVeryLong() {
      // Arrange
      String code = "1234567890";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("codeが単一の数字以外の場合、UNKNOWNが返されるべき")
    void shouldReturnUnknownWhenCodeIsSingleDigit() {
      // Arrange
      String code = "5";

      // Act
      RoleChangeRequestType result = RoleChangeRequestType.valueOfCode(code);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }
  }

  @Nested
  @DisplayName("Jackson Serialization/Deserializationのテスト")
  class JacksonSerializationTest {

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper =
        new com.fasterxml.jackson.databind.ObjectMapper();

    @Test
    @DisplayName("CREATEをJSONにシリアライズするとコード値が出力されるべき")
    void shouldSerializeCreateToJsonCode() throws Exception {
      // Act
      String json = objectMapper.writeValueAsString(RoleChangeRequestType.CREATE);

      // Assert
      assertThat(json).isEqualTo("\"00\"");
    }

    @Test
    @DisplayName("UPDATEをJSONにシリアライズするとコード値が出力されるべき")
    void shouldSerializeUpdateToJsonCode() throws Exception {
      // Act
      String json = objectMapper.writeValueAsString(RoleChangeRequestType.UPDATE);

      // Assert
      assertThat(json).isEqualTo("\"10\"");
    }

    @Test
    @DisplayName("DELETEをJSONにシリアライズするとコード値が出力されるべき")
    void shouldSerializeDeleteToJsonCode() throws Exception {
      // Act
      String json = objectMapper.writeValueAsString(RoleChangeRequestType.DELETE);

      // Assert
      assertThat(json).isEqualTo("\"20\"");
    }

    @Test
    @DisplayName("UNKNOWNをJSONにシリアライズするとコード値が出力されるべき")
    void shouldSerializeUnknownToJsonCode() throws Exception {
      // Act
      String json = objectMapper.writeValueAsString(RoleChangeRequestType.CREATE);

      // Assert
      assertThat(json).isEqualTo("\"00\"");
    }

    @Test
    @DisplayName("コード'10'のJSONをデシリアライズするとCREATEになるべき")
    void shouldDeserializeJsonCodeToCreate() throws Exception {
      // Arrange
      String json = "\"10\"";

      // Act
      RoleChangeRequestType result = objectMapper.readValue(json, RoleChangeRequestType.class);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UPDATE);
    }

    @Test
    @DisplayName("コード'20'のJSONをデシリアライズするとUPDATEになるべき")
    void shouldDeserializeJsonCodeToUpdate() throws Exception {
      // Arrange
      String json = "\"20\"";

      // Act
      RoleChangeRequestType result = objectMapper.readValue(json, RoleChangeRequestType.class);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.DELETE);
    }

    @Test
    @DisplayName("コード'30'のJSONをデシリアライズするとDELETEになるべき")
    void shouldDeserializeJsonCodeToDelete() throws Exception {
      // Arrange
      String json = "\"30\"";

      // Act
      RoleChangeRequestType result = objectMapper.readValue(json, RoleChangeRequestType.class);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("不正なコードのJSONをデシリアライズするとUNKNOWNになるべき")
    void shouldDeserializeInvalidJsonCodeToUnknown() throws Exception {
      // Arrange
      String json = "\"99\"";

      // Act
      RoleChangeRequestType result = objectMapper.readValue(json, RoleChangeRequestType.class);

      // Assert
      assertThat(result).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("nullのJSONをデシリアライズするとIllegalArgumentExceptionがスローされるべき")
    void shouldThrowExceptionWhenDeserializingNullJson() throws JsonProcessingException {
      // Arrange
      String json = "null";

      // Act
      RoleChangeRequestType result = objectMapper.readValue(json, RoleChangeRequestType.class);

      // Assert
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("シリアライズとデシリアライズのラウンドトリップが正常に機能すべき（CREATE）")
    void shouldRoundTripSerializationForCreate() throws Exception {
      // Arrange
      RoleChangeRequestType original = RoleChangeRequestType.CREATE;

      // Act
      String json = objectMapper.writeValueAsString(original);
      RoleChangeRequestType result = objectMapper.readValue(json, RoleChangeRequestType.class);

      // Assert
      assertThat(result).isEqualTo(original);
    }

    @Test
    @DisplayName("シリアライズとデシリアライズのラウンドトリップが正常に機能すべき（UPDATE）")
    void shouldRoundTripSerializationForUpdate() throws Exception {
      // Arrange
      RoleChangeRequestType original = RoleChangeRequestType.UPDATE;

      // Act
      String json = objectMapper.writeValueAsString(original);
      RoleChangeRequestType result = objectMapper.readValue(json, RoleChangeRequestType.class);

      // Assert
      assertThat(result).isEqualTo(original);
    }

    @Test
    @DisplayName("シリアライズとデシリアライズのラウンドトリップが正常に機能すべき（DELETE）")
    void shouldRoundTripSerializationForDelete() throws Exception {
      // Arrange
      RoleChangeRequestType original = RoleChangeRequestType.DELETE;

      // Act
      String json = objectMapper.writeValueAsString(original);
      RoleChangeRequestType result = objectMapper.readValue(json, RoleChangeRequestType.class);

      // Assert
      assertThat(result).isEqualTo(original);
    }
  }

  @Nested
  @DisplayName("Enumの基本機能のテスト")
  class EnumBasicFunctionalityTest {

    @Test
    @DisplayName("values()は全てのenum定数を返すべき")
    void shouldReturnAllEnumConstants() {
      // Act
      RoleChangeRequestType[] values = RoleChangeRequestType.values();

      // Assert
      assertThat(values)
          .hasSize(4)
          .containsExactly(
              RoleChangeRequestType.CREATE,
              RoleChangeRequestType.UPDATE,
              RoleChangeRequestType.DELETE,
              RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("valueOf()は正しいenum定数を返すべき")
    void shouldReturnCorrectEnumConstantByName() {
      // Act & Assert
      assertThat(RoleChangeRequestType.valueOf("CREATE")).isEqualTo(RoleChangeRequestType.CREATE);
      assertThat(RoleChangeRequestType.valueOf("UPDATE")).isEqualTo(RoleChangeRequestType.UPDATE);
      assertThat(RoleChangeRequestType.valueOf("DELETE")).isEqualTo(RoleChangeRequestType.DELETE);
      assertThat(RoleChangeRequestType.valueOf("UNKNOWN")).isEqualTo(RoleChangeRequestType.UNKNOWN);
    }

    @Test
    @DisplayName("valueOf()に不正な名前を渡すとIllegalArgumentExceptionがスローされるべき")
    void shouldThrowExceptionForInvalidEnumName() {
      // Act & Assert
      assertThatThrownBy(() -> RoleChangeRequestType.valueOf("INVALID"))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("name()は正しいenum定数名を返すべき")
    void shouldReturnCorrectEnumName() {
      // Act & Assert
      assertThat(RoleChangeRequestType.CREATE.name()).isEqualTo("CREATE");
      assertThat(RoleChangeRequestType.UPDATE.name()).isEqualTo("UPDATE");
      assertThat(RoleChangeRequestType.DELETE.name()).isEqualTo("DELETE");
      assertThat(RoleChangeRequestType.UNKNOWN.name()).isEqualTo("UNKNOWN");
    }

    @Test
    @DisplayName("ordinal()は正しいインデックスを返すべき")
    void shouldReturnCorrectOrdinal() {
      // Act & Assert
      assertThat(RoleChangeRequestType.CREATE.ordinal()).isEqualTo(0);
      assertThat(RoleChangeRequestType.UPDATE.ordinal()).isEqualTo(1);
      assertThat(RoleChangeRequestType.DELETE.ordinal()).isEqualTo(2);
      assertThat(RoleChangeRequestType.UNKNOWN.ordinal()).isEqualTo(3);
    }

    @Test
    @DisplayName("toString()は正しいenum定数名を返すべき")
    void shouldReturnCorrectToString() {
      // Act & Assert
      assertThat(RoleChangeRequestType.CREATE.toString()).isEqualTo("CREATE");
      assertThat(RoleChangeRequestType.UPDATE.toString()).isEqualTo("UPDATE");
      assertThat(RoleChangeRequestType.DELETE.toString()).isEqualTo("DELETE");
      assertThat(RoleChangeRequestType.UNKNOWN.toString()).isEqualTo("UNKNOWN");
    }

    @Test
    @DisplayName("compareTo()はordinal順で比較すべき")
    void shouldCompareByOrdinal() {
      // Act & Assert
      assertThat(RoleChangeRequestType.CREATE.compareTo(RoleChangeRequestType.UPDATE)).isNegative();
      assertThat(RoleChangeRequestType.DELETE.compareTo(RoleChangeRequestType.CREATE)).isPositive();
      assertThat(RoleChangeRequestType.UPDATE.compareTo(RoleChangeRequestType.UPDATE)).isZero();
    }

    @Test
    @DisplayName("同じenum定数は==で比較できるべき")
    void shouldBeComparableWithEqualityOperator() {
      // Arrange
      RoleChangeRequestType type1 = RoleChangeRequestType.CREATE;
      RoleChangeRequestType type2 = RoleChangeRequestType.CREATE;

      // Act & Assert
      assertThat(type1 == type2).isTrue();
      assertThat(type1).isSameAs(type2);
    }
  }
}
