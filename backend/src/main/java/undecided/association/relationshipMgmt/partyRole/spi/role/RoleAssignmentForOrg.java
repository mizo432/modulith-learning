package undecided.association.relationshipMgmt.partyRole.spi.role;

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
import undecided.association.relationshipMgmt.spi.party.org.Org;

@Entity
@Table(
    name = "role_assignment_for_org",
    indexes = {
      @Index(name = "fk01_role_assignment_for_org", columnList = "role_id"),
      @Index(name = "fk02_role_assignment_for_org", columnList = "org_id")
    })
@Getter
@Setter
public class RoleAssignmentForOrg {
  @Id private UUID roleAssignmentForOrgId;

  @Column(name = "role_id")
  private UUID roleId;

  @ManyToOne
  @JoinColumn(name = "org_id")
  private Org org;
}
