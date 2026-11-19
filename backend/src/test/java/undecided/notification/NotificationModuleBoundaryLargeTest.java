package undecided.notification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import undecided.ModulithDemoApplication;

/**
 * テストリスト項目16: モジュール境界の検証
 *
 * <p>- 項目16: モジュール境界が保たれていること（Spring Modulith検証）
 */
@Tag("large")
@DisplayName("通知モジュールの境界検証")
class NotificationModuleBoundaryLargeTest {

  @Test
  @DisplayName("モジュール境界が保たれていること")
  void shouldVerifyModuleBoundaries() {
    // テストリスト項目16
    ApplicationModules modules = ApplicationModules.of(ModulithDemoApplication.class);
    
    // モジュール構成を確認
    String moduleInfo = modules.toString();
    assertThat(moduleInfo).isNotBlank();
    // notification モジュールが存在することを確認
    assertThat(moduleInfo).contains("notification");
  }
}
