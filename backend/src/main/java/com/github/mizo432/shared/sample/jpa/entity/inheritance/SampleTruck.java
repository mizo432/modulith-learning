package com.github.mizo432.shared.sample.jpa.entity.inheritance;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * トラック（Truck）を表すエンティティで、SINGLE_TABLE継承戦略のサブクラスサンプルです。
 *
 * <p>{@code @DiscriminatorValue}でこのサブクラスを区別する値を指定します。
 * SINGLE_TABLE戦略では、このクラスの固有フィールド（maxLoadWeight）も 親テーブル（sample_vehicles）にカラムとして追加されます。
 */
@Entity
@DiscriminatorValue("TRUCK")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleTruck extends SampleVehicle {

  /** 最大積載量（kg） */
  @Column(name = "max_load_weight")
  private Integer maxLoadWeight;

  /** 荷室長さ（m） */
  @Column(name = "cargo_length")
  private Double cargoLength;

  public SampleTruck(
      SnowflakeId vehicleId,
      String vehicleName,
      String licensePlate,
      Integer maxLoadWeight,
      Double cargoLength) {
    super(vehicleId, vehicleName, licensePlate);
    this.maxLoadWeight = maxLoadWeight;
    this.cargoLength = cargoLength;
  }
}
