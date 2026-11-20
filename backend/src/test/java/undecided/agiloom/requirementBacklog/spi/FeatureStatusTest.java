package undecided.agiloom.requirementBacklog.spi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("FeatureStatusの単体テスト")
class FeatureStatusTest {

  @Test
  @DisplayName("PLANNEDのラベルは計画済みであること")
  void shouldReturnLabelForPlanned() {
    assertThat(FeatureStatus.PLANNED.label()).isEqualTo("計画済み");
  }

  @Test
  @DisplayName("IN_PROGRESSのラベルは進行中であること")
  void shouldReturnLabelForInProgress() {
    assertThat(FeatureStatus.IN_PROGRESS.label()).isEqualTo("進行中");
  }

  @Test
  @DisplayName("DONEのラベルは完了であること")
  void shouldReturnLabelForDone() {
    assertThat(FeatureStatus.DONE.label()).isEqualTo("完了");
  }

  @Test
  @DisplayName("FeatureStatusの定数は3つ存在すること")
  void shouldHaveThreeStatuses() {
    assertThat(FeatureStatus.values()).hasSize(3);
  }
}
