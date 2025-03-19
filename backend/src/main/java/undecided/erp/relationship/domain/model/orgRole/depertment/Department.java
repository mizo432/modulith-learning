package undecided.erp.relationship.domain.model.orgRole.depertment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.relationship.domain.model.orgRole.OrganizationRole;

@Table(name = "departments")
@Entity
@AllArgsConstructor
public class Department extends OrganizationRole {

  @Id
  private SnowflakeId departmentId;

  protected Department() {

  }
}
