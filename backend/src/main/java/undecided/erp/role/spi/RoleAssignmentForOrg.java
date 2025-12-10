package undecided.erp.role.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import undecided.erp.organization.spi.Organization;

@Entity
@Table(
    name = "role_assignment_for_org",
    indexes = {
      @Index(name = "fk01_role_assignment_for_org", columnList = "role_id"),
      @Index(name = "fk02_role_assignment_for_org", columnList = "organization_id")
    })
@Getter
@Setter
public class RoleAssignmentForOrg {
  @Id private UUID roleAssignmentForOrgId;

  @Column(name = "role_id")
  private UUID roleId;

  @ManyToOne
  @JoinColumn(name = "organization_id")
  private Organization organization;
}
