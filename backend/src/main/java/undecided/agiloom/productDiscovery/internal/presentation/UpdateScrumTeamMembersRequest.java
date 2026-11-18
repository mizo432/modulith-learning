package undecided.agiloom.productDiscovery.internal.presentation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ScrumTeamのメンバー従業員IDを更新するリクエストDTOです。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScrumTeamMembersRequest {
  private List<SnowflakeId> memberEmployeeIds;
}
