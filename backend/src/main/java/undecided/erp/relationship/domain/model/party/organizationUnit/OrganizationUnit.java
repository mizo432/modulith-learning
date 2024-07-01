package undecided.erp.relationship.domain.model.party.organizationUnit;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.shared.date.DateInterval;
import undecided.erp.shared.entity.SnowflakeId;

/**
 * OrganizationUnitクラスは組織内の単位を表現します。
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class OrganizationUnit {

  @SuppressWarnings("unchecked")
  @AttributeOverride(name = "value", column = @Column(name = "organization_Unit_id"))
  private SnowflakeId<Party> id = (SnowflakeId<Party>) SnowflakeId.EMPTY;

  @SuppressWarnings("unchecked")
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "organizationId"))
  private SnowflakeId<Party> organizationId = (SnowflakeId<Party>) SnowflakeId.EMPTY;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "name.value", column = @Column(name = "name"))
  })
  private OrganizationUnitAttribute attribute = OrganizationUnitAttribute.EMPTY;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "beginDate.value", column = @Column(name = "begin_date")),
      @AttributeOverride(name = "endDate.value", column = @Column(name = "end_date"))})
  private DateInterval interval = DateInterval.EMPTY;

  public static OrganizationUnit create(@NonNull SnowflakeId<Party> id,
      @NonNull SnowflakeId<Party> organizationId,
      @NonNull OrganizationUnitAttribute attribute, @NonNull DateInterval interval) {
    return new OrganizationUnit(id, organizationId, attribute, interval);
  }

}
