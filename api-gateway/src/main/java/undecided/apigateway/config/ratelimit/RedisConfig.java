package undecided.apigateway.config.ratelimit;

import io.github.bucket4j.distributed.proxy.AsyncProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisベースの分散レートリミッティング設定
 *
 * <p>詳細設計書 10.1節に基づくRedisベースのレートリミッティングを実装します。
 * Bucket4jとLettuceを使用して、複数のAPIゲートウェイインスタンス間で
 * レートリミットの状態を共有します。
 *
 * <p>Spring Cloud Gateway WebMVCの{@code rateLimit}フィルタは、
 * {@link AsyncProxyManager} Beanを検出すると、自動的にRedisベースの
 * 分散バケットを使用します。Beanが存在しない場合は、インメモリのローカルバケット
 * にフォールバックします。
 */
@Configuration
@org.springframework.context.annotation.Profile("!test")
public class RedisConfig {

  /**
   * Redisクライアントを作成します。
   *
   * @param host Redisホスト（デフォルト: localhost）
   * @param port Redisポート（デフォルト: 6379）
   * @param password Redisパスワード（デフォルト: 空文字列）
   * @return RedisClient
   */
  @Bean(destroyMethod = "shutdown")
  public RedisClient redisClient(
      @Value("${spring.data.redis.host:localhost}") String host,
      @Value("${spring.data.redis.port:6379}") int port,
      @Value("${spring.data.redis.password:}") String password) {
    RedisURI.Builder builder = RedisURI.builder().withHost(host).withPort(port);
    if (password != null && !password.isEmpty()) {
      builder.withPassword(password.toCharArray());
    }
    return RedisClient.create(builder.build());
  }

  /**
   * Redis接続を作成します。
   *
   * <p>Bucket4jのLettuceベースのProxyManagerは、Stringキーとbyte[]値の
   * Redis接続を必要とします。キーはクライアント識別子（IPアドレスやPrincipal名）で、
   * 値はシリアライズされたバケット状態です。
   *
   * @param redisClient Redisクライアント
   * @return StatefulRedisConnection
   */
  @Bean
  public StatefulRedisConnection<String, byte[]> redisConnection(RedisClient redisClient) {
    return redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));
  }

  /**
   * RedisベースのAsyncProxyManagerを作成します。
   *
   * <p>Spring Cloud Gateway WebMVCの{@code rateLimit}フィルタは、
   * このBeanを検出すると、自動的にRedisベースの分散バケットを使用します。
   *
   * <p>有効期限戦略: バケットの補充に最大2分かかるように設定しています。
   * これにより、クライアントが一定期間リクエストを送信しない場合、
   * Redis上のバケットデータは自動的に削除されます。
   *
   * @param connection Redis接続
   * @return AsyncProxyManager
   */
  @Bean
  public AsyncProxyManager<String> asyncProxyManager(
      StatefulRedisConnection<String, byte[]> connection) {
    return LettuceBasedProxyManager.builderFor(connection)
        .withExpirationStrategy(
            io.github.bucket4j.distributed.ExpirationAfterWriteStrategy
                .basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(2)))
        .build()
        .asAsync();
  }
}
