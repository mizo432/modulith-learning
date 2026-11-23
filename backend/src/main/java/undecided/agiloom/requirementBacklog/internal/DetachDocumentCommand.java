package undecided.agiloom.requirementBacklog.internal;

import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/** バックログアイテムからのファイル添付解除コマンド。 */
@Getter
@Builder
class DetachDocumentCommand {

  /** 添付解除元のバックログアイテム ID。 */
  @NonNull private final SnowflakeId backlogItemId;

  /** 添付を解除する帳票 ID（系列の最新バージョン）。 */
  @NonNull private final SnowflakeId documentId;

  /** 操作者の従業員 ID。 */
  @Nullable private final SnowflakeId actorEmployeeId;
}
