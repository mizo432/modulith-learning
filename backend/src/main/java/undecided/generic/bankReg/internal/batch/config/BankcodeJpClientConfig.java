package undecided.generic.bankReg.internal.batch.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import undecided.generic.bankReg.internal.batch.client.BankcodeJpMasterExportClient;
import undecided.generic.bankReg.internal.batch.client.BankcodeJpProperties;

/** BankcodeJP Master Export API クライアントの設定クラス。 */
@Configuration
@EnableConfigurationProperties(BankcodeJpProperties.class)
public class BankcodeJpClientConfig {

  @Bean
  public BankcodeJpMasterExportClient bankcodeJpMasterExportClient(
      BankcodeJpProperties properties) {
    RestClient restClient = RestClient.builder().baseUrl(properties.baseUrl()).build();
    return new BankcodeJpMasterExportClient(restClient, properties.apiKey());
  }
}
