package undecided.association.relationshipMgmt.orgRole.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
  @Id private SnowflakeId id = SnowflakeId.EMPTY;

  @Column(unique = true, nullable = false, length = 13, name = "code")
  private DepartmentCode code = DepartmentCode.EMPTY;

  /**
   * 部署インスタンスを作成します。
   *
   * @param code 部署コード
   * @return 部署インスタンス
   */
  public static Department create(@NonNull DepartmentCode code) {
    if (code == null) {
      throw new IllegalArgumentException("code must not be null");
    }
    Department department = new Department();
    department.code = code;
    return department;
  }

  public Department update(@NonNull Department department) {
    return this;
  }
}
