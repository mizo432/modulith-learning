package undecided.erp.relationship.domain.model.party.organizationUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.shared.date.ApplicableDate;
import undecided.erp.shared.date.DateInterval;
import undecided.erp.shared.entity.SnowflakeId;

public class OrganizationUnitTest {

  @Nested
  class CreateTest {

    @Test
    void shouldCreateValidOrganizationUnitGivenCorrectAttributes() {
      SnowflakeId<Party> id = SnowflakeId.of(123L);
      SnowflakeId<Party> organizationId = SnowflakeId.of(456L);
      OrganizationUnitName unitName = new OrganizationUnitName("Test Unit");
      OrganizationUnitAttribute attribute = OrganizationUnitAttribute.create(unitName);
      DateInterval interval = DateInterval.create(ApplicableDate.of(
              LocalDate.parse("2021-01-01")),
          ApplicableDate.of(LocalDate.parse("2022-01-01")));

      OrganizationUnit result = OrganizationUnit.create(id, organizationId, attribute, interval);

      assertThat(result).as("Creation of OrganizationUnit results in a non-null entity")
          .isNotNull();
      assertThat(result.getId()).as("OrganizationUnit id matches provided id").isEqualTo(id);
      assertThat(result.getOrganizationId()).as(
              "OrganizationUnit organizationId matches provided organizationId")
          .isEqualTo(organizationId);
      assertThat(result.getAttribute()).as("OrganizationUnit attribute matches provided attribute")
          .isEqualTo(attribute);
      assertThat(result.getInterval()).as("OrganizationUnit interval matches provided interval")
          .isEqualTo(interval);
    }

    @Test
    void shouldThrowExceptionGivenAllNullParameters() {
      assertThatThrownBy(() -> {
        OrganizationUnit.create(null, null, null, null);
      }).isInstanceOf(NullPointerException.class)
          .hasMessage("id is marked non-null but is null");
    }

    @Test
    void shouldThrowExceptionGivenNullId() {
      SnowflakeId<Party> organizationId = SnowflakeId.of(456L);
      OrganizationUnitName unitName = new OrganizationUnitName("Test Unit");
      OrganizationUnitAttribute attribute = OrganizationUnitAttribute.create(unitName);
      DateInterval interval = DateInterval.create(ApplicableDate.of(
              LocalDate.parse("2021-01-01")),
          ApplicableDate.of(LocalDate.parse("2022-01-01")));
      assertThatThrownBy(() -> {
        OrganizationUnit.create(null, organizationId, attribute, interval);
      }).isInstanceOf(NullPointerException.class)
          .hasMessage("id is marked non-null but is null");
    }

    @Test
    void shouldThrowExceptionGivenNullOrganizationId() {
      SnowflakeId<Party> id = SnowflakeId.of(123L);
      OrganizationUnitName unitName = new OrganizationUnitName("Test Unit");
      OrganizationUnitAttribute attribute = OrganizationUnitAttribute.create(unitName);
      DateInterval interval = DateInterval.create(ApplicableDate.of(
              LocalDate.parse("2021-01-01")),
          ApplicableDate.of(LocalDate.parse("2022-01-01")));
      assertThatThrownBy(() -> {
        OrganizationUnit.create(id, null, attribute, interval);
      }).isInstanceOf(NullPointerException.class)
          .hasMessage("organizationId is marked non-null but is null");
    }

    @Test
    void shouldThrowExceptionGivenNullAttribute() {
      SnowflakeId<Party> id = SnowflakeId.of(123L);
      SnowflakeId<Party> organizationId = SnowflakeId.of(456L);
      DateInterval interval = DateInterval.create(ApplicableDate.of(
              LocalDate.parse("2021-01-01")),
          ApplicableDate.of(LocalDate.parse("2022-01-01")));
      assertThatThrownBy(() -> {
        OrganizationUnit.create(id, organizationId, null, interval);
      }).isInstanceOf(NullPointerException.class)
          .hasMessage("attribute is marked non-null but is null");
    }

    @Test
    void shouldThrowExceptionGivenNullInterval() {
      SnowflakeId<Party> id = SnowflakeId.of(123L);
      SnowflakeId<Party> organizationId = SnowflakeId.of(456L);
      OrganizationUnitName unitName = new OrganizationUnitName("Test Unit");
      OrganizationUnitAttribute attribute = OrganizationUnitAttribute.create(unitName);
      ApplicableDate.of(LocalDate.parse("2022-01-01"));
      assertThatThrownBy(() -> {
        OrganizationUnit.create(id, organizationId, attribute, null);
      }).isInstanceOf(NullPointerException.class)
          .hasMessage("interval is marked non-null but is null");
    }
  }
}

