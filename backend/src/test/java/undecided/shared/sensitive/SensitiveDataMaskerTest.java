package undecided.shared.sensitive;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
class SensitiveDataMaskerTest {

  /** Method under test: {@link SensitiveDataMasker#maskName(String)} */
  @Nested
  @DisplayName("maskNameメソッドのテスト")
  class MaskNameTest {

    @Test
    @DisplayName("null入力の場合nullを返す")
    void shouldReturnNullForNullInput() {
      assertThat(SensitiveDataMasker.maskName(null)).isNull();
    }

    @Test
    @DisplayName("空文字入力の場合空文字を返す")
    void shouldReturnEmptyForEmptyInput() {
      assertThat(SensitiveDataMasker.maskName("")).isEmpty();
    }

    @Test
    @DisplayName("1文字入力の場合そのまま返す")
    void shouldReturnAsIsForSingleChar() {
      assertThat(SensitiveDataMasker.maskName("田")).isEqualTo("田");
    }

    @Test
    @DisplayName("2文字の氏名をマスクする")
    void shouldMaskTwoCharName() {
      assertThat(SensitiveDataMasker.maskName("太郎")).isEqualTo("太*");
    }

    @Test
    @DisplayName("苗字をマスクする")
    void shouldMaskSurname() {
      assertThat(SensitiveDataMasker.maskName("田中")).isEqualTo("田*");
    }

    @Test
    @DisplayName("3文字以上の氏名をマスクする")
    void shouldMaskLongName() {
      assertThat(SensitiveDataMasker.maskName("田中太郎")).isEqualTo("田*");
    }
  }

  /** Method under test: {@link SensitiveDataMasker#maskEmail(String)} */
  @Nested
  @DisplayName("maskEmailメソッドのテスト")
  class MaskEmailTest {

    @Test
    @DisplayName("null入力の場合nullを返す")
    void shouldReturnNullForNullInput() {
      assertThat(SensitiveDataMasker.maskEmail(null)).isNull();
    }

    @Test
    @DisplayName("空文字入力の場合空文字を返す")
    void shouldReturnEmptyForEmptyInput() {
      assertThat(SensitiveDataMasker.maskEmail("")).isEmpty();
    }

    @Test
    @DisplayName("通常のメールアドレスをマスクする")
    void shouldMaskNormalEmail() {
      assertThat(SensitiveDataMasker.maskEmail("taro@example.com")).isEqualTo("t***@example.com");
    }

    @Test
    @DisplayName("長いローカルパートのメールアドレスをマスクする")
    void shouldMaskLongLocalPartEmail() {
      assertThat(SensitiveDataMasker.maskEmail("very.long.local.part@domain.org"))
          .isEqualTo("v***@domain.org");
    }

    @Test
    @DisplayName("1文字のローカルパートのメールアドレスをマスクする")
    void shouldMaskSingleCharLocalPartEmail() {
      assertThat(SensitiveDataMasker.maskEmail("a@example.com")).isEqualTo("a***@example.com");
    }

    @Test
    @DisplayName("@が含まれない文字列はそのまま返す")
    void shouldReturnAsIsWhenNoAtSymbol() {
      assertThat(SensitiveDataMasker.maskEmail("noemail")).isEqualTo("noemail");
    }

    @Test
    @DisplayName("@が先頭にある文字列はそのまま返す")
    void shouldReturnAsIsWhenAtAtStart() {
      assertThat(SensitiveDataMasker.maskEmail("@example.com")).isEqualTo("@example.com");
    }
  }

  /** Method under test: {@link SensitiveDataMasker#maskPhone(String)} */
  @Nested
  @DisplayName("maskPhoneメソッドのテスト")
  class MaskPhoneTest {

    @Test
    @DisplayName("null入力の場合nullを返す")
    void shouldReturnNullForNullInput() {
      assertThat(SensitiveDataMasker.maskPhone(null)).isNull();
    }

    @Test
    @DisplayName("空文字入力の場合空文字を返す")
    void shouldReturnEmptyForEmptyInput() {
      assertThat(SensitiveDataMasker.maskPhone("")).isEmpty();
    }

    @Test
    @DisplayName("11桁の電話番号をマスクする")
    void shouldMask11DigitPhone() {
      assertThat(SensitiveDataMasker.maskPhone("09012345678")).isEqualTo("*******5678");
    }

    @Test
    @DisplayName("10桁の電話番号をマスクする")
    void shouldMask10DigitPhone() {
      assertThat(SensitiveDataMasker.maskPhone("0123456789")).isEqualTo("******6789");
    }

    @Test
    @DisplayName("4桁以下の電話番号はアスタリスクのみ返す")
    void shouldReturnAsterisksForShortPhone() {
      assertThat(SensitiveDataMasker.maskPhone("1234")).isEqualTo("****");
    }

    @Test
    @DisplayName("3桁の電話番号はアスタリスクのみ返す")
    void shouldReturnAsterisksFor3DigitPhone() {
      assertThat(SensitiveDataMasker.maskPhone("123")).isEqualTo("****");
    }

    @Test
    @DisplayName("1桁の電話番号はアスタリスクのみ返す")
    void shouldReturnAsterisksFor1DigitPhone() {
      assertThat(SensitiveDataMasker.maskPhone("1")).isEqualTo("****");
    }
  }

  /** Method under test: {@link SensitiveDataMasker#maskDateOfBirth(LocalDate)} */
  @Nested
  @DisplayName("maskDateOfBirthメソッドのテスト")
  class MaskDateOfBirthTest {

    @Test
    @DisplayName("null入力の場合nullを返す")
    void shouldReturnNullForNullInput() {
      assertThat(SensitiveDataMasker.maskDateOfBirth(null)).isNull();
    }

    @Test
    @DisplayName("通常の生年月日をマスクする")
    void shouldMaskNormalDateOfBirth() {
      LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);
      assertThat(SensitiveDataMasker.maskDateOfBirth(dateOfBirth)).isEqualTo("1990-**-**");
    }

    @Test
    @DisplayName("2000年代の生年月日をマスクする")
    void shouldMaskDateOfBirthIn2000s() {
      LocalDate dateOfBirth = LocalDate.of(2005, 12, 31);
      assertThat(SensitiveDataMasker.maskDateOfBirth(dateOfBirth)).isEqualTo("2005-**-**");
    }

    @Test
    @DisplayName("1月の1日の生年月日をマスクする")
    void shouldMaskDateOfBirthOnJanuaryFirst() {
      LocalDate dateOfBirth = LocalDate.of(1980, 1, 1);
      assertThat(SensitiveDataMasker.maskDateOfBirth(dateOfBirth)).isEqualTo("1980-**-**");
    }
  }

  /** Method under test: {@link SensitiveDataMasker#mask(String)} */
  @Nested
  @DisplayName("maskメソッドのテスト")
  class MaskTest {

    @Test
    @DisplayName("null入力の場合nullを返す")
    void shouldReturnNullForNullInput() {
      assertThat(SensitiveDataMasker.mask(null)).isNull();
    }

    @Test
    @DisplayName("空文字入力の場合空文字を返す")
    void shouldReturnEmptyForEmptyInput() {
      assertThat(SensitiveDataMasker.mask("")).isEmpty();
    }

    @Test
    @DisplayName("機微な情報がない文字列はそのまま返す")
    void shouldReturnAsIsWhenNoSensitiveData() {
      assertThat(SensitiveDataMasker.mask("Hello world")).isEqualTo("Hello world");
    }

    @Test
    @DisplayName("メールアドレスが含まれる文字列をマスクする")
    void shouldMaskEmailInText() {
      assertThat(SensitiveDataMasker.mask("Contact taro@example.com for details"))
          .isEqualTo("Contact t***@example.com for details");
    }

    @Test
    @DisplayName("電話番号が含まれる文字列をマスクする")
    void shouldMaskPhoneInText() {
      assertThat(SensitiveDataMasker.mask("Call 09012345678 now"))
          .isEqualTo("Call *******5678 now");
    }

    @Test
    @DisplayName("メールアドレスと電話番号の両方が含まれる文字列をマスクする")
    void shouldMaskBothEmailAndPhoneInText() {
      String input = "Contact taro@example.com or call 09012345678";
      assertThat(SensitiveDataMasker.mask(input))
          .isEqualTo("Contact t***@example.com or call *******5678");
    }

    @Test
    @DisplayName("複数のメールアドレスが含まれる文字列をマスクする")
    void shouldMaskMultipleEmailsInText() {
      String input = "Emails: a@foo.com and b@bar.org";
      assertThat(SensitiveDataMasker.mask(input))
          .isEqualTo("Emails: a***@foo.com and b***@bar.org");
    }

    @Test
    @DisplayName("複数の電話番号が含まれる文字列をマスクする")
    void shouldMaskMultiplePhonesInText() {
      String input = "Phones: 09012345678 and 08098765432";
      assertThat(SensitiveDataMasker.mask(input))
          .isEqualTo("Phones: *******5678 and *******5432");
    }

    @Test
    @DisplayName("例外メッセージのような文字列をマスクする")
    void shouldMaskExceptionMessage() {
      String input = "User not found: taro@example.com, phone: 09012345678";
      assertThat(SensitiveDataMasker.mask(input))
          .isEqualTo("User not found: t***@example.com, phone: *******5678");
    }
  }
}
