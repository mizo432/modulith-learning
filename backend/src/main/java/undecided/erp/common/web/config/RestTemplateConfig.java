package undecided.erp.common.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateの設定クラス。
 * <p>
 * このクラスは、アプリケーション内でHTTPリクエストを行うためのRestTemplateのBeanを提供します。
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplateのBeanを作成します。
     *
     * @return 設定されたRestTemplateインスタンス
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
