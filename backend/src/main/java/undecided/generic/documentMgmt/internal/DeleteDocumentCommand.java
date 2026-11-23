package undecided.generic.documentMgmt.internal;

import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/** 帳票論理削除のコマンド。 */
@Getter
@Builder
class DeleteDocumentCommand {

  /** 削除対象の帳票 ID。 */
  @NonNull
  private final SnowflakeId documentId;

  /** 削除を実行する従業員 ID（アップロード者と一致する必要がある）。 */
  @NonNull
  private final SnowflakeId actorEmployeeId;
}
