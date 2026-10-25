package undecided.shared.sample.jpa.entity.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleEmployeeProfileの単体テスト")
class SampleEmployeeProfileTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数でプロフィールエンティティが作成される")
    void shouldCreateProfileWithValidArguments() {
      // Arrange
      SnowflakeId profileId = SnowflakeId.of(1L);
      SampleEmployee employee = new SampleEmployee(SnowflakeId.of(2L), "田中太郎", "EMP001");
      String address = "東京都";
      String phoneNumber = "090-1234-5678";
      String email = "taro@example.com";

      // Act
      SampleEmployeeProfile profile =
          new SampleEmployeeProfile(profileId, employee, address, phoneNumber, email);

      // Assert
      assertThat(profile).isNotNull();
      assertThat(profile.getProfileId()).isEqualTo(profileId);
      assertThat(profile.getEmployee()).isEqualTo(employee);
      assertThat(profile.getAddress()).isEqualTo(address);
      assertThat(profile.getPhoneNumber()).isEqualTo(phoneNumber);
      assertThat(profile.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("オプションのフィールドがnullでも作成される")
    void shouldCreateProfileWithNullOptionalFields() {
      // Arrange
      SnowflakeId profileId = SnowflakeId.of(1L);
      SampleEmployee employee = new SampleEmployee(SnowflakeId.of(2L), "田中太郎", "EMP001");

      // Act
      SampleEmployeeProfile profile =
          new SampleEmployeeProfile(profileId, employee, null, null, null);

      // Assert
      assertThat(profile.getAddress()).isNull();
      assertThat(profile.getPhoneNumber()).isNull();
      assertThat(profile.getEmail()).isNull();
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("住所が設定・取得できる")
    void shouldSetAndGetAddress() {
      // Arrange
      SampleEmployeeProfile profile =
          new SampleEmployeeProfile(
              SnowflakeId.of(1L),
              new SampleEmployee(SnowflakeId.of(2L), "田中", "EMP001"),
              "初期住所",
              null,
              null);

      // Act
      profile.setAddress("変更後の住所");

      // Assert
      assertThat(profile.getAddress()).isEqualTo("変更後の住所");
    }

    @Test
    @DisplayName("電話番号が設定・取得できる")
    void shouldSetAndGetPhoneNumber() {
      // Arrange
      SampleEmployeeProfile profile =
          new SampleEmployeeProfile(
              SnowflakeId.of(1L),
              new SampleEmployee(SnowflakeId.of(2L), "田中", "EMP001"),
              null,
              "090-0000-0000",
              null);

      // Act
      profile.setPhoneNumber("090-1234-5678");

      // Assert
      assertThat(profile.getPhoneNumber()).isEqualTo("090-1234-5678");
    }

    @Test
    @DisplayName("メールアドレスが設定・取得できる")
    void shouldSetAndGetEmail() {
      // Arrange
      SampleEmployeeProfile profile =
          new SampleEmployeeProfile(
              SnowflakeId.of(1L),
              new SampleEmployee(SnowflakeId.of(2L), "田中", "EMP001"),
              null,
              null,
              "old@example.com");

      // Act
      profile.setEmail("new@example.com");

      // Assert
      assertThat(profile.getEmail()).isEqualTo("new@example.com");
    }
  }

  @Nested
  @DisplayName("OneToOne関連付けのテスト")
  class OneToOneRelationshipTest {

    @Test
    @DisplayName("プロフィールは従業員に関連付けられる")
    void shouldProfileBeLinkedToEmployee() {
      // Arrange
      SampleEmployee employee = new SampleEmployee(SnowflakeId.of(1L), "田中太郎", "EMP001");

      // Act
      SampleEmployeeProfile profile =
          new SampleEmployeeProfile(
              SnowflakeId.of(2L), employee, "東京都", "090-1234-5678", "taro@example.com");

      // Assert
      assertThat(profile.getEmployee()).isEqualTo(employee);
      assertThat(profile.getEmployee().getEmployeeName()).isEqualTo("田中太郎");
    }

    @Test
    @DisplayName("employeeが設定・取得できる")
    void shouldSetAndGetEmployee() {
      // Arrange
      SampleEmployeeProfile profile =
          new SampleEmployeeProfile(
              SnowflakeId.of(1L),
              new SampleEmployee(SnowflakeId.of(2L), "田中", "EMP001"),
              null,
              null,
              null);

      // Act
      SampleEmployee newEmployee = new SampleEmployee(SnowflakeId.of(3L), "鈴木", "EMP002");
      profile.setEmployee(newEmployee);

      // Assert
      assertThat(profile.getEmployee()).isEqualTo(newEmployee);
    }
  }
}
