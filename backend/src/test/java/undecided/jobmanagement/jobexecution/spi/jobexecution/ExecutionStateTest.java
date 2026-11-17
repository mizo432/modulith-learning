package undecided.jobmanagement.jobexecution.spi.jobexecution;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("ExecutionStateのテスト")
class ExecutionStateTest {

  @Test
  @DisplayName("全列挙値が取得できる")
  void shouldGetAllValues() {
    ExecutionState[] states = ExecutionState.values();

    assertThat(states).hasSize(8);
    assertThat(states).containsExactly(
        ExecutionState.PENDING,
        ExecutionState.RUNNING,
        ExecutionState.SUCCESS,
        ExecutionState.FAILED,
        ExecutionState.UNKNOWN,
        ExecutionState.RE_EXECUTED,
        ExecutionState.RESUMED,
        ExecutionState.CANCELLED
    );
  }

  @Test
  @DisplayName("名前から列挙値を取得できる")
  void shouldGetValueByName() {
    ExecutionState state = ExecutionState.valueOf("PENDING");

    assertThat(state).isEqualTo(ExecutionState.PENDING);
  }

  @Test
  @DisplayName("無効な名前を指定するとIllegalArgumentExceptionがスローされる")
  void shouldThrowExceptionWhenInvalidName() {
    org.junit.jupiter.api.Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> ExecutionState.valueOf("INVALID")
    );
  }
}
