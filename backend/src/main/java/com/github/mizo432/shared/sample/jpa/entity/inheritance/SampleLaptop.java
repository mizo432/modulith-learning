package com.github.mizo432.shared.sample.jpa.entity.inheritance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ノートパソコン（Laptop）を表すエンティティで、JOINED継承戦略のサブクラスサンプルです。
 *
 * <p>JOINED戦略では、このクラスは独立したテーブル（sample_laptops）を持ち、 親テーブル（sample_devices）と外部キーで関連付けられます。
 */
@Entity
@Table(name = "sample_laptops")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleLaptop extends SampleDevice {

  /** CPUコア数 */
  @Column(name = "cpu_cores")
  private Integer cpuCores;

  /** メモリ容量（GB） */
  @Column(name = "memory_gb")
  private Integer memoryGb;

  /** ストレージ容量（GB） */
  @Column(name = "storage_gb")
  private Integer storageGb;

  public SampleLaptop(
      SnowflakeId deviceId,
      String deviceName,
      String serialNumber,
      Integer cpuCores,
      Integer memoryGb,
      Integer storageGb) {
    super(deviceId, deviceName, serialNumber);
    this.cpuCores = cpuCores;
    this.memoryGb = memoryGb;
    this.storageGb = storageGb;
  }
}
