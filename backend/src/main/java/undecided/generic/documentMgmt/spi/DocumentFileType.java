package undecided.generic.documentMgmt.spi;

/**
 * 帳票のファイル種別区分値。
 *
 * <p>許可する拡張子は、画像（png/jpg/gif/webp）、PDF（pdf）、テキスト（txt/md/csv）、文書（docx/xlsx/pptx）。
 */
public enum DocumentFileType {
  IMAGE,
  PDF,
  TEXT,
  DOCUMENT;

  /**
   * 拡張子からファイル種別を判定する。大文字小文字は区別しない。
   *
   * @param extension ドットを含まない拡張子
   * @return 拡張子に対応するファイル種別
   * @throws IllegalArgumentException 許可されていない拡張子、または空文字の場合
   */
  public static DocumentFileType fromExtension(String extension) {
    throw new UnsupportedOperationException("not implemented");
  }
}
