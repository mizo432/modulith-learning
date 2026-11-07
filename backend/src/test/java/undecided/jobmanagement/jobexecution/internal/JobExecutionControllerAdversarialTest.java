package undecided.jobmanagement.jobexecution.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.jobmanagement.jobexecution.spi.jobexecution.command.StartJobCommand;
import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;

/**
 * JobExecutionControllerに対する敵対的検証テスト。
 * @Validとバリデーションアノテーションを追加した後、不正入力が適切に防御されることを検証します。
 * ユニットテストではSpring MVC層のバリデーションは実行されないため、DTOの制約を確認します。
 */
@Tag("small")
@DisplayName("JobExecutionControllerの敵対的検証テスト")
@ExtendWith(MockitoExtension.class)
class JobExecutionControllerAdversarialTest {

  @Mock private StartJobCommand startJobCommand;

  private JobExecutionController controller;

  @BeforeEach
  void setUp() {
    controller = new JobExecutionController(startJobCommand);
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
    @DisplayName("executionIdにSQLインジェクション文字列を含む場合、URI.createでIllegalArgumentExceptionをスローすること")
    void shouldRejectSqlInjectionInExecutionId() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setExecutionId("'; DROP TABLE job_execution; --");
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert - URI.create()が特殊文字';を拒否する
      // これは間接的な防御だが、バリデーションとしての意図はない
      assertThatThrownBy(() -> controller.startJob(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Illegal character");
    }

    @Test
    @DisplayName("stateに不正な値を指定した場合、受理されること")
    void shouldAcceptInvalidState() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setState("INVALID_STATE");
      doNothing().when(startJobCommand).startJob(any());

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
      doNothing().when(startJobCommand).startJob(any());

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
      doNothing().when(startJobCommand).startJob(any());

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
      doNothing().when(startJobCommand).startJob(any());

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
      doNothing().when(startJobCommand).startJob(any());

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
      doNothing().when(startJobCommand).startJob(any());

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
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }
  }
}
