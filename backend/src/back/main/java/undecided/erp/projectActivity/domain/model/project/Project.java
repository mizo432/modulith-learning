package undecided.erp.projectActivity.domain.model.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import undecided.erp.shared.entity.SnowflakeId;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Project {

  /**
   * この変数は、Snowflakeアルゴリズムを使用して一意の識別子を表します。 通常、Projectクラスのフィールドとして使用されます。
   */
  private SnowflakeId id;

  private ProjectAttribute attribute;

  public static Project create(@NonNull ProjectAttribute attribute) {
    return new Project(SnowflakeId.newInstance(), attribute);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Project that) {
      return this.id.equals(that.id);
    }
    return false;
  }
}
