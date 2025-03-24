package undecided.erp.common.exception;


public interface ExceptionLevelResolver {

  ExceptionLevel resolveExceptionLevel(Exception exception);
}
