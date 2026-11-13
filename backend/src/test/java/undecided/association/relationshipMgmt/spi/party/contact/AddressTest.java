package undecided.association.relationshipMgmt.spi.party.contact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Address値オブジェクトのテスト")
class AddressTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTest {

    @Test
    @DisplayName("有効な住所からAddressを正常に作成すること")
    void shouldCreateAddressFromValidString() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address).as("Addressインスタンスが作成されること").isNotNull();
      assertThat(address.value()).as("値が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
    }

    @Test
    @DisplayName("前後に空白がある住所をトリムして作成すること")
    void shouldCreateAddressWithTrimmedValue() {
      // Act
      Address address = Address.of(" 東京都渋谷区神宮前1-2-3 ", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("前後の空白がトリムされること").isEqualTo("東京都渋谷区神宮前1-2-3");
    }

    @Test
    @DisplayName("1文字の住所も正常に作成すること")
    void shouldCreateAddressWithOneCharacter() {
      // Act
      Address address = Address.of("ア", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("1文字の住所が作成されること").isEqualTo("ア");
    }

    @Test
    @DisplayName("200文字の住所も正常に作成すること")
    void shouldCreateAddressWithMaxLength() {
      // Arrange
      String address = "a".repeat(200);

      // Act
      Address addr = Address.of(address, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(addr.value()).as("200文字の住所が作成されること").isEqualTo(address);
    }

    @Test
    @DisplayName("nullが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAddressIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> Address.of(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("nullに対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAddressIsEmpty() {
      // Act & Assert
      assertThatThrownBy(() -> Address.of("", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("空白のみの文字列が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAddressIsBlank() {
      // Act & Assert
      assertThatThrownBy(() -> Address.of("   ", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("空白のみの文字列に対するエラーメッセージが含まれること")
          .hasMessageContaining("null");
    }

    @Test
    @DisplayName("201文字以上の住所が渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAddressIsTooLong() {
      // Arrange
      String address = "a".repeat(201);

      // Act & Assert
      assertThatThrownBy(() -> Address.of(address, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("文字数が多い場合のエラーメッセージが含まれること")
          .hasMessageContaining("200");
    }
  }

  @Nested
  @DisplayName("ofメソッド（prefName付き）のテスト")
  class OfMethodWithPrefNameTest {

    @Test
    @DisplayName("住所と都道府県名からAddressを正常に作成すること")
    void shouldCreateAddressWithPrefName() {
      // Act
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("渋谷区神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
    }

    @Test
    @DisplayName("prefNameがnullの場合、prefNameなしのAddressが作成されること")
    void shouldCreateAddressWithNullPrefName() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名がnullであること").isNull();
    }

    @Test
    @DisplayName("prefNameの前後の空白がトリムされること")
    void shouldTrimPrefName() {
      // Act
      Address address = Address.of("渋谷区神宮前1-2-3", " 東京都 ", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPrefName()).as("都道府県名がトリムされること").isEqualTo("東京都");
    }

    @Test
    @DisplayName("10文字のprefNameも正常に作成すること")
    void shouldCreateAddressWithMaxPrefNameLength() {
      // Arrange
      String prefName = "a".repeat(10);

      // Act
      Address address = Address.of("住所", prefName, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPrefName()).as("10文字のprefNameが作成されること").isEqualTo(prefName);
    }

    @Test
    @DisplayName("11文字以上のprefNameが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPrefNameIsTooLong() {
      // Arrange
      String prefName = "a".repeat(11);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", prefName, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("prefNameが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("10");
    }
  }

  @Nested
  @DisplayName("ofメソッド（prefNameとprefKana付き）のテスト")
  class OfMethodWithPrefNameAndPrefKanaTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かなからAddressを正常に作成すること")
    void shouldCreateAddressWithPrefNameAndPrefKana() {
      // Act
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("渋谷区神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
    }

    @Test
    @DisplayName("prefKanaがnullの場合、prefKanaなしのAddressが作成されること")
    void shouldCreateAddressWithNullPrefKana() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かながnullであること").isNull();
    }

    @Test
    @DisplayName("prefKanaの前後の空白がトリムされること")
    void shouldTrimPrefKana() {
      // Act
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", " トウキョウト ", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPrefKana()).as("都道府県名かながトリムされること").isEqualTo("トウキョウト");
    }

    @Test
    @DisplayName("15文字のprefKanaも正常に作成すること")
    void shouldCreateAddressWithMaxPrefKanaLength() {
      // Arrange
      String prefKana = "a".repeat(15);

      // Act
      Address address = Address.of("住所", "都道府県", prefKana, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPrefKana()).as("15文字のprefKanaが作成されること").isEqualTo(prefKana);
    }

    @Test
    @DisplayName("16文字以上のprefKanaが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPrefKanaIsTooLong() {
      // Arrange
      String prefKana = "a".repeat(16);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", prefKana, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("prefKanaが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("15");
    }
  }

  @Nested
  @DisplayName("ofメソッド（postalCode付き）のテスト")
  class OfMethodWithPostalCodeTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号からAddressを正常に作成すること")
    void shouldCreateAddressWithPostalCode() {
      // Act
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("渋谷区神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
    }

    @Test
    @DisplayName("postalCodeがnullの場合、postalCodeなしのAddressが作成されること")
    void shouldCreateAddressWithNullPostalCode() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getPostalCode()).as("郵便番号がnullであること").isNull();
    }

    @Test
    @DisplayName("postalCodeの前後の空白がトリムされること")
    void shouldTrimPostalCode() {
      // Act
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", " 123456 ", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPostalCode()).as("郵便番号がトリムされること").isEqualTo("123456");
    }

    @Test
    @DisplayName("8文字のpostalCodeも正常に作成すること")
    void shouldCreateAddressWithMaxPostalCodeLength() {
      // Arrange
      String postalCode = "123-4567";

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", postalCode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPostalCode()).as("8文字のpostalCodeが作成されること").isEqualTo(postalCode);
    }

    @Test
    @DisplayName("9文字以上のpostalCodeが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPostalCodeIsTooLong() {
      // Arrange
      String postalCode = "a".repeat(9);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", postalCode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("postalCodeが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("8");
    }
  }

  @Nested
  @DisplayName("ofメソッド（countryName付き）のテスト")
  class OfMethodWithCountryNameTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名からAddressを正常に作成すること")
    void shouldCreateAddressWithCountryName() {
      // Act
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("渋谷区神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
    }

    @Test
    @DisplayName("countryNameがnullの場合、countryNameなしのAddressが作成されること")
    void shouldCreateAddressWithNullCountryName() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getCountryName()).as("国名がnullであること").isNull();
    }

    @Test
    @DisplayName("countryNameの前後の空白がトリムされること")
    void shouldTrimCountryName() {
      // Act
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", " 日本 ", null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCountryName()).as("国名がトリムされること").isEqualTo("日本");
    }

    @Test
    @DisplayName("50文字のcountryNameも正常に作成すること")
    void shouldCreateAddressWithMaxCountryNameLength() {
      // Arrange
      String countryName = "a".repeat(50);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", countryName, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCountryName()).as("50文字のcountryNameが作成されること").isEqualTo(countryName);
    }

    @Test
    @DisplayName("51文字以上のcountryNameが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCountryNameIsTooLong() {
      // Arrange
      String countryName = "a".repeat(51);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", countryName, null, null, null, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("countryNameが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("50");
    }
  }

  @Nested
  @DisplayName("ofメソッド（countryKana付き）のテスト")
  class OfMethodWithCountryKanaTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名、国名かなからAddressを正常に作成すること")
    void shouldCreateAddressWithCountryKana() {
      // Act
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("渋谷区神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
      assertThat(address.getCountryKana()).as("国名かなが保持されること").isEqualTo("ニホン");
    }

    @Test
    @DisplayName("countryKanaがnullの場合、countryKanaなしのAddressが作成されること")
    void shouldCreateAddressWithNullCountryKana() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getCountryKana()).as("国名かながnullであること").isNull();
    }

    @Test
    @DisplayName("countryKanaの前後の空白がトリムされること")
    void shouldTrimCountryKana() {
      // Act
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", " ニホン ", null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCountryKana()).as("国名かながトリムされること").isEqualTo("ニホン");
    }

    @Test
    @DisplayName("50文字のcountryKanaも正常に作成すること")
    void shouldCreateAddressWithMaxCountryKanaLength() {
      // Arrange
      String countryKana = "a".repeat(50);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", countryKana, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCountryKana()).as("50文字のcountryKanaが作成されること").isEqualTo(countryKana);
    }

    @Test
    @DisplayName("51文字以上のcountryKanaが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCountryKanaIsTooLong() {
      // Arrange
      String countryKana = "a".repeat(51);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", countryKana, null, null, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("countryKanaが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("50");
    }
  }

  @Nested
  @DisplayName("ofメソッド（cityName付き）のテスト")
  class OfMethodWithCityNameTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名からAddressを正常に作成すること")
    void shouldCreateAddressWithCityName() {
      // Act
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
      assertThat(address.getCountryKana()).as("国名かなが保持されること").isEqualTo("ニホン");
      assertThat(address.getCityName()).as("市区町村名が保持されること").isEqualTo("渋谷区");
    }

    @Test
    @DisplayName("cityNameがnullの場合、cityNameなしのAddressが作成されること")
    void shouldCreateAddressWithNullCityName() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getCityName()).as("市区町村名がnullであること").isNull();
    }

    @Test
    @DisplayName("cityNameの前後の空白がトリムされること")
    void shouldTrimCityName() {
      // Act
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", " 渋谷区 ", null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCityName()).as("市区町村名がトリムされること").isEqualTo("渋谷区");
    }

    @Test
    @DisplayName("20文字のcityNameも正常に作成すること")
    void shouldCreateAddressWithMaxCityNameLength() {
      // Arrange
      String cityName = "a".repeat(20);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", cityName, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCityName()).as("20文字のcityNameが作成されること").isEqualTo(cityName);
    }

    @Test
    @DisplayName("21文字以上のcityNameが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCityNameIsTooLong() {
      // Arrange
      String cityName = "a".repeat(21);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", cityName, null, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("cityNameが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("ofメソッド（cityKana付き）のテスト")
  class OfMethodWithCityKanaTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かなからAddressを正常に作成すること")
    void shouldCreateAddressWithCityKana() {
      // Act
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
      assertThat(address.getCountryKana()).as("国名かなが保持されること").isEqualTo("ニホン");
      assertThat(address.getCityName()).as("市区町村名が保持されること").isEqualTo("渋谷区");
      assertThat(address.getCityKana()).as("市区町村名かなが保持されること").isEqualTo("シブヤク");
    }

    @Test
    @DisplayName("cityKanaがnullの場合、cityKanaなしのAddressが作成されること")
    void shouldCreateAddressWithNullCityKana() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getCityKana()).as("市区町村名かながnullであること").isNull();
    }

    @Test
    @DisplayName("cityKanaの前後の空白がトリムされること")
    void shouldTrimCityKana() {
      // Act
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", " シブヤク ", null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCityKana()).as("市区町村名かながトリムされること").isEqualTo("シブヤク");
    }

    @Test
    @DisplayName("20文字のcityKanaも正常に作成すること")
    void shouldCreateAddressWithMaxCityKanaLength() {
      // Arrange
      String cityKana = "a".repeat(20);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", cityKana, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCityKana()).as("20文字のcityKanaが作成されること").isEqualTo(cityKana);
    }

    @Test
    @DisplayName("21文字以上のcityKanaが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenCityKanaIsTooLong() {
      // Arrange
      String cityKana = "a".repeat(21);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", cityKana, null, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("cityKanaが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("ofメソッド（wardName付き）のテスト")
  class OfMethodWithWardNameTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名からAddressを正常に作成すること")
    void shouldCreateAddressWithWardName() {
      // Act
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
      assertThat(address.getCountryKana()).as("国名かなが保持されること").isEqualTo("ニホン");
      assertThat(address.getCityName()).as("市区町村名が保持されること").isEqualTo("渋谷区");
      assertThat(address.getCityKana()).as("市区町村名かなが保持されること").isEqualTo("シブヤク");
      assertThat(address.getWardName()).as("区名が保持されること").isEqualTo("神宮前");
    }

    @Test
    @DisplayName("wardNameがnullの場合、wardNameなしのAddressが作成されること")
    void shouldCreateAddressWithNullWardName() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getWardName()).as("区名がnullであること").isNull();
    }

    @Test
    @DisplayName("wardNameの前後の空白がトリムされること")
    void shouldTrimWardName() {
      // Act
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", " 神宮前 ", null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getWardName()).as("区名がトリムされること").isEqualTo("神宮前");
    }

    @Test
    @DisplayName("20文字のwardNameも正常に作成すること")
    void shouldCreateAddressWithMaxWardNameLength() {
      // Arrange
      String wardName = "a".repeat(20);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", wardName, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getWardName()).as("20文字のwardNameが作成されること").isEqualTo(wardName);
    }

    @Test
    @DisplayName("21文字以上のwardNameが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenWardNameIsTooLong() {
      // Arrange
      String wardName = "a".repeat(21);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", wardName, null, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("wardNameが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("ofメソッド（wardKana付き）のテスト")
  class OfMethodWithWardKanaTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かなからAddressを正常に作成すること")
    void shouldCreateAddressWithWardKana() {
      // Act
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("神宮前1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
      assertThat(address.getCountryKana()).as("国名かなが保持されること").isEqualTo("ニホン");
      assertThat(address.getCityName()).as("市区町村名が保持されること").isEqualTo("渋谷区");
      assertThat(address.getCityKana()).as("市区町村名かなが保持されること").isEqualTo("シブヤク");
      assertThat(address.getWardName()).as("区名が保持されること").isEqualTo("神宮前");
      assertThat(address.getWardKana()).as("区名かなが保持されること").isEqualTo("ジンミウマエ");
    }

    @Test
    @DisplayName("wardKanaがnullの場合、wardKanaなしのAddressが作成されること")
    void shouldCreateAddressWithNullWardKana() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getWardKana()).as("区名かながnullであること").isNull();
    }

    @Test
    @DisplayName("wardKanaの前後の空白がトリムされること")
    void shouldTrimWardKana() {
      // Act
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", " ジンミウマエ ", null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getWardKana()).as("区名かながトリムされること").isEqualTo("ジンミウマエ");
    }

    @Test
    @DisplayName("20文字のwardKanaも正常に作成すること")
    void shouldCreateAddressWithMaxWardKanaLength() {
      // Arrange
      String wardKana = "a".repeat(20);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", wardKana, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getWardKana()).as("20文字のwardKanaが作成されること").isEqualTo(wardKana);
    }

    @Test
    @DisplayName("21文字以上のwardKanaが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenWardKanaIsTooLong() {
      // Arrange
      String wardKana = "a".repeat(21);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", wardKana, null, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("wardKanaが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("ofメソッド（oazaChoName付き）のテスト")
  class OfMethodWithOazaChoNameTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かな、大字町名からAddressを正常に作成すること")
    void shouldCreateAddressWithOazaChoName() {
      // Act
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
      assertThat(address.getCountryKana()).as("国名かなが保持されること").isEqualTo("ニホン");
      assertThat(address.getCityName()).as("市区町村名が保持されること").isEqualTo("渋谷区");
      assertThat(address.getCityKana()).as("市区町村名かなが保持されること").isEqualTo("シブヤク");
      assertThat(address.getWardName()).as("区名が保持されること").isEqualTo("神宮前");
      assertThat(address.getWardKana()).as("区名かなが保持されること").isEqualTo("ジンミウマエ");
      assertThat(address.getOazaChoName()).as("大字町名が保持されること").isEqualTo("大字神宮前");
    }

    @Test
    @DisplayName("oazaChoNameがnullの場合、oazaChoNameなしのAddressが作成されること")
    void shouldCreateAddressWithNullOazaChoName() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getOazaChoName()).as("大字町名がnullであること").isNull();
    }

    @Test
    @DisplayName("oazaChoNameの前後の空白がトリムされること")
    void shouldTrimOazaChoName() {
      // Act
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", " 大字神宮前 ", null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getOazaChoName()).as("大字町名がトリムされること").isEqualTo("大字神宮前");
    }

    @Test
    @DisplayName("20文字のoazaChoNameも正常に作成すること")
    void shouldCreateAddressWithMaxOazaChoNameLength() {
      // Arrange
      String oazaChoName = "a".repeat(20);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", "区かな", oazaChoName, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getOazaChoName()).as("20文字のoazaChoNameが作成されること").isEqualTo(oazaChoName);
    }

    @Test
    @DisplayName("21文字以上のoazaChoNameが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenOazaChoNameIsTooLong() {
      // Arrange
      String oazaChoName = "a".repeat(21);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", "区かな", oazaChoName, null, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("oazaChoNameが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("ofメソッド（oazaChoKana付き）のテスト")
  class OfMethodWithOazaChoKanaTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かな、大字町名、大字町名かなからAddressを正常に作成すること")
    void shouldCreateAddressWithOazaChoKana() {
      // Act
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
      assertThat(address.getCountryKana()).as("国名かなが保持されること").isEqualTo("ニホン");
      assertThat(address.getCityName()).as("市区町村名が保持されること").isEqualTo("渋谷区");
      assertThat(address.getCityKana()).as("市区町村名かなが保持されること").isEqualTo("シブヤク");
      assertThat(address.getWardName()).as("区名が保持されること").isEqualTo("神宮前");
      assertThat(address.getWardKana()).as("区名かなが保持されること").isEqualTo("ジンミウマエ");
      assertThat(address.getOazaChoName()).as("大字町名が保持されること").isEqualTo("大字神宮前");
      assertThat(address.getOazaChoKana()).as("大字町名かなが保持されること").isEqualTo("オオアジジンミウマエ");
    }

    @Test
    @DisplayName("oazaChoKanaがnullの場合、oazaChoKanaなしのAddressが作成されること")
    void shouldCreateAddressWithNullOazaChoKana() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getOazaChoKana()).as("大字町名かながnullであること").isNull();
    }

    @Test
    @DisplayName("oazaChoKanaの前後の空白がトリムされること")
    void shouldTrimOazaChoKana() {
      // Act
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", " オオアジジンミウマエ ", null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getOazaChoKana()).as("大字町名かながトリムされること").isEqualTo("オオアジジンミウマエ");
    }

    @Test
    @DisplayName("20文字のoazaChoKanaも正常に作成すること")
    void shouldCreateAddressWithMaxOazaChoKanaLength() {
      // Arrange
      String oazaChoKana = "a".repeat(20);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", "区かな", "大字町名", oazaChoKana, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getOazaChoKana()).as("20文字のoazaChoKanaが作成されること").isEqualTo(oazaChoKana);
    }

    @Test
    @DisplayName("21文字以上のoazaChoKanaが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenOazaChoKanaIsTooLong() {
      // Arrange
      String oazaChoKana = "a".repeat(21);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", "区かな", "大字町名", oazaChoKana, null, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("oazaChoKanaが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("ofメソッド（chomeName付き）のテスト")
  class OfMethodWithChomeNameTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かな、大字町名、大字町名かな、丁目名からAddressを正常に作成すること")
    void shouldCreateAddressWithChomeName() {
      // Act
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
      assertThat(address.getCountryKana()).as("国名かなが保持されること").isEqualTo("ニホン");
      assertThat(address.getCityName()).as("市区町村名が保持されること").isEqualTo("渋谷区");
      assertThat(address.getCityKana()).as("市区町村名かなが保持されること").isEqualTo("シブヤク");
      assertThat(address.getWardName()).as("区名が保持されること").isEqualTo("神宮前");
      assertThat(address.getWardKana()).as("区名かなが保持されること").isEqualTo("ジンミウマエ");
      assertThat(address.getOazaChoName()).as("大字町名が保持されること").isEqualTo("大字神宮前");
      assertThat(address.getOazaChoKana()).as("大字町名かなが保持されること").isEqualTo("オオアジジンミウマエ");
      assertThat(address.getChomeName()).as("丁目名が保持されること").isEqualTo("一丁目");
    }

    @Test
    @DisplayName("chomeNameがnullの場合、chomeNameなしのAddressが作成されること")
    void shouldCreateAddressWithNullChomeName() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", null, null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getChomeName()).as("丁目名がnullであること").isNull();
    }

    @Test
    @DisplayName("chomeNameの前後の空白がトリムされること")
    void shouldTrimChomeName() {
      // Act
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", " 一丁目 ", null, null, null, null, null, null);

      // Assert
      assertThat(address.getChomeName()).as("丁目名がトリムされること").isEqualTo("一丁目");
    }

    @Test
    @DisplayName("20文字のchomeNameも正常に作成すること")
    void shouldCreateAddressWithMaxChomeNameLength() {
      // Arrange
      String chomeName = "a".repeat(20);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", "区かな", "大字町名", "大字かな", chomeName, null, null, null, null, null, null);

      // Assert
      assertThat(address.getChomeName()).as("20文字のchomeNameが作成されること").isEqualTo(chomeName);
    }

    @Test
    @DisplayName("21文字以上のchomeNameが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenChomeNameIsTooLong() {
      // Arrange
      String chomeName = "a".repeat(21);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", "区かな", "大字町名", "大字かな", chomeName, null, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("chomeNameが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("ofメソッド（chomeKana付き）のテスト")
  class OfMethodWithChomeKanaTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かな、大字町名、大字町名かな、丁目名、丁目名かなからAddressを正常に作成すること")
    void shouldCreateAddressWithChomeKana() {
      // Act
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
      assertThat(address.getCountryKana()).as("国名かなが保持されること").isEqualTo("ニホン");
      assertThat(address.getCityName()).as("市区町村名が保持されること").isEqualTo("渋谷区");
      assertThat(address.getCityKana()).as("市区町村名かなが保持されること").isEqualTo("シブヤク");
      assertThat(address.getWardName()).as("区名が保持されること").isEqualTo("神宮前");
      assertThat(address.getWardKana()).as("区名かなが保持されること").isEqualTo("ジンミウマエ");
      assertThat(address.getOazaChoName()).as("大字町名が保持されること").isEqualTo("大字神宮前");
      assertThat(address.getOazaChoKana()).as("大字町名かなが保持されること").isEqualTo("オオアジジンミウマエ");
      assertThat(address.getChomeName()).as("丁目名が保持されること").isEqualTo("一丁目");
      assertThat(address.getChomeKana()).as("丁目名かなが保持されること").isEqualTo("イッチョウメ");
    }

    @Test
    @DisplayName("chomeKanaがnullの場合、chomeKanaなしのAddressが作成されること")
    void shouldCreateAddressWithNullChomeKana() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", null, null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getChomeKana()).as("丁目名かながnullであること").isNull();
    }

    @Test
    @DisplayName("chomeKanaの前後の空白がトリムされること")
    void shouldTrimChomeKana() {
      // Act
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", " イッチョウメ ", null, null, null, null, null);

      // Assert
      assertThat(address.getChomeKana()).as("丁目名かながトリムされること").isEqualTo("イッチョウメ");
    }

    @Test
    @DisplayName("20文字のchomeKanaも正常に作成すること")
    void shouldCreateAddressWithMaxChomeKanaLength() {
      // Arrange
      String chomeKana = "a".repeat(20);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", "区かな", "大字町名", "大字かな", "丁目名", chomeKana, null, null, null, null, null);

      // Assert
      assertThat(address.getChomeKana()).as("20文字のchomeKanaが作成されること").isEqualTo(chomeKana);
    }

    @Test
    @DisplayName("21文字以上のchomeKanaが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenChomeKanaIsTooLong() {
      // Arrange
      String chomeKana = "a".repeat(21);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", "区かな", "大字町名", "大字かな", "丁目名", chomeKana, null, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("chomeKanaが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("ofメソッド（koazaName付き）のテスト")
  class OfMethodWithKoazaNameTest {

    @Test
    @DisplayName("住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かな、大字町名、大字町名かな、丁目名、丁目名かな、小字名からAddressを正常に作成すること")
    void shouldCreateAddressWithKoazaName() {
      // Act
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("1-2-3");
      assertThat(address.getPrefName()).as("都道府県名が保持されること").isEqualTo("東京都");
      assertThat(address.getPrefKana()).as("都道府県名かなが保持されること").isEqualTo("トウキョウト");
      assertThat(address.getPostalCode()).as("郵便番号が保持されること").isEqualTo("150-0001");
      assertThat(address.getCountryName()).as("国名が保持されること").isEqualTo("日本");
      assertThat(address.getCountryKana()).as("国名かなが保持されること").isEqualTo("ニホン");
      assertThat(address.getCityName()).as("市区町村名が保持されること").isEqualTo("渋谷区");
      assertThat(address.getCityKana()).as("市区町村名かなが保持されること").isEqualTo("シブヤク");
      assertThat(address.getWardName()).as("区名が保持されること").isEqualTo("神宮前");
      assertThat(address.getWardKana()).as("区名かなが保持されること").isEqualTo("ジンミウマエ");
      assertThat(address.getOazaChoName()).as("大字町名が保持されること").isEqualTo("大字神宮前");
      assertThat(address.getOazaChoKana()).as("大字町名かなが保持されること").isEqualTo("オオアジジンミウマエ");
      assertThat(address.getChomeName()).as("丁目名が保持されること").isEqualTo("一丁目");
      assertThat(address.getChomeKana()).as("丁目名かなが保持されること").isEqualTo("イッチョウメ");
      assertThat(address.getKoazaName()).as("小字名が保持されること").isEqualTo("小字神宮前");
    }

    @Test
    @DisplayName("koazaNameがnullの場合、koazaNameなしのAddressが作成されること")
    void shouldCreateAddressWithNullKoazaName() {
      // Act
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", null, null, null, null, null);

      // Assert
      assertThat(address.value()).as("住所が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      assertThat(address.getKoazaName()).as("小字名がnullであること").isNull();
    }

    @Test
    @DisplayName("koazaNameの前後の空白がトリムされること")
    void shouldTrimKoazaName() {
      // Act
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", " 小字神宮前 ", null, null, null, null);

      // Assert
      assertThat(address.getKoazaName()).as("小字名がトリムされること").isEqualTo("小字神宮前");
    }

    @Test
    @DisplayName("20文字のkoazaNameも正常に作成すること")
    void shouldCreateAddressWithMaxKoazaNameLength() {
      // Arrange
      String koazaName = "a".repeat(20);

      // Act
      Address address = Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", "区かな", "大字町名", "大字かな", "丁目名", "丁目かな", koazaName, null, null, null, null);

      // Assert
      assertThat(address.getKoazaName()).as("20文字のkoazaNameが作成されること").isEqualTo(koazaName);
    }

    @Test
    @DisplayName("21文字以上のkoazaNameが渡された場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenKoazaNameIsTooLong() {
      // Arrange
      String koazaName = "a".repeat(21);

      // Act & Assert
      assertThatThrownBy(() -> Address.of("住所", "都道府県", "トウキョウト", "123-4567", "国名", "国かな", "市区町村", "市区かな", "区名", "区かな", "大字町名", "大字かな", "丁目名", "丁目かな", koazaName, null, null, null, null))
          .isInstanceOf(IllegalArgumentException.class)
          .as("koazaNameが長い場合のエラーメッセージが含まれること")
          .hasMessageContaining("20");
    }
  }

  @Nested
  @DisplayName("emptyメソッドのテスト")
  class EmptyMethodTest {

    @Test
    @DisplayName("空のAddressインスタンスが作成されること")
    void shouldCreateEmptyAddress() {
      // Act
      Address address = Address.empty();

      // Assert
      assertThat(address).as("Addressインスタンスが作成されること").isNotNull();
      assertThat(address.value()).as("値がnullであること").isNull();
      assertThat(address.getPrefName()).as("prefNameがnullであること").isNull();
      assertThat(address.getPrefKana()).as("prefKanaがnullであること").isNull();
      assertThat(address.getPostalCode()).as("postalCodeがnullであること").isNull();
      assertThat(address.getCountryName()).as("countryNameがnullであること").isNull();
      assertThat(address.getCountryKana()).as("countryKanaがnullであること").isNull();
      assertThat(address.getCityName()).as("cityNameがnullであること").isNull();
      assertThat(address.getCityKana()).as("cityKanaがnullであること").isNull();
      assertThat(address.getWardName()).as("wardNameがnullであること").isNull();
      assertThat(address.getWardKana()).as("wardKanaがnullであること").isNull();
      assertThat(address.getOazaChoName()).as("oazaChoNameがnullであること").isNull();
      assertThat(address.getOazaChoKana()).as("oazaChoKanaがnullであること").isNull();
      assertThat(address.getChomeName()).as("chomeNameがnullであること").isNull();
      assertThat(address.getChomeKana()).as("chomeKanaがnullであること").isNull();
      assertThat(address.getKoazaName()).as("koazaNameがnullであること").isNull();
      assertThat(address.getKoazaKana()).as("koazaKanaがnullであること").isNull();
    }
  }

  @Nested
  @DisplayName("isEmptyメソッドのテスト")
  class IsEmptyMethodTest {

    @Test
    @DisplayName("空のAddressはisEmptyがtrueを返すこと")
    void shouldReturnTrueWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.isEmpty()).as("空のAddressはtrueを返すこと").isTrue();
    }

    @Test
    @DisplayName("値を持つAddressはisEmptyがfalseを返すこと")
    void shouldReturnFalseWhenNotEmpty() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.isEmpty()).as("値を持つAddressはfalseを返すこと").isFalse();
    }
  }

  @Nested
  @DisplayName("getPrefNameメソッドのテスト")
  class GetPrefNameMethodTest {

    @Test
    @DisplayName("prefNameを設定したAddressは都道府県名を返すこと")
    void shouldReturnPrefName() {
      // Arrange
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPrefName()).isEqualTo("東京都");
    }

    @Test
    @DisplayName("prefNameを設定しないAddressはnullを返すこと")
    void shouldReturnNullPrefName() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPrefName()).isNull();
    }

    @Test
    @DisplayName("空のAddressはprefNameがnullであること")
    void shouldReturnNullPrefNameWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getPrefName()).isNull();
    }
  }

  @Nested
  @DisplayName("getPrefKanaメソッドのテスト")
  class GetPrefKanaMethodTest {

    @Test
    @DisplayName("prefKanaを設定したAddressは都道府県名かなを返すこと")
    void shouldReturnPrefKana() {
      // Arrange
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPrefKana()).isEqualTo("トウキョウト");
    }

    @Test
    @DisplayName("prefKanaを設定しないAddressはnullを返すこと")
    void shouldReturnNullPrefKana() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPrefKana()).isNull();
    }

    @Test
    @DisplayName("空のAddressはprefKanaがnullであること")
    void shouldReturnNullPrefKanaWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getPrefKana()).isNull();
    }
  }

  @Nested
  @DisplayName("getPostalCodeメソッドのテスト")
  class GetPostalCodeMethodTest {

    @Test
    @DisplayName("postalCodeを設定したAddressは郵便番号を返すこと")
    void shouldReturnPostalCode() {
      // Arrange
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPostalCode()).isEqualTo("150-0001");
    }

    @Test
    @DisplayName("postalCodeを設定しないAddressはnullを返すこと")
    void shouldReturnNullPostalCode() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getPostalCode()).isNull();
    }

    @Test
    @DisplayName("空のAddressはpostalCodeがnullであること")
    void shouldReturnNullPostalCodeWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getPostalCode()).isNull();
    }
  }

  @Nested
  @DisplayName("getCountryNameメソッドのテスト")
  class GetCountryNameMethodTest {

    @Test
    @DisplayName("countryNameを設定したAddressは国名を返すこと")
    void shouldReturnCountryName() {
      // Arrange
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCountryName()).isEqualTo("日本");
    }

    @Test
    @DisplayName("countryNameを設定しないAddressはnullを返すこと")
    void shouldReturnNullCountryName() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCountryName()).isNull();
    }

    @Test
    @DisplayName("空のAddressはcountryNameがnullであること")
    void shouldReturnNullCountryNameWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getCountryName()).isNull();
    }
  }

  @Nested
  @DisplayName("getCountryKanaメソッドのテスト")
  class GetCountryKanaMethodTest {

    @Test
    @DisplayName("countryKanaを設定したAddressは国名かなを返すこと")
    void shouldReturnCountryKana() {
      // Arrange
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCountryKana()).isEqualTo("ニホン");
    }

    @Test
    @DisplayName("countryKanaを設定しないAddressはnullを返すこと")
    void shouldReturnNullCountryKana() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCountryKana()).isNull();
    }

    @Test
    @DisplayName("空のAddressはcountryKanaがnullであること")
    void shouldReturnNullCountryKanaWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getCountryKana()).isNull();
    }
  }

  @Nested
  @DisplayName("getCityNameメソッドのテスト")
  class GetCityNameMethodTest {

    @Test
    @DisplayName("cityNameを設定したAddressは市区町村名を返すこと")
    void shouldReturnCityName() {
      // Arrange
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCityName()).isEqualTo("渋谷区");
    }

    @Test
    @DisplayName("cityNameを設定しないAddressはnullを返すこと")
    void shouldReturnNullCityName() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCityName()).isNull();
    }

    @Test
    @DisplayName("空のAddressはcityNameがnullであること")
    void shouldReturnNullCityNameWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getCityName()).isNull();
    }
  }

  @Nested
  @DisplayName("getCityKanaメソッドのテスト")
  class GetCityKanaMethodTest {

    @Test
    @DisplayName("cityKanaを設定したAddressは市区町村名かなを返すこと")
    void shouldReturnCityKana() {
      // Arrange
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCityKana()).isEqualTo("シブヤク");
    }

    @Test
    @DisplayName("cityKanaを設定しないAddressはnullを返すこと")
    void shouldReturnNullCityKana() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getCityKana()).isNull();
    }

    @Test
    @DisplayName("空のAddressはcityKanaがnullであること")
    void shouldReturnNullCityKanaWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getCityKana()).isNull();
    }
  }

  @Nested
  @DisplayName("getWardNameメソッドのテスト")
  class GetWardNameMethodTest {

    @Test
    @DisplayName("wardNameを設定したAddressは区名を返すこと")
    void shouldReturnWardName() {
      // Arrange
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getWardName()).isEqualTo("神宮前");
    }

    @Test
    @DisplayName("wardNameを設定しないAddressはnullを返すこと")
    void shouldReturnNullWardName() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getWardName()).isNull();
    }

    @Test
    @DisplayName("空のAddressはwardNameがnullであること")
    void shouldReturnNullWardNameWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getWardName()).isNull();
    }
  }

  @Nested
  @DisplayName("getWardKanaメソッドのテスト")
  class GetWardKanaMethodTest {

    @Test
    @DisplayName("wardKanaを設定したAddressは区名かなを返すこと")
    void shouldReturnWardKana() {
      // Arrange
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getWardKana()).isEqualTo("ジンミウマエ");
    }

    @Test
    @DisplayName("wardKanaを設定しないAddressはnullを返すこと")
    void shouldReturnNullWardKana() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getWardKana()).isNull();
    }

    @Test
    @DisplayName("空のAddressはwardKanaがnullであること")
    void shouldReturnNullWardKanaWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getWardKana()).isNull();
    }
  }

  @Nested
  @DisplayName("getOazaChoNameメソッドのテスト")
  class GetOazaChoNameMethodTest {

    @Test
    @DisplayName("oazaChoNameを設定したAddressは大字町名を返すこと")
    void shouldReturnOazaChoName() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getOazaChoName()).isEqualTo("大字神宮前");
    }

    @Test
    @DisplayName("oazaChoNameを設定しないAddressはnullを返すこと")
    void shouldReturnNullOazaChoName() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getOazaChoName()).isNull();
    }

    @Test
    @DisplayName("空のAddressはoazaChoNameがnullであること")
    void shouldReturnNullOazaChoNameWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getOazaChoName()).isNull();
    }
  }

  @Nested
  @DisplayName("getOazaChoKanaメソッドのテスト")
  class GetOazaChoKanaMethodTest {

    @Test
    @DisplayName("oazaChoKanaを設定したAddressは大字町名かなを返すこと")
    void shouldReturnOazaChoKana() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getOazaChoKana()).isEqualTo("オオアジジンミウマエ");
    }

    @Test
    @DisplayName("oazaChoKanaを設定しないAddressはnullを返すこと")
    void shouldReturnNullOazaChoKana() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getOazaChoKana()).isNull();
    }

    @Test
    @DisplayName("空のAddressはoazaChoKanaがnullであること")
    void shouldReturnNullOazaChoKanaWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getOazaChoKana()).isNull();
    }
  }

  @Nested
  @DisplayName("getChomeNameメソッドのテスト")
  class GetChomeNameMethodTest {

    @Test
    @DisplayName("chomeNameを設定したAddressは丁目名を返すこと")
    void shouldReturnChomeName() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", null, null, null, null, null, null);

      // Assert
      assertThat(address.getChomeName()).isEqualTo("一丁目");
    }

    @Test
    @DisplayName("chomeNameを設定しないAddressはnullを返すこと")
    void shouldReturnNullChomeName() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", null, null, null, null, null, null, null);

      // Assert
      assertThat(address.getChomeName()).isNull();
    }

    @Test
    @DisplayName("空のAddressはchomeNameがnullであること")
    void shouldReturnNullChomeNameWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getChomeName()).isNull();
    }
  }

  @Nested
  @DisplayName("getChomeKanaメソッドのテスト")
  class GetChomeKanaMethodTest {

    @Test
    @DisplayName("chomeKanaを設定したAddressは丁目名かなを返すこと")
    void shouldReturnChomeKana() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", null, null, null, null, null);

      // Assert
      assertThat(address.getChomeKana()).isEqualTo("イッチョウメ");
    }

    @Test
    @DisplayName("chomeKanaを設定しないAddressはnullを返すこと")
    void shouldReturnNullChomeKana() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", null, null, null, null, null, null);

      // Assert
      assertThat(address.getChomeKana()).isNull();
    }

    @Test
    @DisplayName("空のAddressはchomeKanaがnullであること")
    void shouldReturnNullChomeKanaWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getChomeKana()).isNull();
    }
  }

  @Nested
  @DisplayName("getKoazaNameメソッドのテスト")
  class GetKoazaNameMethodTest {

    @Test
    @DisplayName("koazaNameを設定したAddressは小字名を返すこと")
    void shouldReturnKoazaName() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", null, null, null, null);

      // Assert
      assertThat(address.getKoazaName()).isEqualTo("小字神宮前");
    }

    @Test
    @DisplayName("koazaNameを設定しないAddressはnullを返すこと")
    void shouldReturnNullKoazaName() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", null, null, null, null, null);

      // Assert
      assertThat(address.getKoazaName()).isNull();
    }

    @Test
    @DisplayName("空のAddressはkoazaNameがnullであること")
    void shouldReturnNullKoazaNameWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getKoazaName()).isNull();
    }
  }

  @Nested
  @DisplayName("getKoazaKanaメソッドのテスト")
  class GetKoazaKanaMethodTest {

    @Test
    @DisplayName("koazaKanaを設定したAddressは小字名かなを返すこと")
    void shouldReturnKoazaKana() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", "コウアジジンミウマエ", null, null, null);

      // Assert
      assertThat(address.getKoazaKana()).isEqualTo("コウアジジンミウマエ");
    }

    @Test
    @DisplayName("koazaKanaを設定しないAddressはnullを返すこと")
    void shouldReturnNullKoazaKana() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", null, null, null, null);

      // Assert
      assertThat(address.getKoazaKana()).isNull();
    }

    @Test
    @DisplayName("空のAddressはkoazaKanaがnullであること")
    void shouldReturnNullKoazaKanaWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.getKoazaKana()).isNull();
    }
  }

  @Nested
  @DisplayName("等価性のテスト")
  class EqualityTest {

    @Test
    @DisplayName("同じ値とprefNameのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefName() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("渋谷区神宮前1-2-3", "東京都", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値とprefNameのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("値が異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentValue() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("新宿区新宿1-2-3", "東京都", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("値が異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("prefNameが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentPrefName() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("渋谷区神宮前1-2-3", "神奈川県", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("prefNameが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKanaのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKana() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKanaのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("prefKanaが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentPrefKana() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("渋谷区神宮前1-2-3", "東京都", "カナガワケン", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("prefKanaが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCodeのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCode() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCodeのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("postalCodeが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentPostalCode() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "160-0022", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("postalCodeが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryNameのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryName() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryNameのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("countryNameが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentCountryName() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", null, null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "アメリカ", null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("countryNameが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKanaのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKana() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKanaのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("countryKanaが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentCountryKana() {
      // Arrange
      Address first = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", null, null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "アメリカ", null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("countryKanaが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityNameのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKanaAndCityName() {
      // Arrange
      Address first = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityNameのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("cityNameが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentCityName() {
      // Arrange
      Address first = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", null, null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "新宿区", null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("cityNameが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKanaのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKanaAndCityNameAndCityKana() {
      // Arrange
      Address first = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKanaのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("cityKanaが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentCityKana() {
      // Arrange
      Address first = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", null, null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シンジュクク", null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("cityKanaが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardNameのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKanaAndCityNameAndCityKanaAndWardName() {
      // Arrange
      Address first = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardNameのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("wardNameが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentWardName() {
      // Arrange
      Address first = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", null, null, null, null, null, null, null, null, null, null);
      Address second = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "原宿", null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("wardNameが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKanaのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKanaAndCityNameAndCityKanaAndWardNameAndWardKana() {
      // Arrange
      Address first = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", null, null, null, null, null, null, null, null, null);
      Address second = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKanaのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("wardKanaが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentWardKana() {
      // Arrange
      Address first = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", null, null, null, null, null, null, null, null, null);
      Address second = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ハラジュク", null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("wardKanaが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoNameのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKanaAndCityNameAndCityKanaAndWardNameAndWardKanaAndOazaChoName() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", null, null, null, null, null, null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoNameのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("oazaChoNameが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentOazaChoName() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", null, null, null, null, null, null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字原宿", null, null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("oazaChoNameが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoName、oazaChoKanaのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKanaAndCityNameAndCityKanaAndWardNameAndWardKanaAndOazaChoNameAndOazaChoKana() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", null, null, null, null, null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoName、oazaChoKanaのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("oazaChoKanaが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentOazaChoKana() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", null, null, null, null, null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジハラジュク", null, null, null, null, null, null, null);

      // Assert
      assertThat(first).as("oazaChoKanaが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoName、oazaChoKana、chomeNameのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKanaAndCityNameAndCityKanaAndWardNameAndWardKanaAndOazaChoNameAndOazaChoKanaAndChomeName() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", null, null, null, null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", null, null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoName、oazaChoKana、chomeNameのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("chomeNameが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentChomeName() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", null, null, null, null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "二丁目", null, null, null, null, null, null);

      // Assert
      assertThat(first).as("chomeNameが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoName、oazaChoKana、chomeName、chomeKanaのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKanaAndCityNameAndCityKanaAndWardNameAndWardKanaAndOazaChoNameAndOazaChoKanaAndChomeNameAndChomeKana() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", null, null, null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", null, null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoName、oazaChoKana、chomeName、chomeKanaのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("chomeKanaが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentChomeKana() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", null, null, null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "ニチョウメ", null, null, null, null, null);

      // Assert
      assertThat(first).as("chomeKanaが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoName、oazaChoKana、chomeName、chomeKana、koazaNameのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKanaAndCityNameAndCityKanaAndWardNameAndWardKanaAndOazaChoNameAndOazaChoKanaAndChomeNameAndChomeKanaAndKoazaName() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", null, null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", null, null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoName、oazaChoKana、chomeName、chomeKana、koazaNameのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("koazaNameが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentKoazaName() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", null, null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字新宿", null, null, null, null);

      // Assert
      assertThat(first).as("koazaNameが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoName、oazaChoKana、chomeName、chomeKana、koazaName、koazaKanaのAddressは等しいこと")
    void shouldBeEqualWhenSameValueAndPrefNameAndPrefKanaAndPostalCodeAndCountryNameAndCountryKanaAndCityNameAndCityKanaAndWardNameAndWardKanaAndOazaChoNameAndOazaChoKanaAndChomeNameAndChomeKanaAndKoazaNameAndKoazaKana() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", "コウアジジンミウマエ", null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", "コウアジジンミウマエ", null, null, null);

      // Assert
      assertThat(first).as("同じ値、prefName、prefKana、postalCode、countryName、countryKana、cityName、cityKana、wardName、wardKana、oazaChoName、oazaChoKana、chomeName、chomeKana、koazaName、koazaKanaのAddressは等しいこと").isEqualTo(second);
    }

    @Test
    @DisplayName("koazaKanaが異なるAddressは等しくないこと")
    void shouldNotBeEqualWhenDifferentKoazaKana() {
      // Arrange
      Address first = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", "コウアジジンミウマエ", null, null, null);
      Address second = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", "コウアジシヌジク", null, null, null);

      // Assert
      assertThat(first).as("koazaKanaが異なるAddressは等しくないこと").isNotEqualTo(second);
    }

    @Test
    @DisplayName("nullとの等価比較はfalseを返すこと")
    void shouldNotBeEqualToNull() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address).as("nullとの等価比較はfalseを返すこと").isNotEqualTo(null);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("値を持つAddressのtoStringは値を返すこと")
    void shouldReturnValueWhenToString() {
      // Arrange
      Address address = Address.of("東京都渋谷区神宮前1-2-3", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringは値を返すこと").isEqualTo("東京都渋谷区神宮前1-2-3");
    }

    @Test
    @DisplayName("prefNameを持つAddressのtoStringはprefNameと値を返すこと")
    void shouldReturnPrefNameAndValueWhenToString() {
      // Arrange
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはprefNameと値を返すこと").isEqualTo("東京都 渋谷区神宮前1-2-3");
    }

    @Test
    @DisplayName("prefNameとprefKanaを持つAddressのtoStringはprefName(prefKana)と値を返すこと")
    void shouldReturnPrefNamePrefKanaAndValueWhenToString() {
      // Arrange
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはprefName(prefKana)と値を返すこと").isEqualTo("東京都(トウキョウト) 渋谷区神宮前1-2-3");
    }

    @Test
    @DisplayName("postalCodeを持つAddressのtoStringはpostalCodeと値を返すこと")
    void shouldReturnPostalCodeAndValueWhenToString() {
      // Arrange
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはpostalCodeと値を返すこと").isEqualTo("[150-0001] 東京都(トウキョウト) 渋谷区神宮前1-2-3");
    }

    @Test
    @DisplayName("countryNameを持つAddressのtoStringはcountryNameと値を返すこと")
    void shouldReturnCountryNameAndValueWhenToString() {
      // Arrange
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", null, null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはcountryNameと値を返すこと").isEqualTo("日本 [150-0001] 東京都(トウキョウト) 渋谷区神宮前1-2-3");
    }

    @Test
    @DisplayName("countryNameとcountryKanaを持つAddressのtoStringはcountryName(countryKana)と値を返すこと")
    void shouldReturnCountryNameAndCountryKanaAndValueWhenToString() {
      // Arrange
      Address address = Address.of("渋谷区神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", null, null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはcountryName(countryKana)と値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区神宮前1-2-3");
    }

    @Test
    @DisplayName("cityNameを持つAddressのtoStringはcityNameと値を返すこと")
    void shouldReturnCityNameAndValueWhenToString() {
      // Arrange
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", null, null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはcityNameと値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区 神宮前1-2-3");
    }

    @Test
    @DisplayName("cityNameとcityKanaを持つAddressのtoStringはcityName(cityKana)と値を返すこと")
    void shouldReturnCityNameAndCityKanaAndValueWhenToString() {
      // Arrange
      Address address = Address.of("神宮前1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", null, null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはcityName(cityKana)と値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区(シブヤク) 神宮前1-2-3");
    }

    @Test
    @DisplayName("wardNameを持つAddressのtoStringはwardNameと値を返すこと")
    void shouldReturnWardNameAndValueWhenToString() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", null, null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはwardNameと値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区(シブヤク) 神宮前 1-2-3");
    }

    @Test
    @DisplayName("wardNameとwardKanaを持つAddressのtoStringはwardName(wardKana)と値を返すこと")
    void shouldReturnWardNameAndWardKanaAndValueWhenToString() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", null, null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはwardName(wardKana)と値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区(シブヤク) 神宮前(ジンミウマエ) 1-2-3");
    }

    @Test
    @DisplayName("oazaChoNameを持つAddressのtoStringはoazaChoNameと値を返すこと")
    void shouldReturnOazaChoNameAndValueWhenToString() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", null, null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはoazaChoNameと値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区(シブヤク) 神宮前(ジンミウマエ) 大字神宮前 1-2-3");
    }

    @Test
    @DisplayName("oazaChoNameとoazaChoKanaを持つAddressのtoStringはoazaChoName(oazaChoKana)と値を返すこと")
    void shouldReturnOazaChoNameAndOazaChoKanaAndValueWhenToString() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", null, null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはoazaChoName(oazaChoKana)と値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区(シブヤク) 神宮前(ジンミウマエ) 大字神宮前(オオアジジンミウマエ) 1-2-3");
    }

    @Test
    @DisplayName("chomeNameを持つAddressのtoStringはchomeNameと値を返すこと")
    void shouldReturnChomeNameAndValueWhenToString() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", null, null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはchomeNameと値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区(シブヤク) 神宮前(ジンミウマエ) 大字神宮前(オオアジジンミウマエ) 一丁目 1-2-3");
    }

    @Test
    @DisplayName("chomeNameとchomeKanaを持つAddressのtoStringはchomeName(chomeKana)と値を返すこと")
    void shouldReturnChomeNameAndChomeKanaAndValueWhenToString() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", null, null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはchomeName(chomeKana)と値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区(シブヤク) 神宮前(ジンミウマエ) 大字神宮前(オオアジジンミウマエ) 一丁目(イッチョウメ) 1-2-3");
    }

    @Test
    @DisplayName("koazaNameを持つAddressのtoStringはkoazaNameと値を返すこと")
    void shouldReturnKoazaNameAndValueWhenToString() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", null, null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはkoazaNameと値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区(シブヤク) 神宮前(ジンミウマエ) 大字神宮前(オオアジジンミウマエ) 一丁目(イッチョウメ) 小字神宮前 1-2-3");
    }

    @Test
    @DisplayName("koazaNameとkoazaKanaを持つAddressのtoStringはkoazaName(koazaKana)と値を返すこと")
    void shouldReturnKoazaNameAndKoazaKanaAndValueWhenToString() {
      // Arrange
      Address address = Address.of("1-2-3", "東京都", "トウキョウト", "150-0001", "日本", "ニホン", "渋谷区", "シブヤク", "神宮前", "ジンミウマエ", "大字神宮前", "オオアジジンミウマエ", "一丁目", "イッチョウメ", "小字神宮前", "コウアジジンミウマエ", null, null, null);

      // Assert
      assertThat(address.toString()).as("toStringはkoazaName(koazaKana)と値を返すこと").isEqualTo("日本(ニホン) [150-0001] 東京都(トウキョウト) 渋谷区(シブヤク) 神宮前(ジンミウマエ) 大字神宮前(オオアジジンミウマエ) 一丁目(イッチョウメ) 小字神宮前(コウアジジンミウマエ) 1-2-3");
    }

    @Test
    @DisplayName("空のAddressのtoStringはnullを返すこと")
    void shouldReturnNullStringWhenEmpty() {
      // Arrange
      Address address = Address.empty();

      // Assert
      assertThat(address.toString()).as("空のAddressのtoStringはnullを返すこと").isEqualTo("null");
    }
  }

  @Nested
  @DisplayName("AddressConverterのテスト")
  class AddressConverterTest {

    private final Address.AddressConverter converter = new Address.AddressConverter();

    @Nested
    @DisplayName("convertToDatabaseColumnメソッドのテスト")
    class ConvertToDatabaseColumnTest {

      @Test
      @DisplayName("Addressをデータベース用のJSON文字列に変換すること")
      void shouldConvertAddressToJsonString() {
        // Arrange
        Address address = Address.builder()
            .address("東京都渋谷区神宮前1-2-3")
            .prefName("東京都")
            .prefKana("トウキョウト")
            .postalCode("150-0001")
            .countryName("日本")
            .countryKana("ニホン")
            .cityName("渋谷区")
            .cityKana("シブヤク")
            .wardName("神宮前")
            .wardKana("ジンミウマエ")
            .oazaChoName("大字神宮前")
            .oazaChoKana("オオアジジンミウマエ")
            .chomeName("一丁目")
            .chomeKana("イッチョウメ")
            .koazaName("小字神宮前")
            .koazaKana("コウアジジンミウマエ")
            .buildingName("神宮ビル")
            .buildingKana("ジンミウビル")
            .roomNo("101号室")
            .build();

        // Act
        String dbData = converter.convertToDatabaseColumn(address);

        // Assert
        assertThat(dbData).as("JSON形式でシリアライズされること").isNotNull();
        assertThat(dbData).as("JSON形式であること").startsWith("{");
        assertThat(dbData).as("valueフィールドが含まれること").contains("\"value\":\"東京都渋谷区神宮前1-2-3\"");
        assertThat(dbData).as("prefNameフィールドが含まれること").contains("\"prefName\":\"東京都\"");
        assertThat(dbData).as("postalCodeフィールドが含まれること").contains("\"postalCode\":\"150-0001\"");
        assertThat(dbData).as("roomNoフィールドが含まれること").contains("\"roomNo\":\"101号室\"");
        assertThat(dbData).as("buildingNameフィールドが含まれること").contains("\"buildingName\":\"神宮ビル\"");
      }

      @Test
      @DisplayName("nullのAddressはnullに変換すること")
      void shouldConvertNullAddressToNull() {
        // Act
        String dbData = converter.convertToDatabaseColumn(null);

        // Assert
        assertThat(dbData).as("nullのAddressはnullに変換されること").isNull();
      }
    }

    @Nested
    @DisplayName("convertToEntityAttributeメソッドのテスト")
    class ConvertToEntityAttributeTest {

      @Test
      @DisplayName("JSON形式のデータベースデータを全フィールド復元してAddressに変換すること")
      void shouldConvertJsonToAddressWithAllFields() {
        // Arrange
        Address original = Address.builder()
            .address("東京都渋谷区神宮前1-2-3")
            .prefName("東京都")
            .prefKana("トウキョウト")
            .postalCode("150-0001")
            .countryName("日本")
            .countryKana("ニホン")
            .cityName("渋谷区")
            .cityKana("シブヤク")
            .wardName("神宮前")
            .wardKana("ジンミウマエ")
            .oazaChoName("大字神宮前")
            .oazaChoKana("オオアジジンミウマエ")
            .chomeName("一丁目")
            .chomeKana("イッチョウメ")
            .koazaName("小字神宮前")
            .koazaKana("コウアジジンミウマエ")
            .buildingName("神宮ビル")
            .buildingKana("ジンミウビル")
            .roomNo("101号室")
            .build();

        // Act: 一度JSONにシリアライズしてから復元
        String dbData = converter.convertToDatabaseColumn(original);
        Address restored = converter.convertToEntityAttribute(dbData);

        // Assert: 全フィールドが復元されること
        assertThat(restored).as("Addressに変換されること").isNotNull();
        assertThat(restored.value()).as("valueが復元されること").isEqualTo("東京都渋谷区神宮前1-2-3");
        assertThat(restored.getPrefName()).as("prefNameが復元されること").isEqualTo("東京都");
        assertThat(restored.getPrefKana()).as("prefKanaが復元されること").isEqualTo("トウキョウト");
        assertThat(restored.getPostalCode()).as("postalCodeが復元されること").isEqualTo("150-0001");
        assertThat(restored.getCountryName()).as("countryNameが復元されること").isEqualTo("日本");
        assertThat(restored.getCountryKana()).as("countryKanaが復元されること").isEqualTo("ニホン");
        assertThat(restored.getCityName()).as("cityNameが復元されること").isEqualTo("渋谷区");
        assertThat(restored.getCityKana()).as("cityKanaが復元されること").isEqualTo("シブヤク");
        assertThat(restored.getWardName()).as("wardNameが復元されること").isEqualTo("神宮前");
        assertThat(restored.getWardKana()).as("wardKanaが復元されること").isEqualTo("ジンミウマエ");
        assertThat(restored.getOazaChoName()).as("oazaChoNameが復元されること").isEqualTo("大字神宮前");
        assertThat(restored.getOazaChoKana()).as("oazaChoKanaが復元されること").isEqualTo("オオアジジンミウマエ");
        assertThat(restored.getChomeName()).as("chomeNameが復元されること").isEqualTo("一丁目");
        assertThat(restored.getChomeKana()).as("chomeKanaが復元されること").isEqualTo("イッチョウメ");
        assertThat(restored.getKoazaName()).as("koazaNameが復元されること").isEqualTo("小字神宮前");
        assertThat(restored.getKoazaKana()).as("koazaKanaが復元されること").isEqualTo("コウアジジンミウマエ");
        assertThat(restored.getBuildingName()).as("buildingNameが復元されること").isEqualTo("神宮ビル");
        assertThat(restored.getBuildingKana()).as("buildingKanaが復元されること").isEqualTo("ジンミウビル");
        assertThat(restored.getRoomNo()).as("roomNoが復元されること").isEqualTo("101号室");
      }

      @Test
      @DisplayName("旧形式のプレーン文字列データは互換性のためにAddress.of()で復元すること")
      void shouldConvertLegacyPlainStringToAddress() {
        // Arrange: 旧形式のプレーン文字列データ
        String dbData = "東京都渋谷区神宮前1-2-3";

        // Act
        Address address = converter.convertToEntityAttribute(dbData);

        // Assert
        assertThat(address).as("Addressに変換されること").isNotNull();
        assertThat(address.value()).as("値が保持されること").isEqualTo("東京都渋谷区神宮前1-2-3");
      }

      @Test
      @DisplayName("nullのデータベースデータは空のAddressに変換すること")
      void shouldConvertNullToEmptyAddress() {
        // Act
        Address address = converter.convertToEntityAttribute(null);

        // Assert
        assertThat(address).as("Addressインスタンスが作成されること").isNotNull();
        assertThat(address.isEmpty()).as("空のAddressであること").isTrue();
      }

      @Test
      @DisplayName("不正なJSONデータはフォールバックしてAddressに変換すること")
      void shouldConvertInvalidJsonToFallbackAddress() {
        // Act: 不正なJSONは旧形式として扱われ、Address.of()で復元される
        Address address = converter.convertToEntityAttribute("{{{invalid json}}");

        // Assert: 不正なJSONだが文字列として有効なので、valueが設定されたAddressが返される
        assertThat(address).as("Addressインスタンスが作成されること").isNotNull();
        assertThat(address.value()).as("値が保持されること").isEqualTo("{{{invalid json}}");
      }

      @Test
      @DisplayName("空文字列は空のAddressに変換すること")
      void shouldConvertBlankToEmptyAddress() {
        // Act
        Address address = converter.convertToEntityAttribute("   ");

        // Assert
        assertThat(address).as("Addressインスタンスが作成されること").isNotNull();
        assertThat(address.isEmpty()).as("空のAddressであること").isTrue();
      }
    }
  }
}
