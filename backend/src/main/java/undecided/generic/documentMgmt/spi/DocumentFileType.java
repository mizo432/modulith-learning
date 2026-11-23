package undecided.generic.documentMgmt.spi;

import java.util.Locale;
import java.util.Map;

/**
 * 帳票のファイル種別区分値。
 *
 * <p>許可する拡張子は、画像（png/jpg/gif/webp）、PDF（pdf）、テキスト（txt/md/csv）、文書（docx/xlsx/pptx）。
 * 拡張子ごとに対応する Content-Type を1つ持つ。
 */
public enum DocumentFileType {
  IMAGE(
      Map.of(
          "png", "image/png",
          "jpg", "image/jpeg",
          "gif", "image/gif",
          "webp", "image/webp")),
  PDF(Map.of("pdf", "application/pdf")),
  TEXT(
      Map.of(
          "txt", "text/plain",
          "md", "text/markdown",
          "csv", "text/csv")),
  DOCUMENT(
      Map.of(
          "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
          "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
          "pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"));

  /** 拡張子（小文字）から Content-Type への対応。 */
  private final Map<String, String> contentTypesByExtension;

  DocumentFileType(Map<String, String> contentTypesByExtension) {
    this.contentTypesByExtension = contentTypesByExtension;
  }

  /**
   * 拡張子からファイル種別を判定する。大文字小文字は区別しない。
   *
   * @param extension ドットを含まない拡張子
   * @return 拡張子に対応するファイル種別
   * @throws IllegalArgumentException 許可されていない拡張子、または空文字の場合
   */
  public static DocumentFileType fromExtension(String extension) {
    String normalized = normalize(extension);
    for (DocumentFileType type : values()) {
      if (type.contentTypesByExtension.containsKey(normalized)) {
        return type;
      }
    }
    throw new IllegalArgumentException("許可されていない拡張子です: " + extension);
  }

  /**
   * 拡張子に対応する Content-Type を返す。大文字小文字は区別しない。
   *
   * @param extension ドットを含まない拡張子
   * @return 拡張子に対応する Content-Type（小文字）
   * @throws IllegalArgumentException 許可されていない拡張子、または空文字の場合
   */
  public static String contentTypeOf(String extension) {
    String normalized = normalize(extension);
    return fromExtension(normalized).contentTypesByExtension.get(normalized);
  }

  private static String normalize(String extension) {
    if (extension == null || extension.isEmpty()) {
      throw new IllegalArgumentException("拡張子が空です");
    }
    return extension.toLowerCase(Locale.ROOT);
  }
}
