package undecided.association.relationshipMgmt.partyRole.spi.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;

@Entity
@Table(
    name = "role_assignment_for_emp",
    indexes = {
      @Index(name = "fk01_role_assignment_for_emp", columnList = "role_id"),
      @Index(name = "fk02_role_assignment_for_emp", columnList = "employee_id")
    })
@Getter
public class RoleAssignmentForEmp {
  @Id private UUID roleAssignmentForEmpId;

  @Column(name = "role_id")
  private UUID roleId;

  @Column(name = "employee_id")
  private UUID employeeId;

  // JPA用パッケージプライベートセッター
  void setRoleAssignmentForEmpId(UUID roleAssignmentForEmpId) {
    this.roleAssignmentForEmpId = roleAssignmentForEmpId;
  }

  void setRoleId(UUID roleId) {
    this.roleId = roleId;
  }

  void setEmployeeId(UUID employeeId) {
    this.employeeId = employeeId;
  }
}
