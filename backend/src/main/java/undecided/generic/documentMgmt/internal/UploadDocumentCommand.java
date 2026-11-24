package undecided.generic.documentMgmt.internal;

import java.io.InputStream;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/** 帳票アップロードのコマンド。 */
@Getter
@Builder
class UploadDocumentCommand {

  /** ファイル名（元のファイル名）。 */
  @NonNull
  private final String fileName;

  /** Content-Type（MIME タイプ）。 */
  @NonNull
  private final String contentType;

  /** ファイルサイズ（バイト数）。 */
  private final long fileSize;

  /** ファイルの内容。 */
  @NonNull
  private final InputStream content;

  /** アップローダーの従業員 ID。 */
  @Nullable
  private final SnowflakeId uploaderEmployeeId;

  /** アップローダー名。 */
  @Nullable
  private final String uploaderName;

  /** バージョン系列 ID（系列を指定しない場合はファイル名で系列判定する）。 */
  @Nullable
  private final SnowflakeId seriesId;
}
