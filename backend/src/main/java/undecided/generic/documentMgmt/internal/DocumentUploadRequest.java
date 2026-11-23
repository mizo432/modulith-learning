package undecided.generic.documentMgmt.internal;

import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

/** 帳票アップロードリクエストの補助情報（Multipart の非ファイル部分）。 */
@Getter
@Builder
class DocumentUploadRequest {

  /** アップローダーの従業員 ID（文字列形式）。 */
  @Nullable
  private final String uploaderEmployeeId;

  /** アップローダー名。 */
  @Nullable
  private final String uploaderName;
}
