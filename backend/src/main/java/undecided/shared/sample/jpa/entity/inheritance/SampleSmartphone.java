package undecided.shared.sample.jpa.entity.inheritance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * スマートフォン（Smartphone）を表すエンティティで、JOINED継承戦略のサブクラスサンプルです。
 *
 * <p>JOINED戦略では、このクラスは独立したテーブル（sample_smartphones）を持ち、 親テーブル（sample_devices）と外部キーで関連付けられます。
 */
@Entity
@Table(name = "sample_smartphones")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleSmartphone extends SampleDevice {

  /** ディスプレイサイズ（インチ） */
  @Column(name = "display_size")
  private Double displaySize;

  /** SIMカード対応数 */
  @Column(name = "sim_slots")
  private Integer simSlots;

  /** 5G対応フラグ */
  @Column(name = "supports_5g")
  private Boolean supports5g;

  public SampleSmartphone(
      SnowflakeId deviceId,
      String deviceName,
      String serialNumber,
      Double displaySize,
      Integer simSlots,
      Boolean supports5g) {
    super(deviceId, deviceName, serialNumber);
    this.displaySize = displaySize;
    this.simSlots = simSlots;
    this.supports5g = supports5g;
  }
}
