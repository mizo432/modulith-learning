package undecided.shared.sample.jpa.entity.inheritance;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleLaptopの単体テスト")
class SampleLaptopTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数でノートパソコンエンティティが作成される")
    void shouldCreateLaptopWithValidArguments() {
      // Arrange
      SnowflakeId deviceId = SnowflakeId.of(1L);
      String deviceName = "MacBook Pro";
      String serialNumber = "SN001";
      Integer cpuCores = 8;
      Integer memoryGb = 16;
      Integer storageGb = 512;

      // Act
      SampleLaptop laptop =
          new SampleLaptop(deviceId, deviceName, serialNumber, cpuCores, memoryGb, storageGb);

      // Assert
      assertThat(laptop).isNotNull();
      assertThat(laptop.getDeviceId()).isEqualTo(deviceId);
      assertThat(laptop.getDeviceName()).isEqualTo(deviceName);
      assertThat(laptop.getSerialNumber()).isEqualTo(serialNumber);
      assertThat(laptop.getCpuCores()).isEqualTo(cpuCores);
      assertThat(laptop.getMemoryGb()).isEqualTo(memoryGb);
      assertThat(laptop.getStorageGb()).isEqualTo(storageGb);
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("CPUコア数が設定・取得できる")
    void shouldSetAndGetCpuCores() {
      // Arrange
      SampleLaptop laptop = new SampleLaptop(SnowflakeId.of(1L), "MacBook", "SN001", 8, 16, 512);

      // Act
      laptop.setCpuCores(10);

      // Assert
      assertThat(laptop.getCpuCores()).isEqualTo(10);
    }

    @Test
    @DisplayName("メモリ容量が設定・取得できる")
    void shouldSetAndGetMemoryGb() {
      // Arrange
      SampleLaptop laptop = new SampleLaptop(SnowflakeId.of(1L), "MacBook", "SN001", 8, 16, 512);

      // Act
      laptop.setMemoryGb(32);

      // Assert
      assertThat(laptop.getMemoryGb()).isEqualTo(32);
    }

    @Test
    @DisplayName("ストレージ容量が設定・取得できる")
    void shouldSetAndGetStorageGb() {
      // Arrange
      SampleLaptop laptop = new SampleLaptop(SnowflakeId.of(1L), "MacBook", "SN001", 8, 16, 512);

      // Act
      laptop.setStorageGb(1024);

      // Assert
      assertThat(laptop.getStorageGb()).isEqualTo(1024);
    }
  }

  @Nested
  @DisplayName("継承のテスト")
  class InheritanceTest {

    @Test
    @DisplayName("SampleLaptopはSampleDeviceのサブクラスである")
    void shouldBeSubclassOfSampleDevice() {
      // Arrange & Act
      SampleLaptop laptop = new SampleLaptop(SnowflakeId.of(1L), "MacBook", "SN001", 8, 16, 512);

      // Assert
      assertThat(laptop).isInstanceOf(SampleDevice.class);
    }

    @Test
    @DisplayName("SampleLaptopはSampleSmartphoneのサブクラスではない")
    void shouldNotBeSubclassOfSampleSmartphone() {
      // Arrange & Act
      SampleLaptop laptop = new SampleLaptop(SnowflakeId.of(1L), "MacBook", "SN001", 8, 16, 512);

      // Assert
      assertThat(laptop).isNotInstanceOf(SampleSmartphone.class);
    }
  }
}
