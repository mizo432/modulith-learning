package undecided.erp.role.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role {
  @Id
  @Column(name = "role_id")
  private UUID roleId;

  private String roleName;

  @OneToMany
  @JoinColumn(name = "role_id")
  private List<RoleAssignmentForOrg> orgsAssigned = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "role_id")
  private List<RoleAssignmentForEmp> empsAssigned = new ArrayList<>();
}
