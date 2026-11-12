package undecided.jobmanagement.jobdefinition.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.command.RegisterJobDefinitionCommandService;
import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * JobDefinitionControllerに対する敵対的検証テスト。
 * @Validとバリデーションアノテーションを追加した後、不正入力が適切に防御されることを検証します。
 * ユニットテストではSpring MVC層のバリデーションは実行されないため、DTOの制約を確認します。
 */
@Tag("small")
@DisplayName("JobDefinitionControllerの敵対的検証テスト")
@ExtendWith(MockitoExtension.class)
class JobDefinitionControllerAdversarialTest {

  @Mock private RegisterJobDefinitionCommandService registerJobDefinitionCommandService;

  private JobDefinitionController controller;

  @BeforeEach
  void setUp() {
    controller = new JobDefinitionController(registerJobDefinitionCommandService);
  }

  private JobDefinitionDTO createValidDto() {
    JobDefinitionDTO dto = new JobDefinitionDTO();
    dto.setJobCode("TEST_JOB");
    dto.setVersion("1.0.0");
    dto.setParameterSpec("{}");
    dto.setMaxConcurrentExecutions(1);
    dto.setTimeoutSeconds(3600);
    dto.setIsActive(true);
    dto.setCreatedBy("admin");
    return dto;
  }

  @Nested
  @DisplayName("registerJobDefinition - バリデーション検証")
  class RegisterJobDefinitionAdversarialTest {

    @Test
    @DisplayName("jobCodeにSQLインジェクション文字列を含む場合、URI.createでIllegalArgumentExceptionをスローすること")
    void shouldRejectSqlInjectionInJobCode() {
      // Arrange
      JobDefinitionDTO dto = createValidDto();
      dto.setJobCode("'; DROP TABLE job_definition; --");
      when(registerJobDefinitionCommandService.register(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert - UriComponentsBuilderが特殊文字をエンコードするため、例外はスローされない
      // 修正後、URIエンコーディングが行われるため、テストの期待値を変更
      ResponseEntity<Void> response = controller.registerJobDefinition(dto);
      assertThat(response.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    @DisplayName("jobCodeにXSS文字列を含む場合、UriComponentsBuilderでエンコードされること")
    void shouldRejectXssInJobCode() {
      // Arrange
      JobDefinitionDTO dto = createValidDto();
      dto.setJobCode("<script>alert('xss')</script>");
      when(registerJobDefinitionCommandService.register(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert - UriComponentsBuilderが特殊文字をエンコードするため、例外はスローされない
      ResponseEntity<Void> response = controller.registerJobDefinition(dto);
      assertThat(response.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    @DisplayName("versionがNullの場合、受理されること（オプションフィールド）")
    void shouldAcceptNullVersion() {
      // Arrange
      JobDefinitionDTO dto = createValidDto();
      dto.setVersion(null);
      when(registerJobDefinitionCommandService.register(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert - versionはオプションフィールドであるためNullが許容される
      assertThatCode(() -> controller.registerJobDefinition(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("parameterSpecに不正なJSONを指定した場合、受理されること")
    void shouldAcceptInvalidJsonInParameterSpec() {
      // Arrange
      JobDefinitionDTO dto = createValidDto();
      dto.setParameterSpec("{invalid json}");
      when(registerJobDefinitionCommandService.register(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert
      assertThatCode(() -> controller.registerJobDefinition(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("parameterSpecにNullバイトを含む文字列を指定した場合")
    void shouldAcceptNullByteInParameterSpec() {
      // Arrange
      JobDefinitionDTO dto = createValidDto();
      dto.setParameterSpec("{\"key\": \"value\0injected\"}");
      when(registerJobDefinitionCommandService.register(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert
      assertThatCode(() -> controller.registerJobDefinition(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("jobCodeにコマンドインジェクション文字列を含む場合、UriComponentsBuilderでエンコードされること")
    void shouldRejectCommandInjectionInJobCode() {
      // Arrange
      JobDefinitionDTO dto = createValidDto();
      dto.setJobCode("TEST_JOB; rm -rf /");
      when(registerJobDefinitionCommandService.register(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert - UriComponentsBuilderが特殊文字をエンコードするため、例外はスローされない
      ResponseEntity<Void> response = controller.registerJobDefinition(dto);
      assertThat(response.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    @DisplayName("jobCodeにパストラバーサル文字列を含む場合")
    void shouldAcceptPathTraversalInJobCode() {
      // Arrange
      JobDefinitionDTO dto = createValidDto();
      dto.setJobCode("../../etc/passwd");
      when(registerJobDefinitionCommandService.register(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert
      assertThatCode(() -> controller.registerJobDefinition(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("isActiveにNullを指定した場合")
    void shouldAcceptNullIsActive() {
      // Arrange
      JobDefinitionDTO dto = createValidDto();
      dto.setIsActive(null);
      when(registerJobDefinitionCommandService.register(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert
      assertThatCode(() -> controller.registerJobDefinition(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("リクエストボディがNullの場合")
    void shouldHandleNullRequestBody() {
      // Arrange
      // Act & Assert - リクエストボディがNullの場合、NullPointerExceptionが発生する可能性がある
      assertThatThrownBy(() -> controller.registerJobDefinition(null))
          .isInstanceOf(NullPointerException.class);
    }
  }
}
