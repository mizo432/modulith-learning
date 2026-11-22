package undecided.notification.internal;

import java.util.Set;
import undecided.supporting.snowflake.SnowflakeId;

/** 通知領域に保持するスクラムチームメンバーの参照。 */
interface NotificationScrumTeamMemberRepository {

  /** PO、SMを含む指定チームのメンバーと通知先を取得する。 */
  Set<NotificationScrumTeamMember> findByScrumTeamId(SnowflakeId scrumTeamId);
}