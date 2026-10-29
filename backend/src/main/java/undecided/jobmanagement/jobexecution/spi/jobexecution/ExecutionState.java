package undecided.jobmanagement.jobexecution.spi.jobexecution;

public enum ExecutionState {
  PENDING,
  RUNNING,
  SUCCESS,
  FAILED,
  UNKNOWN,
  RE_EXECUTED,
  RESUMED,
  CANCELLED
}
