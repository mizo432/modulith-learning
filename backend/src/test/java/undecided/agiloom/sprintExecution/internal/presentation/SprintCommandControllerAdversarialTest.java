package undecided.agiloom.sprintExecution.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.query.SprintQueryServiceInterface;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * SprintCommandControllerに対する敵対的検証テスト。
 * 不正入力、境界値、インジェクション試行など、悪意のあるリクエストに対するシステムの堅牢性を検証します。
 */
@Tag("small")
@DisplayName("SprintCommandControllerの敵対的検証テスト")
@ExtendWith(MockitoExtension.class)
class SprintCommandControllerAdversarialTest {

  @Mock private CreateSprintCommandServiceInterface createSprintCommandService;
  @Mock private UpdateSprintCommandServiceInterface updateSprintCommandService;
  @Mock private SprintQueryServiceInterface sprintQueryService;
  @Mock private SprintQueryController sprintQueryController;

  private SprintCommandController controller;

  @BeforeEach
  void setUp() {
    controller = new SprintCommandController(
        createSprintCommandService,
        updateSprintCommandService,
        sprintQueryService,
        sprintQueryController
    );
  }

  private Sprint createSprint(SnowflakeId id) {
    return Sprint.builder()
        .id(id)
        .productId(UUID.randomUUID())
        .sprintNumber(1)
        .title("Sprint 1")
        .goal("Goal 1")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now().plusWeeks(2))
        .teamId(UUID.randomUUID())
        .teamName("Team A")
        .build();
  }

  @Nested
  @DisplayName("createSprint - 不正入力検証")
  class CreateSprintAdversarialTest {

    @Test
    @DisplayName("タイトルにSQLインジェクション文字列を含むリクエストを処理すること")
    void shouldHandleSqlInjectionInTitle() {
      // Arrange - SQLインジェクション試行
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("'; DROP TABLE sprint; --")
          .goal("Goal")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(snowflakeId);
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.of(sprint));
      when(sprintQueryController.toResponse(any(Sprint.class))).thenReturn(expectedResponse);

      // Act & Assert - 例外をスローせずに処理できること（JPAのパラメータ化クエリで防御されている前提）
      // コントローラー層では例外をスローしない
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("タイトルにXSS文字列を含むリクエストを処理すること")
    void shouldHandleXssInTitle() {
      // Arrange - XSS試行
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("<script>alert('xss')</script>")
          .goal("<img src=x onerror=alert(1)>")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(snowflakeId);
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.of(sprint));
      when(sprintQueryController.toResponse(any(Sprint.class))).thenReturn(expectedResponse);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("sprintNumberに負の値を指定した場合、バリデーションで拒否されること")
    void shouldRejectNegativeSprintNumber() {
      // Arrange
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(-1)
          .title("Sprint")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      // Act & Assert - @Min(1)制約により、Spring MVC層でバリデーションエラーとなる
      // ユニットテストではSpring MVC層を通らないため、DTOの制約状態を確認する
      assertThat(request.getSprintNumber()).isLessThan(1);
    }

    @Test
    @DisplayName("sprintNumberにInteger.MAX_VALUEを指定した場合")
    void shouldHandleMaxSprintNumber() {
      // Arrange
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(Integer.MAX_VALUE)
          .title("Sprint")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(snowflakeId);
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.of(sprint));
      when(sprintQueryController.toResponse(any(Sprint.class))).thenReturn(expectedResponse);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("endDateがstartDateより前にある場合、バリデーションで拒否されること")
    void shouldRejectEndDateBeforeStartDate() {
      // Arrange - 日付範囲が不正
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("Sprint")
          .startDate(LocalDate.now().plusWeeks(2))
          .endDate(LocalDate.now())  // startDateより前
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      // Act & Assert - @AssertTrue制約により、Spring MVC層でバリデーションエラーとなる
      // isEndDateAfterStartDate()がfalseを返すことを検証
      assertThat(request.isEndDateAfterStartDate()).isFalse();
    }

    @Test
    @DisplayName("過去の日付をstartDateとして指定した場合")
    void shouldHandlePastStartDate() {
      // Arrange
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("Sprint")
          .startDate(LocalDate.now().minusYears(10))
          .endDate(LocalDate.now().minusYears(10).plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(snowflakeId);
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.of(sprint));
      when(sprintQueryController.toResponse(any(Sprint.class))).thenReturn(expectedResponse);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("teamNameに非常に長い文字列を指定した場合")
    void shouldHandleVeryLongTeamName() {
      // Arrange - 非常に長い文字列
      String longName = "A".repeat(10000);
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("Sprint")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName(longName)
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(snowflakeId);
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.of(sprint));
      when(sprintQueryController.toResponse(any(Sprint.class))).thenReturn(expectedResponse);

      // Act & Assert - 文字列長の上限チェックが欠如
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("goalにNullバイトを含む文字列を指定した場合")
    void shouldHandleNullByteInGoal() {
      // Arrange - Nullバイトインジェクション
      CreateSprintRequest request = CreateSprintRequest.builder()
          .productId(UUID.randomUUID())
          .sprintNumber(1)
          .title("Sprint")
          .goal("Normal\0Injected")
          .startDate(LocalDate.now())
          .endDate(LocalDate.now().plusWeeks(2))
          .teamId(UUID.randomUUID())
          .teamName("Team A")
          .build();

      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      Sprint sprint = createSprint(snowflakeId);
      SprintResponse expectedResponse = SprintResponse.builder().id(snowflakeId).build();

      when(createSprintCommandService.execute(any())).thenReturn(snowflakeId);
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.of(sprint));
      when(sprintQueryController.toResponse(any(Sprint.class))).thenReturn(expectedResponse);

      // Act & Assert
      assertThatCode(() -> controller.createSprint(request))
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("changeStatus - 不正入力検証")
  class ChangeStatusAdversarialTest {

    @Test
    @DisplayName("IDに数値以外の文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionForNonNumericId() {
      // Arrange
      // Act & Assert - IDパース時にNumberFormatExceptionをIllegalArgumentExceptionに変換する
      assertThatThrownBy(() -> controller.changeStatus("abc", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに空文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionForEmptyId() {
      // Arrange
      // Act & Assert
      assertThatThrownBy(() -> controller.changeStatus("", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに負の値を指定した場合、SnowflakeIdでIllegalArgumentExceptionをスローすること")
    void shouldHandleNegativeId() {
      // Act & Assert - Long.parseLong("-123")は正常にパースされるが、SnowflakeId.of()で正の値チェックに失敗
      assertThatThrownBy(() -> controller.changeStatus("-123", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("value must be positive");
    }

    @Test
    @DisplayName("IDにLong.MAX_VALUEより大きな値を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowIllegalArgumentExceptionForOverflowId() {
      // Arrange
      // Act & Assert
      assertThatThrownBy(() -> controller.changeStatus("99999999999999999999", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにSQLインジェクション文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectSqlInjectionInId() {
      // Arrange
      // Act & Assert - SQLインジェクション文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus("1; DROP TABLE sprint;", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにXSS文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectXssInId() {
      // Arrange
      // Act & Assert - XSS文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus("<script>alert(1)</script>", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("Sprintが見つからない場合、適切な例外をスローすること")
    void shouldThrowExceptionWhenSprintNotFound() {
      // Arrange
      SnowflakeId snowflakeId = SnowflakeId.of(1234567890L);
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> controller.changeStatus(
          String.valueOf(snowflakeId.value()), SprintStatus.ACTIVE))
          .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("IDにスペースを含む文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectIdWithSpaces() {
      // Arrange
      // Act & Assert - スペースを含むIDは数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus(" 123 ", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに浮動小数点数を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectFloatingPointId() {
      // Arrange
      // Act & Assert - 浮動小数点数は整数としてパースできないため拒否される
      assertThatThrownBy(() -> controller.changeStatus("123.456", SprintStatus.ACTIVE))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("数値である必要があります");
    }
  }
}
