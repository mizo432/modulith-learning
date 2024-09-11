package undecided.erp.shared.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.common.snowflake.SnowflakeIdProvider;
import undecided.erp.common.snowflake.StaticSnowflakeIdProvider;

class SnowflakeIdTest {

  @BeforeEach
  void setUo() {
    StaticSnowflakeIdProvider.initialize(10L);
    ;

  }

  @AfterEach
  void tearDown() {
    SnowflakeIdProvider.clear();

  }

  @Nested
  class NewInstanceTest {

    @Test
    void shouldReturnNotNullWhenNewInstance() {
      assertThat(SnowflakeId.newInstance()).isNotNull();

    }

  }

  @Nested
  class ToStringTest {

    @Test
    void shouldReturnPersistencyWhenToString() {
      SnowflakeId snowflakeId = SnowflakeId.newInstance();
      assertThat(snowflakeId.toString()).isEqualTo("10");
    }

  }

  @Nested
  class EqualsTest {

    @Test
    void shouldReturnFalseWhenEqualsNull() {
      SnowflakeId snowflakeId = SnowflakeId.newInstance();
      assertThat(snowflakeId.equals(null)).isFalse();
    }

    @Test
    void shouldReturnTrueWhenEqualsReflexivity() {
      SnowflakeId snowflakeId = SnowflakeId.newInstance();
      assertThat(snowflakeId.equals(snowflakeId)).isTrue();
    }

    @Test
    void shouldReturnTrueWhenEqualsSymmetry() {
      SnowflakeId snowflakeId1 = SnowflakeId.newInstance();
      SnowflakeId snowflakeId2 = SnowflakeId.of(snowflakeId1.getValue());
      assertThat(snowflakeId1.equals(snowflakeId2)).isTrue();
      assertThat(snowflakeId2.equals(snowflakeId1)).isTrue();
    }

    @Test
    void shouldReturnTrueWhenEqualsTransitivity() {
      SnowflakeId snowflakeId1 = SnowflakeId.newInstance();
      SnowflakeId snowflakeId2 = SnowflakeId.of(snowflakeId1.getValue());
      assertThat(snowflakeId1.equals(snowflakeId2)).isTrue();
      assertThat(snowflakeId2.equals(snowflakeId1)).isTrue();
    }

  }

  @Nested
  class ReconstructTest {

    @Test
    void shouldReturnNotNullValueWhenReconstructNotNullValue() {
      SnowflakeId snowflakeId = SnowflakeId.reconstruct(15L);
      assertThat(snowflakeId).isNotNull();
      assertThat(snowflakeId.getValue()).isEqualTo(15L);
    }

    @Test
    void shouldReturnEmptyWhenReconstructNullValue() {
      SnowflakeId snowflakeId = SnowflakeId.reconstruct(null);
      assertThat(snowflakeId).isSameAs(SnowflakeId.EMPTY);
    }

  }
}
