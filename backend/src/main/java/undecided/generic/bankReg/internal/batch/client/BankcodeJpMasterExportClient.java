package undecided.generic.bankReg.internal.batch.client;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import undecided.supporting.exception.SystemException;

/**
 * BankcodeJP Master Export API のクライアント。
 *
 * <p>API 呼び出しには {@code x-api-key} ヘッダーを付与し、署名付き URL からの ZIP ダウンロードには付与しません。
 *
 * @see <a href="https://api.docs.bankcode-jp.com/master-export.html">Master Export API</a>
 */
public class BankcodeJpMasterExportClient {

  static final String API_KEY_HEADER = "x-api-key";
  static final String ERROR_CODE = "e.bank.import.3001";

  private final RestClient restClient;
  private final @Nullable String apiKey;

  /**
   * コンストラクタ。
   *
   * @param restClient ベース URL を設定済みの {@link RestClient}
   * @param apiKey Master Export 契約アカウントの API キー
   */
  public BankcodeJpMasterExportClient(RestClient restClient, @Nullable String apiKey) {
    this.restClient = restClient;
    this.apiKey = apiKey;
  }

  /**
   * 現在公開中のデータセット情報を取得します。
   *
   * @return データセット情報
   */
  public @NonNull MasterExportDataset fetchLatest() {
    MasterExportDataset dataset =
        restClient
            .get()
            .uri("/master/v1/latest")
            .header(API_KEY_HEADER, requireApiKey())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(MasterExportDataset.class);
    if (dataset == null || dataset.datasetId() == null || dataset.datasetId().isBlank()) {
      throw new SystemException(ERROR_CODE, "Master Export latest response has no dataset_id");
    }
    return dataset;
  }

  /**
   * 指定したデータセットのダウンロード用署名付き URL を発行します。
   *
   * <p>発行回数には日次上限があるため、URL の期限切れまたはダウンロード失敗時のみ再発行してください。
   *
   * @param datasetId データセットID
   * @return 署名付き URL
   */
  public @NonNull MasterExportDownloadUrl issueDownloadUrl(@NonNull String datasetId) {
    checkNotNull(datasetId, () -> new IllegalArgumentException("datasetId must not be null"));
    MasterExportDownloadUrl downloadUrl =
        restClient
            .post()
            .uri("/master/v1/download-url")
            .header(API_KEY_HEADER, requireApiKey())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Map.of("dataset_id", datasetId))
            .retrieve()
            .body(MasterExportDownloadUrl.class);
    if (downloadUrl == null
        || downloadUrl.downloadUrl() == null
        || downloadUrl.downloadUrl().isBlank()) {
      throw new SystemException(ERROR_CODE, "Master Export download-url response has no URL");
    }
    return downloadUrl;
  }

  /**
   * 署名付き URL から ZIP をダウンロードしてファイルに保存します。
   *
   * @param downloadUrl 署名付き URL
   * @param target 保存先ファイル
   */
  public void download(@NonNull MasterExportDownloadUrl downloadUrl, @NonNull Path target) {
    checkNotNull(downloadUrl, () -> new IllegalArgumentException("downloadUrl must not be null"));
    checkNotNull(target, () -> new IllegalArgumentException("target must not be null"));
    restClient
        .get()
        .uri(URI.create(downloadUrl.downloadUrl()))
        .exchange(
            (request, response) -> {
              if (response.getStatusCode().isError()) {
                throw new SystemException(
                    ERROR_CODE,
                    "Failed to download Master Export ZIP: HTTP " + response.getStatusCode());
              }
              try (InputStream body = response.getBody()) {
                Files.copy(body, target, StandardCopyOption.REPLACE_EXISTING);
              } catch (IOException e) {
                throw new SystemException(ERROR_CODE, "Failed to save Master Export ZIP", e);
              }
              return null;
            });
  }

  private String requireApiKey() {
    if (apiKey == null || apiKey.isBlank()) {
      throw new SystemException(
          ERROR_CODE, "batch.bank-import.bankcode-jp.api-key (BANKCODE_JP_API_KEY) is not set");
    }
    return apiKey;
  }
}
