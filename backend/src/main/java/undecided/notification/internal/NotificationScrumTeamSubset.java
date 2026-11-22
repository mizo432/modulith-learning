package undecided.notification.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.agiloom.productDiscovery.spi.ScrumTeamChanged;
import undecided.supporting.snowflake.SnowflakeId;

/** 通知領域に保持するスクラムチームのメンバースナップショット。 */
@Entity
@Table(schema = "undecided_nt", name = "scrum_team_subsets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class NotificationScrumTeamSubset {

  @Id
  @Column(nullable = false)
  private SnowflakeId id;

  @Column(name = "member_employee_ids", nullable = false, columnDefinition = "BIGINT[]")
  @Convert(converter = SnowflakeId.SnowflakeIdListConverter.class)
  private List<SnowflakeId> memberEmployeeIds = new ArrayList<>();

  @Version
  @Column(nullable = false)
  private Long version;

  static NotificationScrumTeamSubset from(ScrumTeamChanged event) {
    NotificationScrumTeamSubset subset = new NotificationScrumTeamSubset();
    subset.id = event.scrumTeamId();
    subset.apply(event);
    return subset;
  }

  void apply(ScrumTeamChanged event) {
    Set<SnowflakeId> members = new LinkedHashSet<>();
    if (event.memberEmployeeIds() != null) {
      event.memberEmployeeIds().stream()
          .filter(id -> id != null && !id.isEmpty())
          .forEach(members::add);
    }
    if (event.poEmployeeId() != null && !event.poEmployeeId().isEmpty()) {
      members.add(event.poEmployeeId());
    }
    if (event.smEmployeeId() != null && !event.smEmployeeId().isEmpty()) {
      members.add(event.smEmployeeId());
    }
    memberEmployeeIds = new ArrayList<>(members);
  }
}