package undecided.erp.depertment.spi;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("OrganizationTestクラス")
class OrganizationTest {

  @Nested
  @DisplayName("getParentOrganizationIdメソッドのテスト")
  class GetParentOrganizationIdTests {

    @Test
    @DisplayName("階層レベルが0の場合、getParentOrganizationIdはnullを返すべき")
    void shouldReturnNullWhenLevelIs0() {
      // Setup
      Organization organization = new Organization();
      organization.setOrganizationId("0000000000");

      // Execute
      String parentOrganizationId = organization.getParentOrganizationId();

      // Verify
      assertThat(parentOrganizationId).isNull();
    }

    @Test
    @DisplayName("階層レベルが1の場合、getParentOrganizationIdはレベル0のIDを返すべき")
    void shouldReturnLevel0IdWhenLevelIs1() {
      // Setup
      Organization organization = new Organization();
      organization.setOrganizationId("1234000000");

      // Execute
      String parentOrganizationId = organization.getParentOrganizationId();

      // Verify
      assertThat(parentOrganizationId).isEqualTo("0000000000");
    }

    @Test
    @DisplayName("階層レベルが2の場合、getParentOrganizationIdはレベル1のIDを返すべき")
    void shouldReturnLevel1IdWhenLevelIs2() {
      // Setup
      Organization organization = new Organization();
      organization.setOrganizationId("1234567000");

      // Execute
      String parentOrganizationId = organization.getParentOrganizationId();

      // Verify
      assertThat(parentOrganizationId).isEqualTo("1234000000");
    }

    @Test
    @DisplayName("階層レベルが3の場合、getParentOrganizationIdはレベル2のIDを返すべき")
    void shouldReturnLevel2IdWhenLevelIs3() {
      // Setup
      Organization organization = new Organization();
      organization.setOrganizationId("1234567890");

      // Execute
      String parentOrganizationId = organization.getParentOrganizationId();

      // Verify
      assertThat(parentOrganizationId).isEqualTo("1234567000");
    }

    @Test
    @DisplayName("organizationIdがnullの場合、getParentOrganizationIdは例外をスローすべき")
    void shouldThrowExceptionWhenOrganizationIdIsNull() {
      // Setup
      Organization organization = new Organization();

      // Execute & Verify
      assertThatThrownBy(organization::getParentOrganizationId)
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("value must not be null!");
    }

    @Test
    @DisplayName("organizationIdが10桁以外の場合、getParentOrganizationIdは例外をスローすべき")
    void shouldThrowExceptionWhenOrganizationIdInvalid() {
      // Setup
      Organization organization = new Organization();
      organization.setOrganizationId("12345");

      // Execute & Verify
      assertThatThrownBy(organization::getParentOrganizationId)
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("value must be 10 digits!");
    }
  }
}
