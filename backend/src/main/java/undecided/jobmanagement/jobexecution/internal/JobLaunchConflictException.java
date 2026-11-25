package undecided.jobmanagement.jobexecution.internal;

public class JobLaunchConflictException extends RuntimeException {

  public JobLaunchConflictException(String message) {
    super(message);
  }
}
