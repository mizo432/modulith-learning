package undecided.agiloom.sprintExecution.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
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
import org.springframework.data.domain.Pageable;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.query.SprintQueryServiceInterface;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * SprintQueryControllerに対する敵対的検証テスト。
 * 不正なページネーションパラメータ、IDインジェクション、境界値攻撃など、
 * 悪意のあるクエリリクエストに対するシステムの堅牢性を検証します。
 */
@Tag("small")
@DisplayName("SprintQueryControllerの敵対的検証テスト")
@ExtendWith(MockitoExtension.class)
class SprintQueryControllerAdversarialTest {

  @Mock private SprintQueryServiceInterface sprintQueryService;

  private SprintQueryController controller;

  @BeforeEach
  void setUp() {
    controller = new SprintQueryController(sprintQueryService);
  }

  private Sprint createSprint(SnowflakeId id, SprintStatus status) {
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
        .status(status)
        .build();
  }

  @Nested
  @DisplayName("getSprints - ページネーション攻撃検証")
  class GetSprintsAdversarialTest {

    @Test
    @DisplayName("sizeに負の値を指定した場合")
    void shouldHandleNegativeSize() {
      // Arrange & Act & Assert
      // 負のsizeが渡された場合の動作を確認
      assertThatThrownBy(() -> controller.getSprints(0, -1))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("sizeに0を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldHandleZeroSize() {
      // Act & Assert - Spring DataのPageRequestはsize=0を許容しない
      assertThatThrownBy(() -> controller.getSprints(0, 0))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("sizeにInteger.MAX_VALUEを指定した場合")
    void shouldHandleMaxSize() {
      // Arrange
      when(sprintQueryService.findAll(any(Pageable.class))).thenReturn(List.of());

      // Act & Assert - 巨大なsizeが受理される可能性がある（DoSリスク）
      // 上限チェックが欠如している
      assertThatCode(() -> controller.getSprints(0, Integer.MAX_VALUE))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("pageに負の値を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldHandleNegativePage() {
      // Act & Assert - Spring DataのPageRequestは負のpage番号を許容しない
      assertThatThrownBy(() -> controller.getSprints(-1, 20))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Page index must not be less than zero");
    }

    @Test
    @DisplayName("pageにInteger.MAX_VALUEを指定した場合")
    void shouldHandleMaxPage() {
      // Arrange
      when(sprintQueryService.findAll(any(Pageable.class))).thenReturn(List.of());

      // Act & Assert
      assertThatCode(() -> controller.getSprints(Integer.MAX_VALUE, 20))
          .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("pageとsizeの両方に极大値を指定した場合")
    void shouldHandleMaxPageAndSize() {
      // Arrange
      when(sprintQueryService.findAll(any(Pageable.class))).thenReturn(List.of());

      // Act & Assert - DoS攻撃の可能性
      assertThatCode(() -> controller.getSprints(Integer.MAX_VALUE, Integer.MAX_VALUE))
          .doesNotThrowAnyException();
    }
  }

  @Nested
  @DisplayName("getSprint - ID不正入力検証")
  class GetSprintAdversarialTest {

    @Test
    @DisplayName("IDに数値以外の文字列を指定した場合、NumberFormatExceptionをスローすること")
    void shouldThrowNumberFormatExceptionForNonNumericId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("abc"))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("IDに空文字列を指定した場合、NumberFormatExceptionをスローすること")
    void shouldThrowNumberFormatExceptionForEmptyId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint(""))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("IDに負の値を指定した場合、SnowflakeIdでIllegalArgumentExceptionをスローすること")
    void shouldHandleNegativeId() {
      // Act & Assert - Long.parseLong("-123")は正常にパースされるが、SnowflakeId.of()で正の値チェックに失敗
      assertThatThrownBy(() -> controller.getSprint("-123"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("value must be positive");
    }

    @Test
    @DisplayName("IDにLong.MAX_VALUEより大きな値を指定した場合")
    void shouldThrowNumberFormatExceptionForOverflowId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("99999999999999999999"))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("IDにSQLインジェクション文字列を指定した場合")
    void shouldHandleSqlInjectionInId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("1; DROP TABLE sprint;"))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("IDにXSS文字列を指定した場合")
    void shouldHandleXssInId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("<script>alert(1)</script>"))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("IDにパストラバーサル文字列を指定した場合")
    void shouldHandlePathTraversalInId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("../../etc/passwd"))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("IDにコマンドインジェクション文字列を指定した場合")
    void shouldHandleCommandInjectionInId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("1 | rm -rf /"))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("IDにスペースを含む文字列を指定した場合")
    void shouldThrowNumberFormatExceptionForIdWithSpaces() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint(" 123 "))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("IDに浮動小数点数を指定した場合")
    void shouldThrowNumberFormatExceptionForFloatingPointId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("123.456"))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("IDにNullバイトを含む文字列を指定した場合")
    void shouldHandleNullByteInId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("123\0456"))
          .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("IDにUnicode全角数字を含む文字列を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldHandleUnicodeInId() {
      // Act & Assert - 全角数字はLong.parseLongでNumberFormatExceptionになるが、
      // SnowflakeId.of()がキャッチしてIllegalArgumentExceptionに変換する
      // または、Long.parseLongが成功し、Sprint not foundになる
      assertThatThrownBy(() -> controller.getSprint("１２３"))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Sprintが見つからない場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenNotFound() {
      // Arrange
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("1234567890"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Sprint not found");
    }
  }
}
