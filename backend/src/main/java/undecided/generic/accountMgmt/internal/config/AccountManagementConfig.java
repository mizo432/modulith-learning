package undecided.generic.accountMgmt.internal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * アカウント管理モジュールの設定クラスです。
 *
 * <p>authorizationサービスとの通信に使用するRestTemplateを提供します。
 */
@Configuration
public class AccountManagementConfig {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
