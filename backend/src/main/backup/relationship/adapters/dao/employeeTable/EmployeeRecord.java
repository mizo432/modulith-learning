package undecided.erp.relationship.adapters.dao.employeeTable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import undecided.erp.relationship.domain.model.personRole.employee.Employee;

/**
 * Represents an employee record.
 */
@Entity
@Table(schema = "relationship", name = "employees"
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class EmployeeRecord {

  @Id
  @Column(name = "person_id", unique = true, nullable = false)
  private Long id;

  @Column(nullable = false, length = 30)
  private String employeeNo;

  public static EmployeeRecord fromEntity(Employee employee) {
    return new EmployeeRecord(employee.getId().getValue(), employee.getEmployeeNo().value());
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    EmployeeRecord that = (EmployeeRecord) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass().hashCode() : getClass().hashCode();
  }
}
