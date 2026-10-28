package undecided.jobMgmt.permissionManagement.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionRequest {

  private String userId;
  private String userName;
  private String permissionLevel;
  private String systemPermission;
}
