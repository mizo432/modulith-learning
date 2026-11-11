package undecided.association.relationshipMgmt.orgRole.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Getter
@NoArgsConstructor
public class Department {
  @Id private SnowflakeId id = SnowflakeId.EMPTY;

  @Column(unique = true, nullable = false, length = 13, name = "code")
  private DepartmentCode code = DepartmentCode.EMPTY;

  /** パッケージプライベートセッター (テスト用) */
  void setId(SnowflakeId id) {
    this.id = id;
  }

  /** パッケージプライベートセッター (テスト用) */
  void setCode(DepartmentCode code) {
    this.code = code;
  }

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

  /**
   * 部署情報を更新します。
   *
   * @param department 更新する部署情報
   * @return 更新された部署インスタンス
   */
  public Department update(@NonNull Department department) {
    if (department == null) {
      throw new IllegalArgumentException("department must not be null");
    }
    this.setCode(department.getCode());
    return this;
  }
}
