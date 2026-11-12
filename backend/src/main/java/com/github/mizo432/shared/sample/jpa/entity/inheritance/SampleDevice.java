package com.github.mizo432.shared.sample.jpa.entity.inheritance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * デバイス（Device）を表す抽象エンティティで、JOINED継承戦略のサンプルです。
 *
 * <p>{@code InheritanceType.JOINED}を使用すると、 各クラスが個別のテーブルにマッピングされ、 外部キーで関連付けられます。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>基底クラスと各サブクラスが個別のテーブルを持つ
 *   <li>データ重複がない（正規化された構造）
 *   <li>読み込み時にJOINが必要になる
 *   <li>サブクラス追加時に既存テーブルを変更しない
 * </ul>
 *
 * <p>継承階層:
 *
 * <pre>
 * SampleDevice (JOINED)
 *   ├── SampleLaptop
 *   └── SampleSmartphone
 * </pre>
 */
@Entity
@Table(name = "sample_devices")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SampleDevice {

  @Id private SnowflakeId deviceId;

  /** デバイス名 */
  @Column(nullable = false, length = 100)
  private String deviceName;

  /** シリアル番号 */
  @Column(nullable = false, length = 50, unique = true)
  private String serialNumber;

  protected SampleDevice(SnowflakeId deviceId, String deviceName, String serialNumber) {
    this.deviceId = deviceId;
    this.deviceName = deviceName;
    this.serialNumber = serialNumber;
  }
}
