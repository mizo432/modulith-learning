package undecided.jbJobMgmt.permissionManagement.presentation.dto;

import undecided.jbJobMgmt.permissionManagement.domain.userpermission.UserPermission;

public class UserPermissionResponse {

  private Long id;
  private String userId;
  private String userName;
  private String permissionLevel;
  private String systemPermission;
  private boolean enabled;

  public Long getId() { return id; }
  public String getUserId() { return userId; }
  public String getUserName() { return userName; }
  public String getPermissionLevel() { return permissionLevel; }
  public String getSystemPermission() { return systemPermission; }
  public boolean isEnabled() { return enabled; }

  public static UserPermissionResponse from(UserPermission up) {
    UserPermissionResponse response = new UserPermissionResponse();
    response.id = up.getId().value();
    response.userId = up.getUserId().getValue();
    response.userName = up.getUserName().getValue();
    response.permissionLevel = up.getPermissionLevel().getLevel();
    response.systemPermission = up.getSystemPermission().getPermission();
    response.enabled = up.isEnabled();
    return response;
  }
}
