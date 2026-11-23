package undecided.generic.documentMgmt.spi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * テストリスト項目1〜7: ファイル種別区分値のテスト
 *
 * <p>- 項目1: png → IMAGE
 * <p>- 項目2: pdf → PDF
 * <p>- 項目3: txt → TEXT
 * <p>- 項目4: docx → DOCUMENT
 * <p>- 項目5: exe → 例外
 * <p>- 項目6: PNG（大文字）→ IMAGE
 * <p>- 項目7: 空文字 → 例外
 */
@Tag("small")
@DisplayName("DocumentFileTypeの単体テスト")
class DocumentFileTypeTest {

  @Test
  @DisplayName("拡張子pngのときIMAGEが返されること")
  void shouldReturnImageForPng() {
    assertThat(DocumentFileType.fromExtension("png")).isEqualTo(DocumentFileType.IMAGE);
  }

  @Test
  @DisplayName("拡張子pdfのときPDFが返されること")
  void shouldReturnPdfForPdf() {
    assertThat(DocumentFileType.fromExtension("pdf")).isEqualTo(DocumentFileType.PDF);
  }

  @Test
  @DisplayName("拡張子txtのときTEXTが返されること")
  void shouldReturnTextForTxt() {
    assertThat(DocumentFileType.fromExtension("txt")).isEqualTo(DocumentFileType.TEXT);
  }

  @Test
  @DisplayName("拡張子docxのときDOCUMENTが返されること")
  void shouldReturnDocumentForDocx() {
    assertThat(DocumentFileType.fromExtension("docx")).isEqualTo(DocumentFileType.DOCUMENT);
  }

  @Test
  @DisplayName("許可されていない拡張子exeのとき例外がスローされること")
  void shouldThrowForExe() {
    assertThatThrownBy(() -> DocumentFileType.fromExtension("exe"))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("大文字の拡張子PNGのときIMAGEが返されること")
  void shouldReturnImageForUpperCasePng() {
    assertThat(DocumentFileType.fromExtension("PNG")).isEqualTo(DocumentFileType.IMAGE);
  }

  @Test
  @DisplayName("空文字の拡張子のとき例外がスローされること")
  void shouldThrowForEmptyExtension() {
    assertThatThrownBy(() -> DocumentFileType.fromExtension(""))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
