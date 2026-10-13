package undecided.association.relationshipMgmt.personRole.spi.employee;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
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
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "employee_no", nullable = false, length = 10)))
  private EmployeeNo employeeNo;

  /** 検索用名称 */
  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "search_name", nullable = false, length = 50)))
  private SearchName searchName;

  @Embedded
  @AttributeOverrides(
      @AttributeOverride(
          name = "value",
          column = @Column(name = "full_name", nullable = false, length = 255)))
  private FullName fullName;
}
