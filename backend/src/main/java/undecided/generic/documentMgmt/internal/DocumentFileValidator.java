package undecided.generic.documentMgmt.internal;

import java.util.Locale;
import undecided.generic.documentMgmt.spi.DocumentFileType;

/** 帳票ファイルの種別（拡張子と Content-Type の両方）とサイズを検証する。 */
class DocumentFileValidator {

  /** 1ファイルの最大サイズ（10MB = 10 × 1024 × 1024 バイト）。 */
  static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024;

  /**
   * ファイルを検証する。
   *
   * @param fileName ファイル名（拡張子を含む）
   * @param contentType Content-Type
   * @param fileSize ファイルサイズ（バイト）
   * @throws InvalidDocumentFileException 検証に合格しない場合
   */
  void validate(String fileName, String contentType, long fileSize) {
    if (fileSize <= 0 || fileSize > MAX_FILE_SIZE_BYTES) {
      throw new InvalidDocumentFileException(
          "ファイルサイズは1バイト以上" + MAX_FILE_SIZE_BYTES + "バイト以下にしてください: " + fileSize);
    }
    String expectedContentType = expectedContentTypeOf(extensionOf(fileName));
    if (contentType == null || !normalizeContentType(contentType).equals(expectedContentType)) {
      throw new InvalidDocumentFileException(
          "Content-Type が拡張子と対応していません: " + fileName + ", " + contentType);
    }
  }

  /** 最後のドットより後ろを拡張子として取り出す。 */
  private static String extensionOf(String fileName) {
    int dot = fileName == null ? -1 : fileName.lastIndexOf('.');
    if (dot < 0 || dot == fileName.length() - 1) {
      throw new InvalidDocumentFileException("ファイル名に拡張子がありません: " + fileName);
    }
    return fileName.substring(dot + 1);
  }

  private static String expectedContentTypeOf(String extension) {
    try {
      return DocumentFileType.contentTypeOf(extension);
    } catch (IllegalArgumentException e) {
      throw new InvalidDocumentFileException("許可されていない拡張子です: " + extension, e);
    }
  }

  /** パラメータ（{@code ; charset=...} など）を除き、小文字にする。 */
  private static String normalizeContentType(String contentType) {
    int semicolon = contentType.indexOf(';');
    String mediaType = semicolon < 0 ? contentType : contentType.substring(0, semicolon);
    return mediaType.trim().toLowerCase(Locale.ROOT);
  }
}
