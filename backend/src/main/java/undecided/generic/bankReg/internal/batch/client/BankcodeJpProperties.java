package undecided.generic.bankReg.internal.batch.client;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * BankcodeJP Master Export API への接続設定。
 *
 * @param baseUrl API のベース URL
 * @param apiKey Master Export 契約アカウントの API キー（未設定の場合、定期実行はスキップされます）
 */
@ConfigurationProperties("batch.bank-import.bankcode-jp")
public record BankcodeJpProperties(
    @DefaultValue("https://apis.bankcode-jp.com") String baseUrl, @Nullable String apiKey) {

  /**
   * API キーが設定されているかを判定します。
   *
   * @return API キーが空でない場合 true
   */
  public boolean hasApiKey() {
    return apiKey != null && !apiKey.isBlank();
  }
}
