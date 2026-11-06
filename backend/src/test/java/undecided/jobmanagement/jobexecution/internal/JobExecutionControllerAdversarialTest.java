package undecided.jobmanagement.jobexecution.internal;

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
 * バリデーションアノテーションが欠如しているため、不正入力の受け付けを検証します。
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
  @DisplayName("startJob - バリデーション欠如検証")
  class StartJobAdversarialTest {

    @Test
    @DisplayName("executionIdがNullの場合、受理されること（脆弱性）")
    void shouldAcceptNullExecutionId() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setExecutionId(null);
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert - @Validがないため、Nullチェックが行われない
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("executionIdが空文字列の場合、受理されること（脆弱性）")
    void shouldAcceptEmptyExecutionId() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setExecutionId("");
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("jobCodeがNullの場合、受理されること（脆弱性）")
    void shouldAcceptNullJobCode() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setJobCode(null);
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("jobCodeが空文字列の場合、受理されること（脆弱性）")
    void shouldAcceptEmptyJobCode() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setJobCode("");
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("jobCodeにSQLインジェクション文字列を含む場合、受理されること")
    void shouldAcceptSqlInjectionInJobCode() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setJobCode("'; DROP TABLE job_execution; --");
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("jobCodeにXSS文字列を含む場合、受理されること")
    void shouldAcceptXssInJobCode() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setJobCode("<script>alert('xss')</script>");
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

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
    @DisplayName("errorMessageに非常に長い文字列を指定した場合")
    void shouldAcceptVeryLongErrorMessage() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setErrorMessage("A".repeat(100000));
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert - 文字列長の上限チェックが欠如
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
    @DisplayName("全フィールドがNullの場合、受理されること（脆弱性）")
    void shouldAcceptAllNullFields() {
      // Arrange
      JobExecutionDTO dto = new JobExecutionDTO();
      // 全フィールドNullのまま
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert - @Validがないため、Nullチェックが行われない
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("jobCodeにコマンドインジェクション文字列を含む場合")
    void shouldAcceptCommandInjectionInJobCode() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setJobCode("TEST_JOB; rm -rf /");
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("jobCodeにパストラバーサル文字列を含む場合")
    void shouldAcceptPathTraversalInJobCode() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setJobCode("../../etc/passwd");
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
    @DisplayName("executionIdに非常に長い文字列を指定した場合")
    void shouldAcceptVeryLongExecutionId() {
      // Arrange
      JobExecutionDTO dto = createValidDto();
      dto.setExecutionId("A".repeat(10000));
      doNothing().when(startJobCommand).startJob(any());

      // Act & Assert - 文字列長の上限チェックが欠如
      assertThatCode(() -> controller.startJob(dto))
          .doesNotThrowAnyException();
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
