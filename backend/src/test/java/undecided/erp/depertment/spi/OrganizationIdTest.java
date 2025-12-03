package undecided.erp.depertment.spi;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrganizationIdTestクラス")
class OrganizationIdTest {

  @Nested
  @DisplayName("getLevelメソッドのテスト")
  class GetLevelTests {

    @Test
    @DisplayName("全てが0の場合、階層の深さはDEPTH_LEVEL_0を返すべき")
    void shouldReturnDepthLevel0WhenAllZeros() {
      OrganizationId organizationId = new OrganizationId("0000000000");

      Integer level = organizationId.getLevel();

      assertThat(level).isEqualTo(0);
    }

    @Test
    @DisplayName("末尾がLEVEL_1_SUFFIXの場合、階層の深さはDEPTH_LEVEL_1を返すべき")
    void shouldReturnDepthLevel1WhenEndsWithLevel1Suffix() {
      OrganizationId organizationId = new OrganizationId("1234000000");

      Integer level = organizationId.getLevel();

      assertThat(level).isEqualTo(1);
    }

    @Test
    @DisplayName("末尾がLEVEL_2_SUFFIXの場合、階層の深さはDEPTH_LEVEL_2を返すべき")
    void shouldReturnDepthLevel2WhenEndsWithLevel2Suffix() {
      OrganizationId organizationId = new OrganizationId("1234567000");

      Integer level = organizationId.getLevel();

      assertThat(level).isEqualTo(2);
    }

    @Test
    @DisplayName("特定の条件に合わない場合、階層の深さはDEPTH_LEVEL_3を返すべき")
    void shouldReturnDepthLevel3WhenNoSpecificConditionMatches() {
      OrganizationId organizationId = new OrganizationId("1234567890");

      Integer level = organizationId.getLevel();

      assertThat(level).isEqualTo(3);
    }

    @Test
    @DisplayName("値がnullの場合は例外をスローすべき")
    void shouldThrowExceptionWhenValueIsNull() {
      assertThatThrownBy(() -> new OrganizationId(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("value must not be null!");
    }

    @Test
    @DisplayName("値が10桁以外の場合は例外をスローすべき")
    void shouldThrowExceptionWhenValueIsNot10Digits() {
      assertThatThrownBy(() -> new OrganizationId("12345"))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("value must be 10 digits!");
    }
  }
}
