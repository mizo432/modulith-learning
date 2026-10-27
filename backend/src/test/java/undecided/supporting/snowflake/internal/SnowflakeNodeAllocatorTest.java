package undecided.supporting.snowflake.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import undecided.shared.dateProvider.StaticDateTimeProvider;

@Tag("small")
@DisplayName("SnowflakeNodeAllocatorのテスト")
class SnowflakeNodeAllocatorTest {

  private final LocalDateTime fixedNow = LocalDateTime.of(2026, 9, 27, 10, 0, 0);
  private SnowflakeNodeJpaRepository repository;
  private SnowflakeNodeAllocator allocator;

  @BeforeEach
  void setUp() {
    StaticDateTimeProvider.initialize(fixedNow);
    repository = mock(SnowflakeNodeJpaRepository.class);
    allocator = new SnowflakeNodeAllocator(repository);
  }

  @AfterEach
  void tearDown() {
    StaticDateTimeProvider.clear();
  }

  @Nested
  @DisplayName("allocateメソッドのテスト")
  class AllocateMethodTest {

    @Test
    @DisplayName("空きノードが存在する場合、ノードが割り当てられて保存されること")
    void shouldAllocateNewNodeWhenAvailable() {
      when(repository.findById(0)).thenReturn(Optional.empty());

      SnowflakeNode result = allocator.allocate("test-app");

      assertThat(result).isNotNull();
      assertThat(result.getNodeId()).isEqualTo(0);
      assertThat(result.getInstanceName()).isEqualTo("test-app");
      assertThat(result.getLeaseUntil()).isEqualTo(fixedNow.plusSeconds(30));

      ArgumentCaptor<SnowflakeNode> captor = ArgumentCaptor.forClass(SnowflakeNode.class);
      verify(repository).save(captor.capture());
      SnowflakeNode saved = captor.getValue();
      assertThat(saved.getNodeId()).isEqualTo(0);
      assertThat(saved.getInstanceName()).isEqualTo("test-app");
      assertThat(saved.getLeaseUntil()).isEqualTo(fixedNow.plusSeconds(30));
    }

    @Test
    @DisplayName("既存ノードのリースが切れている場合、ノードが再割り当てされること")
    void shouldReallocateExpiredNode() {
      SnowflakeNode expiredNode = new SnowflakeNode(0, "old-app", fixedNow.minusSeconds(10));
      when(repository.findById(0)).thenReturn(Optional.of(expiredNode));

      SnowflakeNode result = allocator.allocate("new-app");

      assertThat(result).isNotNull();
      assertThat(result.getNodeId()).isEqualTo(0);
      assertThat(result.getInstanceName()).isEqualTo("new-app");
      assertThat(result.getLeaseUntil()).isEqualTo(fixedNow.plusSeconds(30));

      verify(repository).save(any(SnowflakeNode.class));
    }

    @Test
    @DisplayName("利用可能なノードが存在しない場合、例外がスローされること")
    void shouldThrowExceptionWhenNoNodeAvailable() {
      for (int i = 0; i < NodeId.MAX; i++) {
        when(repository.findById(i))
            .thenReturn(Optional.of(new SnowflakeNode(i, "other-app", fixedNow.plusSeconds(20))));
      }

      assertThatThrownBy(() -> allocator.allocate("test-app"))
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("No snowflake node ID available.");
    }
  }

  @Nested
  @DisplayName("renewメソッドのテスト")
  class RenewMethodTest {

    @Test
    @DisplayName("有効なリースの場合、更新が成功しtrueを返すこと")
    void shouldRenewSuccessfullyWhenLeaseIsValid() {
      SnowflakeNode currentLease = new SnowflakeNode(1, "test-app", fixedNow.plusSeconds(20));
      when(repository.findById(1)).thenReturn(Optional.of(currentLease));

      boolean result = allocator.renew(currentLease);

      assertThat(result).isTrue();
      ArgumentCaptor<SnowflakeNode> captor = ArgumentCaptor.forClass(SnowflakeNode.class);
      verify(repository).save(captor.capture());
      SnowflakeNode saved = captor.getValue();
      assertThat(saved.getNodeId()).isEqualTo(1);
      assertThat(saved.getInstanceName()).isEqualTo("test-app");
      assertThat(saved.getLeaseUntil()).isEqualTo(fixedNow.plusSeconds(30));
    }

    @Test
    @DisplayName("ノードが存在しない場合、falseを返すこと")
    void shouldReturnFalseWhenNodeNotFound() {
      SnowflakeNode currentLease = new SnowflakeNode(1, "test-app", fixedNow.plusSeconds(20));
      when(repository.findById(1)).thenReturn(Optional.empty());

      boolean result = allocator.renew(currentLease);

      assertThat(result).isFalse();
      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("インスタンス名が一致しない場合、falseを返すこと")
    void shouldReturnFalseWhenInstanceNameDoesNotMatch() {
      SnowflakeNode dbNode = new SnowflakeNode(1, "other-app", fixedNow.plusSeconds(20));
      SnowflakeNode currentLease = new SnowflakeNode(1, "test-app", fixedNow.plusSeconds(20));
      when(repository.findById(1)).thenReturn(Optional.of(dbNode));

      boolean result = allocator.renew(currentLease);

      assertThat(result).isFalse();
      verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("リース期限が切れている場合、更新を行わずfalseを返すこと")
    void shouldReturnFalseWhenLeaseHasExpired() {
      SnowflakeNode expiredDbNode = new SnowflakeNode(1, "test-app", fixedNow.minusSeconds(1));
      SnowflakeNode currentLease = new SnowflakeNode(1, "test-app", fixedNow.minusSeconds(1));
      when(repository.findById(1)).thenReturn(Optional.of(expiredDbNode));

      boolean result = allocator.renew(currentLease);

      assertThat(result).isFalse();
      verify(repository, never()).save(any());
    }
  }
}
