package undecided.erp.shared.presentation.web.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import undecided.shared.web.logging.HttpSessionEventLoggingListener;

@Configuration
public class SessionConfig {
    /**
     * HTTPセッションに関連するイベントのロギングを行うためのHttpSessionEventLoggingListenerを設定します。
     * <p>
     * このリスナーは、セッションの作成、破棄、属性の変更イベントなどを監視し、それらの情報をロギングします。
     *
     * @return HttpSessionEventLoggingListenerインスタンスを返します。これにより、セッション関連イベントのロギング機能が提供されます。
     */
    @Bean
    protected HttpSessionEventLoggingListener httpSessionEventLoggingListener() {
        return new HttpSessionEventLoggingListener();
    }
}
