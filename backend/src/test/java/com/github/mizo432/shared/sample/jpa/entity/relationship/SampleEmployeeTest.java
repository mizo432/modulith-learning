package com.github.mizo432.shared.sample.jpa.entity.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("SampleEmployeeの単体テスト")
class SampleEmployeeTest {

  @Nested
  @DisplayName("コンストラクタのテスト")
  class ConstructorTest {

    @Test
    @DisplayName("正常な引数で従業員エンティティが作成される")
    void shouldCreateEmployeeWithValidArguments() {
      // Arrange
      SnowflakeId employeeId = SnowflakeId.of(1L);
      String employeeName = "田中太郎";
      String employeeNo = "EMP001";

      // Act
      SampleEmployee employee = new SampleEmployee(employeeId, employeeName, employeeNo);

      // Assert
      assertThat(employee).isNotNull();
      assertThat(employee.getEmployeeId()).isEqualTo(employeeId);
      assertThat(employee.getEmployeeName()).isEqualTo(employeeName);
      assertThat(employee.getEmployeeNo()).isEqualTo(employeeNo);
    }
  }

  @Nested
  @DisplayName("getter/setterのテスト")
  class GetterSetterTest {

    @Test
    @DisplayName("従業員名が設定・取得できる")
    void shouldSetAndGetEmployeeName() {
      // Arrange
      SampleEmployee employee = new SampleEmployee(SnowflakeId.of(1L), "初期名", "EMP001");

      // Act
      employee.setEmployeeName("変更後の名前");

      // Assert
      assertThat(employee.getEmployeeName()).isEqualTo("変更後の名前");
    }

    @Test
    @DisplayName("従業員番号が設定・取得できる")
    void shouldSetAndGetEmployeeNo() {
      // Arrange
      SampleEmployee employee = new SampleEmployee(SnowflakeId.of(1L), "田中", "EMP001");

      // Act
      employee.setEmployeeNo("EMP002");

      // Assert
      assertThat(employee.getEmployeeNo()).isEqualTo("EMP002");
    }
  }
}
