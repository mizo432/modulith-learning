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
import undecided.erp.employee.Employee;

@Entity
@Table(
    name = "role_assignment_for_emp",
    indexes = {
      @Index(name = "fk01_role_assignment_for_emp", columnList = "role_id"),
      @Index(name = "fk02_role_assignment_for_emp", columnList = "employee_id")
    })
@Getter
@Setter
public class RoleAssignmentForEmp {
  @Id private UUID roleAssignmentForEmpId;

  @Column(name = "role_id")
  private UUID roleId;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  private Employee organization;
}
