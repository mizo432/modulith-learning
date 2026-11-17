package com.github.mizo432.shared.sample.jpa.entity.inheritance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleCarの単体テスト")
class SampleCarTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数で乗用車エンティティが作成される")
    void shouldCreateCarWithValidArguments() {
      // Arrange
      SnowflakeId vehicleId = SnowflakeId.of(1L);
      String vehicleName = "トヨタ・カローラ";
      String licensePlate = "東京あ12-34";
      Integer seatCount = 5;
      Integer trunkCapacity = 400;

      // Act
      SampleCar car = new SampleCar(vehicleId, vehicleName, licensePlate, seatCount, trunkCapacity);

      // Assert
      assertThat(car).isNotNull();
      assertThat(car.getVehicleId()).isEqualTo(vehicleId);
      assertThat(car.getVehicleName()).isEqualTo(vehicleName);
      assertThat(car.getLicensePlate()).isEqualTo(licensePlate);
      assertThat(car.getSeatCount()).isEqualTo(seatCount);
      assertThat(car.getTrunkCapacity()).isEqualTo(trunkCapacity);
    }

    @Test
    @DisplayName("オプションのフィールドがnullでも作成される")
    void shouldCreateCarWithNullOptionalFields() {
      // Arrange & Act
      SampleCar car = new SampleCar(SnowflakeId.of(1L), "車両名", "番号", null, null);

      // Assert
      assertThat(car.getSeatCount()).isNull();
      assertThat(car.getTrunkCapacity()).isNull();
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("定員数が設定・取得できる")
    void shouldSetAndGetSeatCount() {
      // Arrange
      SampleCar car = new SampleCar(SnowflakeId.of(1L), "車両名", "番号", 5, 400);

      // Act
      car.setSeatCount(4);

      // Assert
      assertThat(car.getSeatCount()).isEqualTo(4);
    }

    @Test
    @DisplayName("トランク容量が設定・取得できる")
    void shouldSetAndGetTrunkCapacity() {
      // Arrange
      SampleCar car = new SampleCar(SnowflakeId.of(1L), "車両名", "番号", 5, 400);

      // Act
      car.setTrunkCapacity(500);

      // Assert
      assertThat(car.getTrunkCapacity()).isEqualTo(500);
    }
  }

  @Nested
  @DisplayName("継承のテスト")
  class InheritanceTest {

    @Test
    @DisplayName("SampleCarはSampleVehicleのサブクラスである")
    void shouldBeSubclassOfSampleVehicle() {
      // Arrange & Act
      SampleCar car = new SampleCar(SnowflakeId.of(1L), "車両名", "番号", 5, 400);

      // Assert
      assertThat(car).isInstanceOf(SampleVehicle.class);
    }

    @Test
    @DisplayName("継承されたstatusフィールドはACTIVEがデフォルトで設定される")
    void shouldHaveActiveStatusByDefault() {
      // Arrange & Act
      SampleCar car = new SampleCar(SnowflakeId.of(1L), "車両名", "番号", 5, 400);

      // Assert
      assertThat(car.getStatus()).isEqualTo(SampleVehicle.VehicleStatus.ACTIVE);
    }

    @Test
    @DisplayName("継承されたstatusフィールドが変更できる")
    void shouldChangeInheritedStatus() {
      // Arrange
      SampleCar car = new SampleCar(SnowflakeId.of(1L), "車両名", "番号", 5, 400);

      // Act
      car.setStatus(SampleVehicle.VehicleStatus.MAINTENANCE);

      // Assert
      assertThat(car.getStatus()).isEqualTo(SampleVehicle.VehicleStatus.MAINTENANCE);
    }
  }
}
