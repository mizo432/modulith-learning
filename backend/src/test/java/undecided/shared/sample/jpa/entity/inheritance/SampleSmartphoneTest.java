package undecided.shared.sample.jpa.entity.inheritance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleSmartphoneの単体テスト")
class SampleSmartphoneTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数でスマートフォンエンティティが作成される")
    void shouldCreateSmartphoneWithValidArguments() {
      // Arrange
      SnowflakeId deviceId = SnowflakeId.of(1L);
      String deviceName = "iPhone 15";
      String serialNumber = "SN002";
      Double displaySize = 6.1;
      Integer simSlots = 2;
      Boolean supports5g = true;

      // Act
      SampleSmartphone smartphone =
          new SampleSmartphone(
              deviceId, deviceName, serialNumber, displaySize, simSlots, supports5g);

      // Assert
      assertThat(smartphone).isNotNull();
      assertThat(smartphone.getDeviceId()).isEqualTo(deviceId);
      assertThat(smartphone.getDeviceName()).isEqualTo(deviceName);
      assertThat(smartphone.getSerialNumber()).isEqualTo(serialNumber);
      assertThat(smartphone.getDisplaySize()).isEqualTo(displaySize);
      assertThat(smartphone.getSimSlots()).isEqualTo(simSlots);
      assertThat(smartphone.getSupports5g()).isTrue();
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("ディスプレイサイズが設定・取得できる")
    void shouldSetAndGetDisplaySize() {
      // Arrange
      SampleSmartphone smartphone =
          new SampleSmartphone(SnowflakeId.of(1L), "iPhone", "SN002", 6.1, 2, true);

      // Act
      smartphone.setDisplaySize(6.7);

      // Assert
      assertThat(smartphone.getDisplaySize()).isEqualTo(6.7);
    }

    @Test
    @DisplayName("SIMカード対応数が設定・取得できる")
    void shouldSetAndGetSimSlots() {
      // Arrange
      SampleSmartphone smartphone =
          new SampleSmartphone(SnowflakeId.of(1L), "iPhone", "SN002", 6.1, 2, true);

      // Act
      smartphone.setSimSlots(1);

      // Assert
      assertThat(smartphone.getSimSlots()).isEqualTo(1);
    }

    @Test
    @DisplayName("5G対応フラグが設定・取得できる")
    void shouldSetAndGetSupports5g() {
      // Arrange
      SampleSmartphone smartphone =
          new SampleSmartphone(SnowflakeId.of(1L), "iPhone", "SN002", 6.1, 2, true);

      // Act
      smartphone.setSupports5g(false);

      // Assert
      assertThat(smartphone.getSupports5g()).isFalse();
    }
  }

  @Nested
  @DisplayName("継承のテスト")
  class InheritanceTest {

    @Test
    @DisplayName("SampleSmartphoneはSampleDeviceのサブクラスである")
    void shouldBeSubclassOfSampleDevice() {
      // Arrange & Act
      SampleSmartphone smartphone =
          new SampleSmartphone(SnowflakeId.of(1L), "iPhone", "SN002", 6.1, 2, true);

      // Assert
      assertThat(smartphone).isInstanceOf(SampleDevice.class);
    }
  }
}
