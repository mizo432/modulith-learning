package undecided.erp.common.message;

import java.util.Arrays;
import lombok.NonNull;
import undecided.erp.common.primitive.Objects2;

public record ResultMessage(String code, Object[] args, String text) {

  /**
   * Constructor.
   *
   * @param code message code
   * @param args replacement values of message format
   * @param text default message
   */
  public ResultMessage(String code, Object[] args, String text) {
    this.code = code;
    this.args = args == null ? Objects2.EMPTY_ARRAY : args;
    this.text = text;
  }

  /**
   * create <code>ResultMessage</code> instance which has the given code and args<br>
   * <p>
   * <code>text</code> is <code>null</code>
   * </p>
   *
   * @param code message code (must not be null)
   * @param args replacement values of message format
   * @return ResultMessage instance
   */
  public static ResultMessage fromCode(@NonNull String code, Object... args) {
    return new ResultMessage(code, args, null);
  }

  /**
   * create <code>ResultMessage</code> instance which has the given text<br>
   * <p>
   * <code>code</code> is <code>null</code>
   * </p>
   *
   * @param text message tet (must not be null)
   * @return ResultMessage instance
   */
  public static ResultMessage fromText(@NonNull String text) {
    return new ResultMessage(null, Objects2.EMPTY_ARRAY, text);

  }

  @Override
  public String toString() {
    return "ResultMessage{" +
        "code='" + code + '\'' +
        ", args=" + Arrays.toString(args) +
        ", text='" + text + '\'' +
        '}';
  }
}
