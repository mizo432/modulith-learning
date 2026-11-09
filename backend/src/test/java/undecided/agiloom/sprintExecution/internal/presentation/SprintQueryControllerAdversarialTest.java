package undecided.agiloom.sprintExecution.internal.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.mizo432.shared.exception.BusinessException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import undecided.agiloom.sprintExecution.spi.SprintDTO;
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
  private SprintResponseAssembler sprintResponseAssembler;

  private SprintQueryController controller;

  @BeforeEach
  void setUp() {
    sprintResponseAssembler = new SprintResponseAssembler();
    controller = new SprintQueryController(sprintQueryService, sprintResponseAssembler);
  }

  private static final UUID FIXED_PRODUCT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private static final UUID FIXED_TEAM_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

  private SprintDTO createSprintDTO(SnowflakeId id, SprintStatus status) {
    return SprintDTO.builder()
        .id(id)
        .productId(FIXED_PRODUCT_ID)
        .sprintNumber(1)
        .title("Sprint 1")
        .goal("Goal 1")
        .startDate(LocalDate.now())
        .endDate(LocalDate.now().plusWeeks(2))
        .status(status)
        .teamId(FIXED_TEAM_ID)
        .teamName("Team A")
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  @Nested
  @DisplayName("getSprints - ページネーション攻撃検証")
  class GetSprintsAdversarialTest {

    @Test
    @DisplayName("sizeに負の値を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectNegativeSize() {
      // Act & Assert - @Validated + @Min(1)により、Spring MVC層ではConstraintViolationExceptionで拒否される
      // ユニットテストではSpring MVC層を通らないため、PageRequest.of()がIllegalArgumentExceptionをスローする
      assertThatThrownBy(() -> controller.getSprints(0, -1))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("sizeに0を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectZeroSize() {
      // Act & Assert - @Validated + @Min(1)により、Spring MVC層ではConstraintViolationExceptionで拒否される
      // ユニットテストではSpring MVC層を通らないため、PageRequest.of()がIllegalArgumentExceptionをスローする
      assertThatThrownBy(() -> controller.getSprints(0, 0))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("sizeにInteger.MAX_VALUEを指定した場合、受理されること")
    void shouldHandleMaxSize() {
      // Arrange
      when(sprintQueryService.findAll(any(Pageable.class))).thenReturn(List.of());

      // Act & Assert - @Max(100)制約により、Spring MVC層ではConstraintViolationExceptionで拒否される
      // ユニットテストではSpring MVC層を通らないため、受理される
      List<SprintResponse> result = controller.getSprints(0, Integer.MAX_VALUE);
      assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("pageに負の値を指定した場合、IllegalArgumentExceptionをスローすること")
    void shouldRejectNegativePage() {
      // Act & Assert - @Validated + @Min(0)により、Spring MVC層ではConstraintViolationExceptionで拒否される
      // ユニットテストではSpring MVC層を通らないため、PageRequest.of()がIllegalArgumentExceptionをスローする
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
      List<SprintResponse> result = controller.getSprints(Integer.MAX_VALUE, 20);
      assertThat(result).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("pageとsizeの両方に极大値を指定した場合、受理されること")
    void shouldHandleMaxPageAndSize() {
      // Arrange
      when(sprintQueryService.findAll(any(Pageable.class))).thenReturn(List.of());

      // Act & Assert - @Max(100)制約により、Spring MVC層ではConstraintViolationExceptionで拒否される
      // ユニットテストではSpring MVC層を通らないため、受理される
      List<SprintResponse> result = controller.getSprints(Integer.MAX_VALUE, Integer.MAX_VALUE);
      assertThat(result).isNotNull().isEmpty();
    }
  }

  @Nested
  @DisplayName("getSprint - ID不正入力検証")
  class GetSprintAdversarialTest {

    @Test
    @DisplayName("IDに数値以外の文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldThrowBusinessExceptionForNonNumericId() {
      // Act & Assert - IDパース時にNumberFormatExceptionをIllegalArgumentExceptionに変換する
      assertThatThrownBy(() -> controller.getSprint("abc"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに空文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldThrowBusinessExceptionForEmptyId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint(""))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに負の値を指定した場合、BusinessExceptionをスローすること")
    void shouldHandleNegativeId() {
      // Act & Assert - Long.parseLong("-123")は正常にパースされるが、SnowflakeId.of()で正の値チェックに失敗し、BusinessExceptionに変換される
      assertThatThrownBy(() -> controller.getSprint("-123"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("正の値である必要があります");
    }

    @Test
    @DisplayName("IDにLong.MAX_VALUEより大きな値を指定した場合、BusinessExceptionをスローすること")
    void shouldThrowBusinessExceptionForOverflowId() {
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("99999999999999999999"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにSQLインジェクション文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectSqlInjectionInId() {
      // Act & Assert - SQLインジェクション文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.getSprint("1; DROP TABLE sprint;"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにXSS文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectXssInId() {
      // Act & Assert - XSS文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.getSprint("<script>alert(1)</script>"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにパストラバーサル文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectPathTraversalInId() {
      // Act & Assert - パストラバーサル文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.getSprint("../../etc/passwd"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにコマンドインジェクション文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectCommandInjectionInId() {
      // Act & Assert - コマンドインジェクション文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.getSprint("1 | rm -rf /"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにスペースを含む文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectIdWithSpaces() {
      // Act & Assert - スペースを含むIDは数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.getSprint(" 123 "))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDに浮動小数点数を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectFloatingPointId() {
      // Act & Assert - 浮動小数点数は整数としてパースできないため拒否される
      assertThatThrownBy(() -> controller.getSprint("123.456"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにNullバイトを含む文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldRejectNullByteInId() {
      // Act & Assert - Nullバイトを含む文字列は数値としてパースできないため拒否される
      assertThatThrownBy(() -> controller.getSprint("123\0456"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("数値である必要があります");
    }

    @Test
    @DisplayName("IDにUnicode全角数字を含む文字列を指定した場合、BusinessExceptionをスローすること")
    void shouldHandleUnicodeInId() {
      // Arrange - 全角数字はLong.parseLongで正常にパースされるが、DBに存在しないためNotFoundになる
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.empty());
      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("１２３"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint not found");
    }

    @Test
    @DisplayName("Sprintが見つからない場合、BusinessExceptionをスローすること")
    void shouldThrowExceptionWhenNotFound() {
      // Arrange
      when(sprintQueryService.findById(any(SnowflakeId.class))).thenReturn(Optional.empty());

      // Act & Assert
      assertThatThrownBy(() -> controller.getSprint("1234567890"))
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("Sprint not found");
    }
  }
}
