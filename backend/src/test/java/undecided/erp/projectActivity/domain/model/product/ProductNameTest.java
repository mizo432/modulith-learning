package undecided.erp.projectActivity.domain.model.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * テストクラス: {@link ProductName} クラスを作成するメソッドをテストします。
 */
class ProductNameTest {

  /**
   * ネストしたクラス: of 非nullの文字列を引数に受け取り、新たな {@link ProductName} オブジェクトを作成するメソッドをテストします。
   */
  @Nested
  @DisplayName("of メソッド")
  class OfTest {

    @Test
    @DisplayName("正常にProductNameオブジェクトが作成される")
    void shouldCreateProductName() {
      String productCode = "PRODUCT_123";

      ProductName productName = ProductName.of(productCode);

      assertThat(productName)
          .isNotNull()
          .extracting(ProductName::getValue)
          .isEqualTo(productCode);
    }

    @Test
    @DisplayName("NullPointerException引数が空の場合に例外をスローする")
    void shouldThrowExceptionWhenValueIsNull() {
      assertThatThrownBy(() -> ProductName.of(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("value is marked non-null but is null");
    }

    @Test
    @DisplayName("NullPointerException引数が空の場合に例外をスローする")
    void shouldThrowExceptionWhenValueIsEmpty() {
      assertThatThrownBy(() -> ProductName.of(""))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("ProductName cannot be empty. Please provide a valid code.");
    }
  }

  /**
   * ネストしたクラス: isEmpty 文字列が空であるかどうかを検証するメソッドをテストします。
   */
  @Nested
  @DisplayName("isEmpty メソッド")
  class IsEmptyTest {

    @Test
    @DisplayName("正常にProductNameオブジェクトが空であることを検証")
    void shouldValidateProductNameIsEmpty() {

      ProductName productName = ProductName.EMPTY;

      assertThat(productName.isEmpty())
          .isTrue();
    }

    @Test
    @DisplayName("正常にProductNameオブジェクトが空でないことを検証")
    void shouldValidateProductNameIsNotEmpty() {

      ProductName productName = ProductName.of("PRODUCT_123");

      assertThat(productName.isEmpty())
          .isFalse();
    }

    @Test
    @DisplayName("NullPointerException引数がnullの場合に例外をスローする")
    void shouldThrowExceptionWhenValueIsNull() {
      assertThatThrownBy(() -> ProductName.of(null).isEmpty())
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("value is marked non-null but is null");
    }
  }

  /**
   * ネストしたクラス: toString 文字列を返すメソッドをテストします。
   */
  @Nested
  @DisplayName("toString メソッド")
  class ToStringTest {

    @Test
    @DisplayName("正常に文字列が返される")
    void shouldReturnStringValue() {
      String productCode = "PRODUCT_123";
      ProductName productName = ProductName.of(productCode);
      assertThat(productName.toString()).isEqualTo(productCode);
    }

    @Test
    @DisplayName("空が渡されるとnull文字列を返す")
    void shouldNullStringWhenEmpty() {
      assertThat(ProductName.EMPTY.toString()).isEqualTo("null");
    }
  }
}
