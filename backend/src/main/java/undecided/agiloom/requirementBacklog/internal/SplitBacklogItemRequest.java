package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import undecided.supporting.snowflake.SnowflakeId;

record SplitBacklogItemRequest(
    SnowflakeId actorEmployeeId, List<SplitBacklogItemChildRequest> children) {}