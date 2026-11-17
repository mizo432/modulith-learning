package com.github.mizo432.shared.sample.jpa.entity.inheritance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleTruckの単体テスト")
class SampleTruckTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数でトラックエンティティが作成される")
    void shouldCreateTruckWithValidArguments() {
      // Arrange
      SnowflakeId vehicleId = SnowflakeId.of(1L);
      String vehicleName = "日産・アトラス";
      String licensePlate = "東京お50-99";
      Integer maxLoadWeight = 10000;
      Double cargoLength = 6.1;

      // Act
      SampleTruck truck =
          new SampleTruck(vehicleId, vehicleName, licensePlate, maxLoadWeight, cargoLength);

      // Assert
      assertThat(truck).isNotNull();
      assertThat(truck.getVehicleId()).isEqualTo(vehicleId);
      assertThat(truck.getVehicleName()).isEqualTo(vehicleName);
      assertThat(truck.getLicensePlate()).isEqualTo(licensePlate);
      assertThat(truck.getMaxLoadWeight()).isEqualTo(maxLoadWeight);
      assertThat(truck.getCargoLength()).isEqualTo(cargoLength);
    }

    @Test
    @DisplayName("オプションのフィールドがnullでも作成される")
    void shouldCreateTruckWithNullOptionalFields() {
      // Arrange & Act
      SampleTruck truck = new SampleTruck(SnowflakeId.of(1L), "車両名", "番号", null, null);

      // Assert
      assertThat(truck.getMaxLoadWeight()).isNull();
      assertThat(truck.getCargoLength()).isNull();
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("最大積載量が設定・取得できる")
    void shouldSetAndGetMaxLoadWeight() {
      // Arrange
      SampleTruck truck = new SampleTruck(SnowflakeId.of(1L), "車両名", "番号", 10000, 6.1);

      // Act
      truck.setMaxLoadWeight(15000);

      // Assert
      assertThat(truck.getMaxLoadWeight()).isEqualTo(15000);
    }

    @Test
    @DisplayName("荷室長さが設定・取得できる")
    void shouldSetAndGetCargoLength() {
      // Arrange
      SampleTruck truck = new SampleTruck(SnowflakeId.of(1L), "車両名", "番号", 10000, 6.1);

      // Act
      truck.setCargoLength(7.2);

      // Assert
      assertThat(truck.getCargoLength()).isEqualTo(7.2);
    }
  }

  @Nested
  @DisplayName("継承のテスト")
  class InheritanceTest {

    @Test
    @DisplayName("SampleTruckはSampleVehicleのサブクラスである")
    void shouldBeSubclassOfSampleVehicle() {
      // Arrange & Act
      SampleTruck truck = new SampleTruck(SnowflakeId.of(1L), "車両名", "番号", 10000, 6.1);

      // Assert
      assertThat(truck).isInstanceOf(SampleVehicle.class);
    }

    @Test
    @DisplayName("SampleTruckはSampleCarのサブクラスではない")
    void shouldNotBeSubclassOfSampleCar() {
      // Arrange & Act
      SampleTruck truck = new SampleTruck(SnowflakeId.of(1L), "車両名", "番号", 10000, 6.1);

      // Assert
      assertThat(truck).isNotInstanceOf(SampleCar.class);
    }

    @Test
    @DisplayName("継承されたstatusフィールドはACTIVEがデフォルトで設定される")
    void shouldHaveActiveStatusByDefault() {
      // Arrange & Act
      SampleTruck truck = new SampleTruck(SnowflakeId.of(1L), "車両名", "番号", 10000, 6.1);

      // Assert
      assertThat(truck.getStatus()).isEqualTo(SampleVehicle.VehicleStatus.ACTIVE);
    }
  }
}
