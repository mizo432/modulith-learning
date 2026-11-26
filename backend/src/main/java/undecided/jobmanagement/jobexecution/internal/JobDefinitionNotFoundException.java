package undecided.jobmanagement.jobexecution.internal;

public class JobDefinitionNotFoundException extends RuntimeException {

  public JobDefinitionNotFoundException(String message) {
    super(message);
  }
}
