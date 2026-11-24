package undecided.generic.documentMgmt.internal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static undecided.generic.documentMgmt.internal.DocumentFileValidator.MAX_FILE_SIZE_BYTES;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * テストリスト項目10〜18: ファイル検証のテスト
 *
 * <p>- 項目10: 許可された拡張子と Content-Type の組み合わせは合格する
 * <p>- 項目11: 拡張子は許可されているが Content-Type が不一致のとき不合格
 * <p>- 項目12: Content-Type は許可されているが拡張子が不一致のとき不合格
 * <p>- 項目13: ファイルサイズが10MBちょうどのとき合格する
 * <p>- 項目14: ファイルサイズが10MBを1バイト超えたとき不合格
 * <p>- 項目15: ファイルサイズが0バイトのとき不合格
 * <p>- 項目16: ファイルサイズが負のとき不合格
 * <p>- 項目17: 許可されていない拡張子のとき不合格
 * <p>- 項目18: 許可されていない Content-Type のとき不合格
 */
@Tag("small")
@DisplayName("DocumentFileValidatorの単体テスト")
class DocumentFileValidatorTest {

  private static final long VALID_SIZE = 1024L;

  private final DocumentFileValidator validator = new DocumentFileValidator();

  @Test
  @DisplayName("許可された拡張子とContent-Typeの組み合わせのとき合格すること")
  void shouldPassForAllowedExtensionAndContentType() {
    assertThatCode(() -> validator.validate("photo.png", "image/png", VALID_SIZE))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("拡張子は許可されているがContent-Typeが不一致のとき不合格となること")
  void shouldFailWhenContentTypeDoesNotMatchExtension() {
    assertThatThrownBy(() -> validator.validate("photo.png", "application/pdf", VALID_SIZE))
        .isInstanceOf(InvalidDocumentFileException.class);
  }

  @Test
  @DisplayName("Content-Typeは許可されているが拡張子が不一致のとき不合格となること")
  void shouldFailWhenExtensionDoesNotMatchContentType() {
    assertThatThrownBy(() -> validator.validate("photo.pdf", "image/png", VALID_SIZE))
        .isInstanceOf(InvalidDocumentFileException.class);
  }

  @Test
  @DisplayName("ファイルサイズが上限ちょうどのとき合格すること")
  void shouldPassWhenFileSizeIsExactlyMax() {
    assertThatCode(() -> validator.validate("photo.png", "image/png", MAX_FILE_SIZE_BYTES))
        .doesNotThrowAnyException();
  }

  @Test
  @DisplayName("ファイルサイズが上限を1バイト超えたとき不合格となること")
  void shouldFailWhenFileSizeExceedsMaxByOneByte() {
    assertThatThrownBy(
            () -> validator.validate("photo.png", "image/png", MAX_FILE_SIZE_BYTES + 1))
        .isInstanceOf(InvalidDocumentFileException.class);
  }

  @Test
  @DisplayName("ファイルサイズが0バイトのとき不合格となること")
  void shouldFailWhenFileSizeIsZero() {
    assertThatThrownBy(() -> validator.validate("photo.png", "image/png", 0L))
        .isInstanceOf(InvalidDocumentFileException.class);
  }

  @Test
  @DisplayName("ファイルサイズが負のとき不合格となること")
  void shouldFailWhenFileSizeIsNegative() {
    assertThatThrownBy(() -> validator.validate("photo.png", "image/png", -1L))
        .isInstanceOf(InvalidDocumentFileException.class);
  }

  @Test
  @DisplayName("許可されていない拡張子のとき不合格となること")
  void shouldFailForDisallowedExtension() {
    assertThatThrownBy(() -> validator.validate("setup.exe", "image/png", VALID_SIZE))
        .isInstanceOf(InvalidDocumentFileException.class);
  }

  @Test
  @DisplayName("許可されていないContent-Typeのとき不合格となること")
  void shouldFailForDisallowedContentType() {
    assertThatThrownBy(
            () -> validator.validate("photo.png", "application/x-msdownload", VALID_SIZE))
        .isInstanceOf(InvalidDocumentFileException.class);
  }
}
