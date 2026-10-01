package undecided.generic.bankReg.internal.batch.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import org.jspecify.annotations.Nullable;

/**
 * {@code POST /master/v1/download-url} が返す、ZIP ダウンロード用の署名付き URL。
 *
 * <p>URL 自体が認証情報となるため、ログ等に出力してはいけません。
 *
 * @param datasetId 対象データセットID
 * @param downloadUrl 署名付き URL
 * @param expiresAt URL の有効期限
 */
public record MasterExportDownloadUrl(
    @JsonProperty("dataset_id") String datasetId,
    @JsonProperty("download_url") String downloadUrl,
    @JsonProperty("expires_at") @Nullable OffsetDateTime expiresAt) {

  @Override
  public String toString() {
    return "MasterExportDownloadUrl[datasetId=" + datasetId + ", expiresAt=" + expiresAt + "]";
  }
}
