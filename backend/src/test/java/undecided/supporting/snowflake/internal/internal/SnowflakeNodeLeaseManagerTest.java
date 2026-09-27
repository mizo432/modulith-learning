package undecided.supporting.snowflake.internal.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import undecided.supporting.snowflake.internal.NodeId;
import undecided.supporting.snowflake.internal.SnowflakeNode;
import undecided.supporting.snowflake.internal.SnowflakeNodeAllocator;
import undecided.supporting.snowflake.internal.SnowflakeNodeLeaseManager;

@Tag("small")
@DisplayName("SnowflakeNodeLeaseManagerのテスト")
class SnowflakeNodeLeaseManagerTest {

  private SnowflakeNodeAllocator allocator;
  private SnowflakeNodeLeaseManager manager;

  @BeforeEach
  void setUp() {
    allocator = mock(SnowflakeNodeAllocator.class);
    manager = new SnowflakeNodeLeaseManager(allocator);
    ReflectionTestUtils.setField(manager, "applicationName", "test-app");
  }

  @Nested
  @DisplayName("startメソッドのテスト")
  class StartMethodTest {

    @Test
    @DisplayName("start呼び出し時にノードが割り当てられること")
    void shouldAllocateNodeOnStart() {
      SnowflakeNode node = new SnowflakeNode(5, "test-app", LocalDateTime.now().plusSeconds(30));
      when(allocator.allocate("test-app")).thenReturn(node);

      manager.start();

      verify(allocator).allocate("test-app");
      assertThat(manager.nodeId().getValue()).isEqualTo(5);
    }
  }

  @Nested
  @DisplayName("nodeIdメソッドのテスト")
  class NodeIdMethodTest {

    @Test
    @DisplayName("初期化されていない場合、IllegalStateExceptionがスローされること")
    void shouldThrowExceptionWhenNotInitialized() {
      assertThatThrownBy(() -> manager.nodeId())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Snowflake node is not initialized");
    }

    @Test
    @DisplayName("初期化済みの場合、正しいNodeIdが返されること")
    void shouldReturnCorrectNodeIdWhenInitialized() {
      SnowflakeNode node = new SnowflakeNode(10, "test-app", LocalDateTime.now().plusSeconds(30));
      when(allocator.allocate("test-app")).thenReturn(node);
      manager.start();

      NodeId nodeId = manager.nodeId();

      assertThat(nodeId.getValue()).isEqualTo(10);
    }
  }

  @Nested
  @DisplayName("renewメソッドのテスト")
  class RenewMethodTest {

    @Test
    @DisplayName("初期化されていない場合、IllegalStateExceptionがスローされること")
    void shouldThrowExceptionWhenNotInitialized() {
      assertThatThrownBy(() -> manager.renew())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Snowflake node is not initialized");
    }

    @Test
    @DisplayName("初期化済みの場合、allocator.renewの結果が返されること")
    void shouldDelegateRenewToAllocatorWhenInitialized() {
      SnowflakeNode node = new SnowflakeNode(10, "test-app", LocalDateTime.now().plusSeconds(30));
      when(allocator.allocate("test-app")).thenReturn(node);
      when(allocator.renew(node)).thenReturn(true);
      manager.start();

      boolean result = manager.renew();

      assertThat(result).isTrue();
      verify(allocator).renew(node);
    }
  }
}
