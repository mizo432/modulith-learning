package com.github.mizo432.shared.sample.jpa.entity.inheritance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * 車両（Vehicle）を表す抽象エンティティで、SINGLE_TABLE継承戦略のサンプルです。
 *
 * <p>{@code InheritanceType.SINGLE_TABLE}を使用すると、 基底クラスとすべてのサブクラスが一つのテーブルにマッピングされます。
 * ディスクリミネーターカラム（discriminator）で各サブクラスを区別します。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>一つのテーブルにすべてのサブクラスデータが格納される
 *   <li>読み込みパフォーマンスが向上（JOIN不要）
 *   <li>サブクラス固有のフィールドがnullになる可能性がある
 *   <li>ディスクリミネーターカラムで型を区別
 * </ul>
 *
 * <p>継承階層:
 *
 * <pre>
 * SampleVehicle (SINGLE_TABLE)
 *   ├── SampleCar
 *   └── SampleTruck
 * </pre>
 */
@Entity
@Table(name = "sample_vehicles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SampleVehicle {

  @Id private SnowflakeId vehicleId;

  /** 車両名 */
  @Column(nullable = false, length = 100)
  private String vehicleName;

  /** 車両番号 */
  @Column(nullable = false, length = 20, unique = true)
  private String licensePlate;

  /** 車両状態 */
  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private VehicleStatus status;

  protected SampleVehicle(SnowflakeId vehicleId, String vehicleName, String licensePlate) {
    this.vehicleId = vehicleId;
    this.vehicleName = vehicleName;
    this.licensePlate = licensePlate;
    this.status = VehicleStatus.ACTIVE;
  }

  /** 車両のステータスを表す列挙型です。 */
  public enum VehicleStatus {
    ACTIVE,
    INACTIVE,
    MAINTENANCE
  }
}
