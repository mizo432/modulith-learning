package undecided.generic.documentMgmt.spi;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 帳票管理モジュールの公開ファサード。
 *
 * <p>他モジュール（requirementBacklog 等）は、internal のサービスを直接参照せず、
 * このファサード経由で帳票のアップロード・取得・ダウンロード・論理削除を行います。
 */
public interface DocumentFacade {

  /**
   * 帳票をアップロードします。
   *
   * @param fileName ファイル名
   * @param contentType Content-Type（MIME タイプ）
   * @param fileSize ファイルサイズ（バイト数）
   * @param content ファイル内容
   * @param uploaderEmployeeId アップローダーの従業員 ID
   * @param uploaderName アップローダー名
   * @param seriesId バージョン系列 ID（null の場合はファイル名で系列判定する）
   * @return アップロードされた帳票の ID
   */
  @NonNull
  SnowflakeId upload(
      @NonNull String fileName,
      @NonNull String contentType,
      long fileSize,
      @NonNull InputStream content,
      @Nullable SnowflakeId uploaderEmployeeId,
      @Nullable String uploaderName,
      @Nullable SnowflakeId seriesId);

  /**
   * 帳票を ID で取得します（論理削除済みを含まない）。
   *
   * @param documentId 帳票 ID
   * @return 帳票
   */
  @NonNull
  Optional<Document> findById(@NonNull SnowflakeId documentId);

  /**
   * バージョン系列に属する帳票の一覧を取得します（全バージョン、論理削除済みを含む）。
   *
   * @param seriesId バージョン系列 ID
   * @return 系列内の帳票一覧
   */
  @NonNull
  List<Document> findBySeriesId(@NonNull SnowflakeId seriesId);

  /**
   * 帳票をダウンロードします。
   *
   * @param documentId 帳票 ID
   * @return ダウンロード内容
   */
  @NonNull
  DocumentContent download(@NonNull SnowflakeId documentId);

  /**
   * 帳票を論理削除します（削除できるのはアップロード者のみ）。
   *
   * @param documentId 帳票 ID
   * @param actorEmployeeId 操作者の従業員 ID
   */
  void delete(@NonNull SnowflakeId documentId, @Nullable SnowflakeId actorEmployeeId);
}
