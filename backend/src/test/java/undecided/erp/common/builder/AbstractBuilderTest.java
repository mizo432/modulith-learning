package undecided.erp.common.builder;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * AbstractBuilderクラスのためのテストクラスです。
 */
class AbstractBuilderTest {

  /**
   * このクラスはConcreteClassとConcreteRecordビルダーのbuild()メソッドのテストを含んでいます。
   */
  @Nested
  @DisplayName("buildメソッドテスト")
  class BuildTest {

    @Test
    @DisplayName("ConcreteClassがBuilderから正しく作成される")
    void shouldCreateConcreteClassFromBuilder() {
      ConcreteClass actualClass = ConcreteClass.builder()
          .withId(1)
          .withName("name")
          .build();
      assertThat(actualClass).isNotNull();
      System.out.println(actualClass);
    }

    @Test
    @DisplayName("ConcreteRecordがBuilderから正しく作成される")
    void shouldCreateConcreteRecordFromBuilder() {
      ConcreteRecord actualClass = ConcreteRecord.builder()
          .withId(1)
          .withName("name")
          .build();
      assertThat(actualClass).isNotNull();
      System.out.println(actualClass);
    }

  }

  /**
   * ApplyTest クラスは ConcreteClass と ConcreteRecord の apply() メソッドをテストする責任があります。
   */
  @Nested
  @DisplayName("applyメソッドテスト")
  class ApplyTest {

    @Test
    @DisplayName("ConcreteClassがApplyメソッドを用いて正しく作成される")
    void shouldCreateConcreteClassUsingApplyMethod() {
      ConcreteClass source = new ConcreteClass(2, "name2");
      ConcreteClass actualClass = ConcreteClass.builder()
          .withId(null)
          .withName(null)
          .withId(1)
          .withName("name")
          .apply(source);
      assertThat(actualClass).isNotNull();
      System.out.println(actualClass);
    }

    @Test
    @DisplayName("ConcreteRecordがApplyメソッドを用いて正しく作成される")
    void shouldCreateConcreteRecordUsingApplyMethod() {
      ConcreteRecord source = new ConcreteRecord(2, "name2");
      ConcreteRecord actualClass = ConcreteRecord.builder()
          .withId(null)
          .withName(null)
          .withId(1)
          .withName("name")
          .apply(source);
      assertThat(actualClass).isNotNull();
      System.out.println(actualClass);
    }

  }
}