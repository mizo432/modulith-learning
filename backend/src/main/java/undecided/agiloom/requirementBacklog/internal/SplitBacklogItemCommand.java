package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import undecided.supporting.snowflake.SnowflakeId;

record SplitBacklogItemCommand(
    SnowflakeId backlogItemId, SnowflakeId actorEmployeeId, List<Child> children) {
  record Child(String title, Integer storyPoint) {}
}