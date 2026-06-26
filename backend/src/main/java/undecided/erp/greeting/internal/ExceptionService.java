package undecided.erp.greeting.internal;

public interface ExceptionService {

  RuntimeException throwDangerException();

  RuntimeException throwSystemException();

  RuntimeException throwDarkException();

  RuntimeException throwPrimaryException();

  RuntimeException throwLightException();

  RuntimeException throwSecondaryException();

  RuntimeException throwErrorException();
}
