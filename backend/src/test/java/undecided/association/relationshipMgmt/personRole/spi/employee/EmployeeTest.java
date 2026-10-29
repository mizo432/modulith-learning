package undecided.association.relationshipMgmt.personRole.spi.employee;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Employeeエンティティの単体テスト")
class EmployeeTest {

  @Nested
  @DisplayName("プロパティのゲッターとセッターのテスト")
  class PropertyAccessTest {

    @Test
    @DisplayName("各プロパティの設定と取得が正常に動作すること")
    void shouldGetAndSetAllProperties() {
      // Arrange
      Employee employee = new Employee();
      UUID employeeId = UUID.randomUUID();
      String lastName = "Tanaka";
      String firstName = "Yuki";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      LocalDateTime validTo = LocalDateTime.of(2025, 12, 31, 23, 59);

      // Act
      employee.setEmployeeId(employeeId);
      employee.setLastName(lastName);
      employee.setFirstName(firstName);
      employee.setValidFrom(validFrom);
      employee.setValidTo(validTo);

      // Assert
      assertThat(employee.getEmployeeId()).isEqualTo(employeeId);
      assertThat(employee.getLastName()).isEqualTo(lastName);
      assertThat(employee.getFirstName()).isEqualTo(firstName);
      assertThat(employee.getValidFrom()).isEqualTo(validFrom);
      assertThat(employee.getValidTo()).isEqualTo(validTo);
    }

    @Test
    @DisplayName("初期状態では全プロパティがnullであること")
    void shouldHaveNullPropertiesByDefault() {
      // Arrange
      Employee employee = new Employee();

      // Assert
      assertThat(employee.getEmployeeId()).isNull();
      assertThat(employee.getLastName()).isNull();
      assertThat(employee.getFirstName()).isNull();
      assertThat(employee.getValidFrom()).isNull();
      assertThat(employee.getValidTo()).isNull();
    }

    @Test
    @DisplayName("validToがnullでも正常に動作すること")
    void shouldHandleNullValidTo() {
      // Arrange
      Employee employee = new Employee();
      employee.setEmployeeId(UUID.randomUUID());
      employee.setLastName("Sato");
      employee.setFirstName("Ken");
      employee.setValidFrom(LocalDateTime.of(2024, 1, 1, 0, 0));

      // Assert
      assertThat(employee.getValidTo()).isNull();
      assertThat(employee.getEmployeeId()).isNotNull();
      assertThat(employee.getLastName()).isEqualTo("Sato");
      assertThat(employee.getFirstName()).isEqualTo("Ken");
    }

    @Test
    @DisplayName("氏名に日本語文字列を設定できること")
    void shouldHandleJapaneseNames() {
      // Arrange
      Employee employee = new Employee();

      // Act
      employee.setLastName("田中");
      employee.setFirstName("由紀");

      // Assert
      assertThat(employee.getLastName()).isEqualTo("田中");
      assertThat(employee.getFirstName()).isEqualTo("由紀");
    }

    @Test
    @DisplayName("氏名に長い文字列を設定できること")
    void shouldHandleLongNames() {
      // Arrange
      Employee employee = new Employee();
      String longName = "A".repeat(100);

      // Act
      employee.setLastName(longName);
      employee.setFirstName(longName);

      // Assert
      assertThat(employee.getLastName()).isEqualTo(longName);
      assertThat(employee.getFirstName()).isEqualTo(longName);
    }
  }

  @Nested
  @DisplayName("エンティティアノテーションのテスト")
  class EntityAnnotationTest {

    @Test
    @DisplayName("Employeeクラスに@Entityアノテーションが付与されていること")
    void shouldHaveEntityAnnotation() {
      // Assert
      assertThat(Employee.class.isAnnotationPresent(jakarta.persistence.Entity.class)).isTrue();
    }

    @Test
    @DisplayName("テーブル名がemployeesであること")
    void shouldHaveCorrectTableName() {
      // Assert
      jakarta.persistence.Table table = Employee.class.getAnnotation(jakarta.persistence.Table.class);
      assertThat(table).isNotNull();
      assertThat(table.name()).isEqualTo("employees");
    }

    @Test
    @DisplayName("employeeIdに@Idアノテーションが付与されていること")
    void shouldHaveIdAnnotationOnEmployeeId() throws NoSuchFieldException {
      // Arrange
      jakarta.persistence.Id idAnnotation =
          Employee.class.getDeclaredField("employeeId").getAnnotation(jakarta.persistence.Id.class);

      // Assert
      assertThat(idAnnotation).isNotNull();
    }

    @Test
    @DisplayName("lastNameに@Convertアノテーションが付与されていること")
    void shouldHaveConvertAnnotationOnLastName() throws NoSuchFieldException {
      // Arrange
      jakarta.persistence.Convert convertAnnotation =
          Employee.class.getDeclaredField("lastName").getAnnotation(jakarta.persistence.Convert.class);

      // Assert
      assertThat(convertAnnotation).isNotNull();
      assertThat(convertAnnotation.converter())
          .isEqualTo(com.github.mizo432.shared.converter.EncryptedStringConverter.class);
    }

    @Test
    @DisplayName("firstNameに@Convertアノテーションが付与されていること")
    void shouldHaveConvertAnnotationOnFirstName() throws NoSuchFieldException {
      // Arrange
      jakarta.persistence.Convert convertAnnotation =
          Employee.class.getDeclaredField("firstName").getAnnotation(jakarta.persistence.Convert.class);

      // Assert
      assertThat(convertAnnotation).isNotNull();
      assertThat(convertAnnotation.converter())
          .isEqualTo(com.github.mizo432.shared.converter.EncryptedStringConverter.class);
    }

    @Test
    @DisplayName("lastNameに@Column(nullable=false)アノテーションが付与されていること")
    void shouldHaveColumnAnnotationOnLastName() throws NoSuchFieldException {
      // Arrange
      jakarta.persistence.Column columnAnnotation =
          Employee.class.getDeclaredField("lastName").getAnnotation(jakarta.persistence.Column.class);

      // Assert
      assertThat(columnAnnotation).isNotNull();
      assertThat(columnAnnotation.nullable()).isFalse();
    }

    @Test
    @DisplayName("firstNameに@Column(nullable=false)アノテーションが付与されていること")
    void shouldHaveColumnAnnotationOnFirstName() throws NoSuchFieldException {
      // Arrange
      jakarta.persistence.Column columnAnnotation =
          Employee.class.getDeclaredField("firstName").getAnnotation(jakarta.persistence.Column.class);

      // Assert
      assertThat(columnAnnotation).isNotNull();
      assertThat(columnAnnotation.nullable()).isFalse();
    }

    @Test
    @DisplayName("validFromに@Column(nullable=false)アノテーションが付与されていること")
    void shouldHaveColumnAnnotationOnValidFrom() throws NoSuchFieldException {
      // Arrange
      jakarta.persistence.Column columnAnnotation =
          Employee.class.getDeclaredField("validFrom").getAnnotation(jakarta.persistence.Column.class);

      // Assert
      assertThat(columnAnnotation).isNotNull();
      assertThat(columnAnnotation.nullable()).isFalse();
    }
  }
}
