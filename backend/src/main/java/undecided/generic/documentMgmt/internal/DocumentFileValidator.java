package undecided.generic.documentMgmt.internal;

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
    throw new UnsupportedOperationException("not implemented");
  }
}
