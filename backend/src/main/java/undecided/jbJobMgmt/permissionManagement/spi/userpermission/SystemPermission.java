package undecided.jbJobMgmt.permissionManagement.spi.userpermission;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * システムごとの権限を表す値オブジェクトです。
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemPermission {

  @Column(name = "system_permission", nullable = false, length = 50)
  private String permission;

  public SystemPermission(String permission) {
    if (permission == null || permission.isBlank()) {
      throw new IllegalArgumentException("System permission must not be null or blank");
    }
    this.permission = permission;
  }
}
