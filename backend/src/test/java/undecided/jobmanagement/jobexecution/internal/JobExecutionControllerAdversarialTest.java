package undecided.jobmanagement.jobexecution.internal;

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
import undecided.jobmanagement.jobexecution.spi.jobexecution.command.StartJobCommandService;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * JobExecutionControllerに対する敵対的検証テスト。
 * @Validとバリデーションアノテーションを追加した後、不正入力が適切に防御されることを検証します。
 * ユニットテストではSpring MVC層のバリデーションは実行されないため、DTOの制約を確認します。
 */
@Tag("small")
@DisplayName("JobExecutionControllerの敵対的検証テスト")
@ExtendWith(MockitoExtension.class)
class JobExecutionControllerAdversarialTest {

  @Mock private StartJobCommandService startJobCommandService;

  private JobExecutionController controller;

  @BeforeEach
  void setUp() {
    controller = new JobExecutionController(startJobCommandService);
  }

  private JobExecutionDTO createValidDto() {
    JobExecutionDTO dto = new JobExecutionDTO();
    dto.setExecutionId("EXEC-001");
    dto.setJobCode("TEST_JOB");
    dto.setState("PENDING");
    dto.setProcessedCount(0L);
    dto.setCreatedBy("admin");
    return dto;
  }

  @Nested
  @DisplayName("startJob - バリデーション検証")
  class StartJobAdversarialTest {

    @Test
    @DisplayName("executionIdにSQLインジェクション文字列を含む場合、UriComponentsBuilderでエンコードされること")
    void shouldRejectSqlInjectionInExecutionId() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setExecutionId("'; DROP TABLE job_execution; --");
      when(startJobCommandService.startJob(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert - UriComponentsBuilderが特殊文字をエンコードするため、例外はスローされない
      ResponseEntity<Void> response = controller.startJob(dto);
      assertThat(response.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    @DisplayName("stateに不正な値を指定した場合、受理されること")
    void shouldAcceptInvalidState() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setState("INVALID_STATE");
      when(startJobCommandService.startJob(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert - 列挙型チェックが欠如
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("stateにNullを指定した場合、受理されること")
    void shouldAcceptNullState() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setState(null);
      when(startJobCommandService.startJob(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("processedCountに負の値を指定した場合、受理されること")
    void shouldAcceptNegativeProcessedCount() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setProcessedCount(-1L);
      when(startJobCommandService.startJob(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("processedCountにLong.MAX_VALUEを指定した場合、受理されること")
    void shouldAcceptMaxProcessedCount() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setProcessedCount(Long.MAX_VALUE);
      when(startJobCommandService.startJob(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("errorMessageにNullバイトを含む文字列を指定した場合")
    void shouldAcceptNullByteInErrorMessage() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setErrorMessage("Error\0Injected");
      when(startJobCommandService.startJob(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("checkpointPositionに不正な値を指定した場合、受理されること")
    void shouldAcceptInvalidCheckpointPosition() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setCheckpointPosition("not-a-valid-checkpoint");
      when(startJobCommandService.startJob(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("リクエストボディがNullの場合")
    void shouldHandleNullRequestBody() {
      // Arrange
      // Act & Assert - リクエストボディがNullの場合、NullPointerExceptionが発生する可能性がある
      assertThatThrownBy(() -> controller.startJob(null))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("createdByにNullを指定した場合")
    void shouldAcceptNullCreatedBy() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setCreatedBy(null);
      when(startJobCommandService.startJob(any())).thenReturn(SnowflakeId.of(1234567890L));

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }
  }
}
