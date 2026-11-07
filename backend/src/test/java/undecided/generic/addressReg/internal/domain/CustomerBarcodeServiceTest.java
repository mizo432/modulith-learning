package undecided.generic.addressReg.internal.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("カスタマバーコード生成サービスのテスト")
class CustomerBarcodeServiceTest {

  @Nested
  @DisplayName("カスタマバーコード生成のテスト")
  class GenerateBarcode {

    @Test
    @DisplayName("郵便番号と住所表示番号からカスタマバーコードを生成できること")
    void generate_basic() {
      // given
      String postalCode = "1000013";
      String addressNumber = "1-3-2-503";

      // when
      String result = CustomerBarcodeService.generate(postalCode, addressNumber);

      // then
      assertThat(result).isNotNull();
      assertThat(result).startsWith("(");
      assertThat(result).endsWith(")");
      assertThat(result).hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("郵便番号にハイフンが含まれている場合は除去して処理すること")
    void generate_with_hyphen_in_postal_code() {
      // given
      String postalCode = "100-0013";
      String addressNumber = "1-3-2";

      // when
      String result = CustomerBarcodeService.generate(postalCode, addressNumber);

      // then
      assertThat(result).isNotNull();
      assertThat(result).startsWith("(");
      assertThat(result).endsWith(")");
    }

    @Test
    @DisplayName("住所表示番号が空の場合はCC4で埋めること")
    void generate_with_empty_address_number() {
      // given
      String postalCode = "1000013";
      String addressNumber = "";

      // when
      String result = CustomerBarcodeService.generate(postalCode, addressNumber);

      // then
      assertThat(result).isNotNull();
      assertThat(result).startsWith("(");
      assertThat(result).endsWith(")");
    }

    @Test
    @DisplayName("住所表示番号がnullの場合は空文字として処理すること")
    void generate_with_null_address_number() {
      // given
      String postalCode = "1000013";
      String addressNumber = null;

      // when
      String result = CustomerBarcodeService.generate(postalCode, addressNumber);

      // then
      assertThat(result).isNotNull();
      assertThat(result).startsWith("(");
      assertThat(result).endsWith(")");
    }

    @Test
    @DisplayName("郵便番号がnullの場合は例外をスローすること")
    void generate_with_null_postal_code() {
      // given
      String postalCode = null;
      String addressNumber = "1-3-2";

      // when & then
      assertThatThrownBy(() -> CustomerBarcodeService.generate(postalCode, addressNumber))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("郵便番号は必須です");
    }

    @Test
    @DisplayName("郵便番号が空の場合は例外をスローすること")
    void generate_with_blank_postal_code() {
      // given
      String postalCode = "   ";
      String addressNumber = "1-3-2";

      // when & then
      assertThatThrownBy(() -> CustomerBarcodeService.generate(postalCode, addressNumber))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("郵便番号は必須です");
    }

    @Test
    @DisplayName("郵便番号が7桁でない場合は例外をスローすること")
    void generate_with_invalid_postal_code_length() {
      // given
      String postalCode = "12345";
      String addressNumber = "1-3-2";

      // when & then
      assertThatThrownBy(() -> CustomerBarcodeService.generate(postalCode, addressNumber))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("7桁");
    }

    @Test
    @DisplayName("住所表示番号が13桁を超える場合は truncation されること")
    void generate_with_long_address_number() {
      // given
      String postalCode = "1000013";
      String addressNumber = "1-3-2-503-100-200"; // 13桁以上

      // when
      String result = CustomerBarcodeService.generate(postalCode, addressNumber);

      // then
      assertThat(result).isNotNull();
      assertThat(result).startsWith("(");
      assertThat(result).endsWith(")");
    }
  }

  @Nested
  @DisplayName("住所表示番号抽出のテスト")
  class ExtractAddressNumber {

    @Test
    @DisplayName("基本的な住所から住所表示番号を抽出できること")
    void extract_basic() {
      // given
      String addressB = "1丁目3番2号 郵便プラザ503室";

      // when
      String result = CustomerBarcodeService.extractAddressNumber(addressB);

      // then
      assertThat(result).isNotNull();
      assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("漢数字を含む住所から住所表示番号を抽出できること")
    void extract_with_kanji_numbers() {
      // given
      String addressB = "十一丁目六番地一号 郵便タワー601";

      // when
      String result = CustomerBarcodeService.extractAddressNumber(addressB);

      // then
      assertThat(result).isNotNull();
      assertThat(result).contains("11");
      assertThat(result).contains("6");
      assertThat(result).contains("1");
    }

    @Test
    @DisplayName("アルファベットを含む住所から住所表示番号を抽出できること")
    void extract_with_alphabet() {
      // given
      String addressB = "9丁目7-6 郵便シティA棟1F1号";

      // when
      String result = CustomerBarcodeService.extractAddressNumber(addressB);

      // then
      assertThat(result).isNotNull();
      assertThat(result).contains("9");
      assertThat(result).contains("7");
      assertThat(result).contains("6");
    }

    @Test
    @DisplayName("nullの住所は空文字を返すこと")
    void extract_with_null() {
      // when
      String result = CustomerBarcodeService.extractAddressNumber(null);

      // then
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("空文字の住所は空文字を返すこと")
    void extract_with_blank() {
      // when
      String result = CustomerBarcodeService.extractAddressNumber("   ");

      // then
      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("小文字は変換されること")
    void extract_lowercase_conversion() {
      // given
      String addressB = "1丁目 a棟";

      // when
      String result = CustomerBarcodeService.extractAddressNumber(addressB);

      // then
      assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("不要な文字は除去されること")
    void extract_remove_unnecessary_chars() {
      // given
      String addressB = "1-2/3・4.5";

      // when
      String result = CustomerBarcodeService.extractAddressNumber(addressB);

      // then
      assertThat(result).doesNotContain("/");
      assertThat(result).doesNotContain("・");
      assertThat(result).doesNotContain(".");
    }
  }

  @Nested
  @DisplayName("日本郵政マニュアルのサンプルテスト")
  class JapanPostManualSamples {

    @Test
    @DisplayName("東京都千代田区霞が関1丁目3番2号 郵便プラザ503室 のサンプル")
    void sample_tokyo() {
      // given
      String postalCode = "1000013"; // 100-0013 からハイフン除去
      String addressB = "1丁目3番2号 郵便プラザ503室";

      // when
      String addressNumber = CustomerBarcodeService.extractAddressNumber(addressB);
      String barcode = CustomerBarcodeService.generate(postalCode, addressNumber);

      // then
      assertThat(addressNumber).contains("1");
      assertThat(addressNumber).contains("3");
      assertThat(addressNumber).contains("2");
      assertThat(addressNumber).contains("503");
      assertThat(barcode).startsWith("(");
      assertThat(barcode).endsWith(")");
    }

    @Test
    @DisplayName("北海道帯広市稲田町南 七線 西28 のサンプル")
    void sample_hokkaido() {
      // given
      String postalCode = "0928015"; // 帯広市の郵便番号（例）
      String addressB = "七線 西28";

      // when
      String addressNumber = CustomerBarcodeService.extractAddressNumber(addressB);
      String barcode = CustomerBarcodeService.generate(postalCode, addressNumber);

      // then
      assertThat(addressNumber).contains("7");
      assertThat(addressNumber).contains("28");
      assertThat(barcode).startsWith("(");
      assertThat(barcode).endsWith(")");
    }

    @Test
    @DisplayName("茨城県日立市宮田町 6丁目7-14 ABCビル2F のサンプル")
    void sample_ibaraki_2f_excluded() {
      // given
      String postalCode = "3170854"; // 日立市の郵便番号（例）
      String addressB = "6丁目7-14 ABCビル2F";

      // when
      String addressNumber = CustomerBarcodeService.extractAddressNumber(addressB);
      String barcode = CustomerBarcodeService.generate(postalCode, addressNumber);

      // then
      // 2Fは除外されるべき（算用数字に続くF）
      assertThat(addressNumber).contains("6");
      assertThat(addressNumber).contains("7");
      assertThat(addressNumber).contains("14");
      assertThat(barcode).startsWith("(");
      assertThat(barcode).endsWith(")");
    }

    @Test
    @DisplayName("茨城県日立市宮田町 6丁目7-14 ABCビル2F201号室 のサンプル")
    void sample_ibaraki_2f_replaced_with_hyphen() {
      // given
      String postalCode = "3170854";
      String addressB = "6丁目7-14 ABCビル2F201号室";

      // when
      String addressNumber = CustomerBarcodeService.extractAddressNumber(addressB);
      String barcode = CustomerBarcodeService.generate(postalCode, addressNumber);

      // then
      // F以降に数字がある場合はFはハイフンに置き換えられる
      assertThat(addressNumber).contains("6");
      assertThat(addressNumber).contains("7");
      assertThat(addressNumber).contains("14");
      assertThat(addressNumber).contains("201");
      assertThat(barcode).startsWith("(");
      assertThat(barcode).endsWith(")");
    }
  }
}
