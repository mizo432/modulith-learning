package undecided.shared.exception;

public class IORuntimeException extends SystemException {
  public IORuntimeException(String code, String message, Throwable cause) {
    super(code, message, cause);
  }
}
