package undecided.association.relationshipMgmt.personRole.spi.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(name = "employees", schema = "relationship_mgmt")
@Getter
@Setter
public class SimpleEmployee {
  @Id private SnowflakeId employeeId;

  @Embedded
  @Column(nullable = false, columnDefinition = "VARCHAR(10)")
  private EmployeeNo employeeNo;

  /** 検索用名称 */
  @Embedded
  @Column(nullable = false, columnDefinition = "VARCHAR(50)")
  private SearchName searchName;

  @Embedded
  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private FullName fullName;
}
