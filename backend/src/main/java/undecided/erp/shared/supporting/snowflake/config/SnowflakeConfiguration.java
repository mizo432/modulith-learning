package undecided.erp.shared.supporting.snowflake.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import undecided.erp.shared.supporting.snowflake.internal.SnowflakeIdGeneratorImpl;
import undecided.erp.shared.supporting.snowflake.internal.SnowflakeNodeLeaseManager;
import undecided.erp.shared.supporting.snowflake.spi.SnowflakeIdGenerator;

/**
 * SnowflakeConfigurationクラスは、Snowflake IDの生成を設定するためのSpring構成クラスです。
 * <p>
 * このクラスは、Snowflakeアルゴリズムを使用して一意のIDを生成するために必要な依存関係を定義します。
 * 具体的には、SnowflakeIdGeneratorのインスタンスをSpringのBeanとして提供します。
 * <p>
 * Snowflakeアルゴリズムは、分散環境において一意の識別子を生成するために使用されます。この構成クラスは、 必要な構成パラメータ（例:
 * エポックの基準時間）を設定し、ノードIDを取得する仕組みを組み込んでいます。
 */
@Configuration
public class SnowflakeConfiguration {

  /**
   * Snowflakeアルゴリズムで使用される基準時間 (エポック時間) をミリ秒単位で表します。
   * <p>
   * この値は、Snowflake IDを生成する際のタイムスタンプ計算の基点として使用されます。 `EPOCH_MILLIS` は、1970年1月1日午前0時 (UTC)
   * を基準としたエポック時間をミリ秒単位で表す Long型の定数です。この値から現在時刻を差し引いてIDにタイムスタンプを割り当てます。
   * <p>
   * Snowflakeアルゴリズムでは、このエポック時間を調整することで生成されるIDの総数や時間範囲への 依存性を制御できます。一般的には、システムが稼働し始める基準の時間を設定します。
   * <p>
   * この値を適切に設定しないと、生成されるIDが重複するリスクや使用可能な時間範囲に制限が 生じる可能性があるため、設定には注意が必要です。
   */
  private static final long EPOCH_MILLIS = 1735689600000L;

  /**
   * SnowflakeIdGenerator Beanを提供します。このメソッドは、Snowflakeアルゴリズムを使用して一意のIDを生成するための
   * SnowflakeIdGeneratorインスタンスを作成します。
   *
   * @param leaseManager SnowflakeノードIDを管理するためのSnowflakeNodeLeaseManagerインスタンス。 ノードIDはSnowflake
   * ID生成プロセスで必要です。
   * @return Snowflakeアルゴリズムを用いて一意の識別子を生成するためのSnowflakeIdGeneratorインスタンス。
   */
  @Bean
  public SnowflakeIdGenerator snowflakeIdGenerator(SnowflakeNodeLeaseManager leaseManager) {
    return new SnowflakeIdGeneratorImpl(leaseManager.nodeId(),
        EPOCH_MILLIS);

  }
}
