package undecided.erp.relationship.domain.model.partyRole.personRole.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.common.exception.BusinessException;
import undecided.erp.common.snowflake.SnowflakeIdProvider;

@DisplayName("Employeeクラスのテスト")
class EmployeeTest {

  @Nested
  @DisplayName("newInstanceメソッドのテスト")
  class NewInstanceTest {

    @Test
    @DisplayName("正常な名前とカナ名を渡した場合にEmployeeインスタンスが生成されるべき")
    void shouldCreateEmployeeWithValidArguments() {
      // Mock SnowflakeIdProvider
      try (var mockedStatic = mockStatic(SnowflakeIdProvider.class)) {
        Long generatedId = 123456789L;
        mockedStatic.when(SnowflakeIdProvider::generateId).thenReturn(generatedId);

        String name = "John Doe";
        String kanaName = "ジョン";

        Employee employee = Employee.newInstance(name, kanaName);

        assertThat(employee).isNotNull();
        assertThat(employee.getEmployeeId()).isEqualTo(generatedId);
        assertThat(employee.getName()).isEqualTo(name);
        assertThat(employee.getKanaName()).isEqualTo(kanaName);
        assertThat(employee.getInitials()).isEqualTo(String.valueOf(kanaName.charAt(0)));
      }
    }

    @Test
    @DisplayName("nameが空文字の場合、BusinessExceptionをスローすべき")
    void shouldThrowExceptionWhenNameIsEmpty() {
      String emptyName = "";
      String kanaName = "ジョン";

      assertThatThrownBy(() -> Employee.newInstance(emptyName, kanaName))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("kanaNameが空文字の場合、BusinessExceptionをスローすべき")
    void shouldThrowExceptionWhenKanaNameIsEmpty() {
      String name = "John Doe";
      String emptyKanaName = "";

      assertThatThrownBy(() -> Employee.newInstance(name, emptyKanaName))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("nameがnullの場合、BusinessExceptionをスローすべき")
    void shouldThrowExceptionWhenNameIsNull() {
      String nullName = null;
      String kanaName = "ジョン";

      assertThatThrownBy(() -> Employee.newInstance(nullName, kanaName))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("kanaNameがnullの場合、BusinessExceptionをスローすべき")
    void shouldThrowExceptionWhenKanaNameIsNull() {
      String name = "John Doe";
      String nullKanaName = null;

      assertThatThrownBy(() -> Employee.newInstance(name, nullKanaName))
          .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("kanaNameが上限長を超えた場合、BusinessExceptionをスローすべき")
    void shouldThrowExceptionWhenKanaNameExceedsMaximumLength() {
      String name = "John Doe";
      String kanaName = "あ".repeat(301);

      assertThatThrownBy(() -> Employee.newInstance(name, kanaName))
          .isInstanceOf(BusinessException.class);
    }
  }

}
