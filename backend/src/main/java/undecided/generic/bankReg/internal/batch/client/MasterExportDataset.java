package undecided.generic.bankReg.internal.batch.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import org.jspecify.annotations.Nullable;

/**
 * {@code GET /master/v1/latest} が返す、現在公開中のデータセット情報。
 *
 * @param datasetId データセットID（新旧の比較には一致判定のみを使用する）
 * @param publishedAt 公開日時
 * @param zipSha256 ZIP 全体の SHA-256（16進 64 文字）
 * @param zipSizeBytes ZIP のバイト数
 * @param bankRowCount banks.csv のデータ行数（ヘッダーを除く）
 */
public record MasterExportDataset(
    @JsonProperty("dataset_id") String datasetId,
    @JsonProperty("published_at") @Nullable OffsetDateTime publishedAt,
    @JsonProperty("zip_sha256") String zipSha256,
    @JsonProperty("zip_size_bytes") long zipSizeBytes,
    @JsonProperty("bank_row_count") int bankRowCount) {}
