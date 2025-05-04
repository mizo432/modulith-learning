package undecided.erp.projectActivity.domain.model.project;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ProjectAttribute {

  public static final ProjectAttribute EMPTY = new ProjectAttribute();
  private final ProjectName name;
  private final ProjectCode code;

  ProjectAttribute() {
    this.name = ProjectName.EMPTY;
    this.code = ProjectCode.EMPTY;

  }

  public static ProjectAttribute of(@NonNull ProjectName name, @NonNull ProjectCode code) {
    return new ProjectAttribute(name, code);

  }

}
