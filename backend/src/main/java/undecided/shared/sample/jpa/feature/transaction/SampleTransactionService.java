package undecided.shared.sample.jpa.feature.transaction;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * @Transactionalの使用パターンを示すサンプルサービスです。
 *
 * <p>トランザクション管理のPropagation、Isolation、readOnlyなどの設定パターンを示します。
 */
@Transactional
public interface SampleTransactionService {

  /**
   * デフォルトのトランザクション設定でデータを保存します。
   *
   * <p>Propagation.REQUIRED、Isolation.DEFAULTが適用されます。
   *
   * @param name 保存する名前
   * @return 生成されたID
   */
  SnowflakeId saveWithDefaultTransaction(String name);

  /**
   * 読み取り専用トランザクションでデータを検索します。
   *
   * <p>readOnly = trueにより、パフォーマンスが向上します。
   *
   * @param id 検索するID
   * @return 検索結果
   */
  @Transactional(readOnly = true)
  String findWithReadOnlyTransaction(SnowflakeId id);

  /**
   * 新しいトランザクションでデータを保存します。
   *
   * <p>Propagation.REQUIRES_NEWにより、既存のトランザクションが一時停止され、 新しい独立したトランザクションが作成されます。
   *
   * @param name 保存する名前
   * @return 生成されたID
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  SnowflakeId saveWithNewTransaction(String name);

  /**
   * 指定されたIsolationレベルでデータを保存します。
   *
   * <p>Isolation.SERIALIZABLEにより、完全な分離が保証されます。
   *
   * @param name 保存する名前
   * @return 生成されたID
   */
  @Transactional(isolation = Isolation.SERIALIZABLE)
  SnowflakeId saveWithSerializableIsolation(String name);

  /**
   * トランザクションなしで実行されます。
   *
   * <p>Propagation.SUPPORTSにより、トランザクションが存在すれば参加し、 存在しなければトランザクションなしで実行されます。
   *
   * @param id 検索するID
   * @return 検索結果
   */
  @Transactional(propagation = Propagation.SUPPORTS)
  String findWithOptionalTransaction(SnowflakeId id);

  /**
   * 例外発生時にロールバックするパターンを示します。
   *
   * <p>RuntimeExceptionはデフォルトでロールバックされますが、 CheckedExceptionは明示的に指定する必要があります。
   *
   * @param name 保存する名前
   * @return 生成されたID
   */
  @Transactional(rollbackFor = Exception.class)
  SnowflakeId saveWithRollbackForException(String name);
}
