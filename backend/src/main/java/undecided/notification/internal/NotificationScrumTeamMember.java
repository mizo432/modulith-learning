package undecided.notification.internal;

import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/** 分割通知の対象となるスクラムチームメンバー。 */
record NotificationScrumTeamMember(SnowflakeId employeeId, @Nullable String email) {}