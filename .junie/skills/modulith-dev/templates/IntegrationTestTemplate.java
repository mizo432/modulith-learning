package undecided.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;

/**
 * {@code SampleIntegrationTest} は、統合テスト（Medium）のテンプレートクラスです。
 * <p>
 * 規約要件:
 * 1. @Tag("medium") を付与
 * 2. @ActiveProfiles("test") および @Import(TestcontainersConfiguration.class) で PostgreSQL コンテナを利用
 * 3. クラス・メソッドともに日本語 @DisplayName を付与
 * 4. 対象メソッドごとに @Nested クラスでグループ化
 * 5. メソッド名は should で開始し、アンダースコアを使用しない
 * 6. package-private 可視性
 */
@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("Sampleモジュールの統合テスト")
class SampleIntegrationTest {

  @Autowired
  private SampleService sampleService;

  @Nested
  @DisplayName("process メソッドの統合テスト")
  class ProcessTest {

    @Test
    @DisplayName("DB連携および依存モジュール連携が正常に動作すること")
    void shouldProcessCorrectlyWithDatabaseIntegration() {
      // Arrange
      SampleCommand command = new SampleCommand("sample-id", "test-payload");

      // Act
      SampleResult result = sampleService.process(command);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result.isSuccess()).isTrue();
    }
  }
}
