package undecided.generic.bankReg.internal.batch.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;
import undecided.generic.bankReg.internal.batch.client.BankcodeJpMasterExportClient;
import undecided.generic.bankReg.internal.batch.client.BankcodeJpProperties;

/**
 * 金融機関インポートバッチのインフラストラクチャ設定クラス。
 *
 * <p>BankcodeJP Master Export API 用の {@link RestClient} を構成し、 {@link BankcodeJpMasterExportClient}
 * を生成します。
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(BankcodeJpProperties.class)
public class BankImportInfrastructureConfig {

  @Bean
  public RestClient bankcodeJpRestClient(BankcodeJpProperties properties) {
    return RestClient.builder().baseUrl(properties.baseUrl()).build();
  }

  @Bean
  public BankcodeJpMasterExportClient bankcodeJpMasterExportClient(
      RestClient bankcodeJpRestClient, BankcodeJpProperties properties) {
    return new BankcodeJpMasterExportClient(bankcodeJpRestClient, properties.apiKey());
  }
}
