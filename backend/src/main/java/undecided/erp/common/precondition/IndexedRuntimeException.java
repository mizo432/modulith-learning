package undecided.erp.common.precondition;

import lombok.Getter;

@Getter
public class IndexedRuntimeException extends RuntimeException {

  Integer index;

  public IndexedRuntimeException(Integer index, Throwable cause) {
    super(cause);
    this.index = index;
  }

  public IndexedRuntimeException(String message, Integer index) {
    super(String.format("Index:%d %s", index, message));
    this.index = index;
  }

  public IndexedRuntimeException(String message, Integer index, Throwable cause) {
    super(String.format("Index:%d %s", index, message), cause);
    this.index = index;
  }
}
