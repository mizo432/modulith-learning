package undecided.jobMgmt.permissionManagement.spi.userpermission;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ユーザーの権限を管理するルートエンティティです。
 */
@Entity
@Table(name = "user_permission")
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPermission {

  @jakarta.persistence.Id
  private SnowflakeId id;

  @Embedded
  private UserId userId;

  @Embedded
  private UserName userName;

  @Embedded
  private PermissionLevel permissionLevel;

  @Embedded
  private SystemPermission systemPermission;

  @jakarta.persistence.Column(name = "enabled", nullable = false)
  private boolean enabled;

  @jakarta.persistence.Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @jakarta.persistence.Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public UserPermission(UserId userId, UserName userName, PermissionLevel permissionLevel,
      SystemPermission systemPermission) {
    this.id = SnowflakeId.of(java.util.UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    this.userId = userId;
    this.userName = userName;
    this.permissionLevel = permissionLevel;
    this.systemPermission = systemPermission;
    this.enabled = true;
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  public void changePermissionLevel(PermissionLevel permissionLevel) {
    if (permissionLevel == null) {
      throw new IllegalArgumentException("Permission level must not be null");
    }
    this.permissionLevel = permissionLevel;
    this.updatedAt = LocalDateTime.now();
  }

  public void disable() {
    this.enabled = false;
    this.updatedAt = LocalDateTime.now();
  }

  public void enable() {
    this.enabled = true;
    this.updatedAt = LocalDateTime.now();
  }
}
