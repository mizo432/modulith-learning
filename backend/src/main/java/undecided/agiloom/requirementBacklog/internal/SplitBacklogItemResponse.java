package undecided.agiloom.requirementBacklog.internal;

import java.util.List;

record SplitBacklogItemResponse(BacklogItemResponse parent, List<BacklogItemResponse> children) {}