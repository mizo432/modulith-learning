package undecided.erp.relationship.domain.model.orgRole.company;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.common.exception.BusinessException;

@DisplayName("CompanyCodeクラスのテスト")
class CompanyCodeTest {

  @Nested
  @DisplayName("ofメソッドのテスト")
  class OfMethodTests {

    @Test
    @DisplayName("有効な会社コードを渡すときのテスト")
    void shouldReturnCompanyCodeWhenValidValueIsProvided() {
      // Given
      String validCode = "12345678";

      // When
      CompanyCode companyCode = CompanyCode.of(validCode);

      // Then
      assertThat(companyCode).isNotNull();
      assertThat(companyCode.value()).isEqualTo(validCode);
    }

    @Test
    @DisplayName("会社コードがnullのときのテスト")
    void shouldThrowExceptionWhenCompanyCodeIsNull() {
      // Given
      String nullCode = null;

      // When / Then
      assertThatThrownBy(() -> CompanyCode.of(nullCode))
          .isInstanceOf(BusinessException.class)
          .hasMessage(
              "ResultMessages [type=error, list=[ResultMessage [code=null, args=[], text=Company code cannot be null]]]");
    }

    @Test
    @DisplayName("会社コードが空のときのテスト")
    void shouldThrowExceptionWhenCompanyCodeIsEmpty() {
      // Given
      String emptyCode = "";

      // When / Then
      assertThatThrownBy(() -> CompanyCode.of(emptyCode))
          .isInstanceOf(BusinessException.class)
          .hasMessage(
              "ResultMessages [type=error, list=[ResultMessage [code=null, args=[], text=Company code cannot be empty]]]");
    }

    @Test
    @DisplayName("会社コードの長さが8でないときのテスト")
    void shouldThrowExceptionWhenCompanyCodeIsNotEightCharacters() {
      // Given
      String invalidLengthCode = "123";

      // When / Then
      assertThatThrownBy(() -> CompanyCode.of(invalidLengthCode))
          .isInstanceOf(BusinessException.class)
          .hasMessage(
              "ResultMessages [type=error, list=[ResultMessage [code=null, args=[], text=Company code must be length is 8]]]");
    }

    @Test
    @DisplayName("会社コードに数字以外が含まれているときのテスト")
    void shouldThrowExceptionWhenCompanyCodeContainsNonDigitalCharacters() {
      // Given
      String nonDecimalCode = "12345abc";

      // When / Then
      assertThatThrownBy(() -> CompanyCode.of(nonDecimalCode))
          .isInstanceOf(BusinessException.class)
          .hasMessage(
              "ResultMessages [type=error, list=[ResultMessage [code=null, args=[], text=Company code must be all decimal]]]");
    }
  }
}
