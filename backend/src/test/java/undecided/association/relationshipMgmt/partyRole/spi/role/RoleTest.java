package undecided.association.relationshipMgmt.partyRole.spi.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.mizo432.shared.common.exception.BusinessException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("Roleエンティティの単体テスト")
class RoleTest {

  private static final SnowflakeId ROLE_ID = SnowflakeId.of(1L);
  private static final LocalDateTime VALID_FROM = LocalDateTime.of(2024, 1, 1, 0, 0);
  private static final LocalDateTime VALID_TO = LocalDateTime.of(2025, 12, 31, 23, 59);

  private Role createRole() {
    Role role = new Role();
    role.setRoleId(ROLE_ID);
    role.setRoleCode("ADMIN");
    role.setRoleName("Administrator");
    role.setRoleTypeCode("SYSTEM");
    role.setDescription("System administrator role");
    role.setValidFrom(VALID_FROM);
    role.setValidTo(VALID_TO);
    return role;
  }

  @Nested
  @DisplayName("プロパティのゲッターのテスト")
  class PropertyAccessTest {

    @Test
    @DisplayName("各プロパティの設定と取得が正常に動作すること")
    void shouldGetAndSetAllProperties() {
      // Arrange
      Role role = createRole();

      // Assert
      assertThat(role.getRoleId()).isEqualTo(ROLE_ID);
      assertThat(role.getRoleCode()).isEqualTo("ADMIN");
      assertThat(role.getRoleName()).isEqualTo("Administrator");
      assertThat(role.getRoleTypeCode()).isEqualTo("SYSTEM");
      assertThat(role.getDescription()).isEqualTo("System administrator role");
      assertThat(role.getValidFrom()).isEqualTo(VALID_FROM);
      assertThat(role.getValidTo()).isEqualTo(VALID_TO);
    }

    @Test
    @DisplayName("初期状態では全プロパティがnullであること")
    void shouldHaveNullPropertiesByDefault() {
      // Arrange
      Role role = new Role();

      // Assert
      assertThat(role.getRoleId()).isNull();
      assertThat(role.getRoleCode()).isNull();
      assertThat(role.getRoleName()).isNull();
      assertThat(role.getRoleTypeCode()).isNull();
      assertThat(role.getDescription()).isNull();
      assertThat(role.getValidFrom()).isNull();
      assertThat(role.getValidTo()).isNull();
    }
  }

  @Nested
  @DisplayName("updateメソッドのテスト")
  class UpdateMethodTest {

    @Test
    @DisplayName("ロール情報が更新される")
    void shouldUpdateAllProperties() {
      // Arrange
      Role role = createRole();

      // Act
      role.update("USER", "User", "BASIC", "Basic user role");

      // Assert
      assertThat(role.getRoleCode()).isEqualTo("USER");
      assertThat(role.getRoleName()).isEqualTo("User");
      assertThat(role.getRoleTypeCode()).isEqualTo("BASIC");
      assertThat(role.getDescription()).isEqualTo("Basic user role");
    }

    @Test
    @DisplayName("descriptionはnullで更新できる")
    void shouldUpdateWithNullDescription() {
      // Arrange
      Role role = createRole();

      // Act
      role.update("USER", "User", "BASIC", null);

      // Assert
      assertThat(role.getRoleCode()).isEqualTo("USER");
      assertThat(role.getRoleName()).isEqualTo("User");
      assertThat(role.getRoleTypeCode()).isEqualTo("BASIC");
      assertThat(role.getDescription()).isNull();
    }

    @Test
    @DisplayName("roleCodeがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenRoleCodeIsNull() {
      // Arrange
      Role role = createRole();

      // Act & Assert
      assertThatThrownBy(() -> role.update(null, "User", "BASIC", "Description"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("roleCode must not be null");
    }

    @Test
    @DisplayName("roleNameがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenRoleNameIsNull() {
      // Arrange
      Role role = createRole();

      // Act & Assert
      assertThatThrownBy(() -> role.update("USER", null, "BASIC", "Description"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("roleName must not be null");
    }

    @Test
    @DisplayName("roleTypeCodeがnullの場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenRoleTypeCodeIsNull() {
      // Arrange
      Role role = createRole();

      // Act & Assert
      assertThatThrownBy(() -> role.update("USER", "User", null, "Description"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("roleTypeCode must not be null");
    }

    @Test
    @DisplayName("roleCodeが空文字の場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenRoleCodeIsEmpty() {
      // Arrange
      Role role = createRole();

      // Act & Assert
      assertThatThrownBy(() -> role.update("   ", "User", "BASIC", "Description"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("roleCode must not be empty");
    }

    @Test
    @DisplayName("roleCodeが30文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenRoleCodeExceedsLength() {
      // Arrange
      Role role = createRole();
      String longCode = "A".repeat(31);

      // Act & Assert
      assertThatThrownBy(() -> role.update(longCode, "User", "BASIC", "Description"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("roleCode must not exceed 30 characters");
    }

    @Test
    @DisplayName("roleNameが50文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenRoleNameExceedsLength() {
      // Arrange
      Role role = createRole();
      String longName = "A".repeat(51);

      // Act & Assert
      assertThatThrownBy(() -> role.update("USER", longName, "BASIC", "Description"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("roleName must not exceed 50 characters");
    }

    @Test
    @DisplayName("roleTypeCodeが20文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenRoleTypeCodeExceedsLength() {
      // Arrange
      Role role = createRole();
      String longTypeCode = "A".repeat(21);

      // Act & Assert
      assertThatThrownBy(() -> role.update("USER", "User", longTypeCode, "Description"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("roleTypeCode must not exceed 20 characters");
    }

    @Test
    @DisplayName("descriptionが200文字を超える場合、BusinessExceptionがスローされる")
    void shouldThrowExceptionWhenDescriptionExceedsLength() {
      // Arrange
      Role role = createRole();
      String longDescription = "A".repeat(201);

      // Act & Assert
      assertThatThrownBy(() -> role.update("USER", "User", "BASIC", longDescription))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("description must not exceed 200 characters");
    }
  }

  @Nested
  @DisplayName("エンティティアノテーションのテスト")
  class EntityAnnotationTest {

    @Test
    @DisplayName("Roleクラスに@Entityアノテーションが付与されていること")
    void shouldHaveEntityAnnotation() {
      // Assert
      assertThat(Role.class.isAnnotationPresent(jakarta.persistence.Entity.class)).isTrue();
    }

    @Test
    @DisplayName("テーブル名がparty_roleであること")
    void shouldHaveCorrectTableName() {
      // Assert
      jakarta.persistence.Table table = Role.class.getAnnotation(jakarta.persistence.Table.class);
      assertThat(table).isNotNull();
      assertThat(table.name()).isEqualTo("party_role");
    }

    @Test
    @DisplayName("roleIdに@Idアノテーションが付与されていること")
    void shouldHaveIdAnnotationOnRoleId() throws NoSuchFieldException {
      // Arrange
      jakarta.persistence.Id idAnnotation =
          Role.class.getDeclaredField("roleId").getAnnotation(jakarta.persistence.Id.class);

      // Assert
      assertThat(idAnnotation).isNotNull();
    }
  }
}
