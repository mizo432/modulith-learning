package undecided.agiloom.requirementBacklog.internal;

import java.io.InputStream;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムへのファイル添付コマンド。 */
@Getter
@Builder
class AttachDocumentCommand {

  /** 添付先のバックログアイテム ID。 */
  @NonNull private final SnowflakeId backlogItemId;

  /** ファイル名（元のファイル名）。 */
  @NonNull private final String fileName;

  /** Content-Type（MIME タイプ）。 */
  @NonNull private final String contentType;

  /** ファイルサイズ（バイト数）。 */
  private final long fileSize;

  /** ファイルの内容。 */
  @NonNull private final InputStream content;

  /** アップローダーの従業員 ID。 */
  @Nullable private final SnowflakeId uploaderEmployeeId;

  /** アップローダー名。 */
  @Nullable private final String uploaderName;

  /** バージョン系列 ID（再アップロード時に既存系列を指定する。新規添付は null）。 */
  @Nullable private final SnowflakeId seriesId;
}
