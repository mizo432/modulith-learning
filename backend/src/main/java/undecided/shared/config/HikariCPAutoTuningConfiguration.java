package undecided.shared.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * HikariCP接続プールの実行時オートチューニング設定クラスです。
 *
 * <p>このクラスは、HikariCPの接続プール設定をアプリケーション実行中に 動的に調整するための機能を有効にします。
 *
 * <p>オートチューニングを有効にするには、application.ymlで以下の設定を行います:
 *
 * <pre>
 * hikari:
 *   auto-tuning:
 *     enabled: true
 *     interval-seconds: 60
 *     max-pool-size-limit: 50
 *     min-idle-limit: 1
 * </pre>
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(HikariCPAutoTuningProperties.class)
public class HikariCPAutoTuningConfiguration {}
