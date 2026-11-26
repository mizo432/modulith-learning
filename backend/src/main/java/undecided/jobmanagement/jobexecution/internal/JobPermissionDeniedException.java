package undecided.jobmanagement.jobexecution.internal;

public class JobPermissionDeniedException extends RuntimeException {

  public JobPermissionDeniedException(String message) {
    super(message);
  }
}
