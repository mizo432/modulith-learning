package undecided.erp.common.exception;

public interface ExceptionCodeResolver {

  String resolveExceptionCode(Exception exception);
}
