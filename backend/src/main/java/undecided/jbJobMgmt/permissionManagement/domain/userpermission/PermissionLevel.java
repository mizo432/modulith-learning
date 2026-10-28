package undecided.jbJobMgmt.permissionManagement.domain.userpermission;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 権限レベルを表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PermissionLevel {

  @Column(name = "permission_level", nullable = false, length = 20)
  private String level;

  public PermissionLevel(String level) {
    if (level == null || level.isBlank()) {
      throw new IllegalArgumentException("Permission level must not be null or blank");
    }
    if (!level.equals("admin") && !level.equals("operator") && !level.equals("viewer")) {
      throw new IllegalArgumentException("Permission level must be admin, operator, or viewer");
    }
    this.level = level;
  }

  public boolean isAdmin() {
    return "admin".equals(level);
  }

  public boolean isOperator() {
    return "operator".equals(level);
  }

  public boolean isViewer() {
    return "viewer".equals(level);
  }
}
