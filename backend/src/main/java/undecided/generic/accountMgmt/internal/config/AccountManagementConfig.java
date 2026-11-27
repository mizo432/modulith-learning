package undecided.generic.accountMgmt.internal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import undecided.supporting.http.LoggingClientHttpRequestInterceptor;

/**
 * アカウント管理モジュールの設定クラスです。
 *
 * <p>authorizationサービスとの通信に使用するRestClientを提供します。
 */
@Configuration
public class AccountManagementConfig {

  @Bean
  public RestClient accountManagementRestClient() {
    return RestClient.builder()
        .requestInterceptor(new LoggingClientHttpRequestInterceptor())
        .build();
  }
}
