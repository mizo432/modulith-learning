package undecided.jobmanagement.permissionmanagement.spi.userpermission;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Roleのテスト")
class RoleTest {

  @Test
  @DisplayName("全列挙値が取得できる")
  void shouldGetAllValues() {
    Role[] roles = Role.values();

    assertThat(roles).hasSize(2);
    assertThat(roles).containsExactly(Role.ADMIN, Role.USER);
  }

  @Test
  @DisplayName("名前から列挙値を取得できる")
  void shouldGetValueByName() {
    Role role = Role.valueOf("ADMIN");

    assertThat(role).isEqualTo(Role.ADMIN);
  }

  @Test
  @DisplayName("無効な名前を指定するとIllegalArgumentExceptionがスローされる")
  void shouldThrowExceptionWhenInvalidName() {
    org.junit.jupiter.api.Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> Role.valueOf("INVALID")
    );
  }
}
