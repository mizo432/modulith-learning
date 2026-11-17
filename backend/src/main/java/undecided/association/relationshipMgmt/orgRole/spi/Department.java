package undecided.association.relationshipMgmt.orgRole.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 部署エンティティ。
 *
 * <p>組織内の部署を表します。部署コードによって一意に識別されます。
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department {
  /**
   * 部署を一意に識別するためのID。
   *
   * <p>用途: 部署情報の検索や更新で使用されます。
   */
  @Id protected SnowflakeId id = SnowflakeId.EMPTY;

  /**
   * 部署コード。
   *
   * <p>用途: 部署を一意に識別するためのコードです。
   */
  @Column(unique = true, nullable = false, length = 13, name = "code")
  protected DepartmentCode code = DepartmentCode.EMPTY;

  /**
   * 部署を新規作成します。
   *
   * @param id 部署ID
   * @param code 部署コード
   * @return 新規作成されたDepartmentインスタンス
   */
  public static Department create(SnowflakeId id, DepartmentCode code) {
    Department department = new Department();
    department.id = id;
    department.code = code;
    return department;
  }

  /**
   * 部署情報を更新します。
   *
   * @param department 更新後の部署情報
   * @return 更新された部署インスタンス
   */
  public Department update(@NonNull Department department) {
    return this;
  }
}
