package undecided.authorization.infra.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 暗号化設定クラスです。
 *
 * <p>このクラスは、データベースに格納される機微な情報の暗号化/復号化に使用する 暗号化器をBeanとして提供します。AES-256方式を使用し、鍵は環境変数から取得します。
 *
 * <p>暗号化鍵は環境変数 {@code ENCRYPTION_KEY} から読み込みます。 開発環境ではデフォルト鍵を使用し、本番環境では適切な鍵を設定してください。
 */
@Configuration
public class EncryptionConfig {

  /** 暗号化アルゴリズム。 */
  public static final String ALGORITHM = "PBEWITHHMACSHA512ANDAES_256";

  /** プールサイズ。 */
  public static final int POOL_SIZE = 2;

  /** 反復回数。 */
  public static final int ITERATIONS = 1000;

  /**
   * 文字列暗号化器をBeanとして提供します。
   *
   * @return 設定された {@link StringEncryptor} インスタンス
   */
  @Bean("stringEncryptor")
  public StringEncryptor stringEncryptor() {
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setAlgorithm(ALGORITHM);
    config.setPassword(System.getenv("ENCRYPTION_KEY"));
    config.setPoolSize(POOL_SIZE);
    config.setKeyObtentionIterations(ITERATIONS);
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
    encryptor.setConfig(config);
    return encryptor;
  }
}
