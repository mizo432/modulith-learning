package undecided.shared.sample.jpa.entity.inheritance;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 乗用車（Car）を表すエンティティで、SINGLE_TABLE継承戦略のサブクラスサンプルです。
 *
 * <p>{@code @DiscriminatorValue}でこのサブクラスを区別する値を指定します。 SINGLE_TABLE戦略では、このクラスの固有フィールド（seatCount）も
 * 親テーブル（sample_vehicles）にカラムとして追加されます。
 */
@Entity
@Table(name = "sample_vehicles")
@DiscriminatorValue("CAR")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleCar extends SampleVehicle {

  /** 定員数 */
  @Column(name = "seat_count")
  private Integer seatCount;

  /** トランク容量（リットル） */
  @Column(name = "trunk_capacity")
  private Integer trunkCapacity;

  public SampleCar(
      SnowflakeId vehicleId,
      String vehicleName,
      String licensePlate,
      Integer seatCount,
      Integer trunkCapacity) {
    super(vehicleId, vehicleName, licensePlate);
    this.seatCount = seatCount;
    this.trunkCapacity = trunkCapacity;
  }
}
