package undecided.erp.common.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.common.primitive.Arrays2;

@Getter
public class ResultMessage {

  /**
   * empty array object
   */
  private static final Object[] EMPTY_ARRAY = new Object[0];

  /**
   * message code -- GETTER -- returns code.
   */
  private final String code;

  /**
   * message arguments -- GETTER -- returns args
   */
  private final Object[] args;

  /**
   * message text -- GETTER -- returns text.
   */
  private final String text;

  /**
   * Constructor.<br>
   *
   * @param code message code
   * @param args replacement values of message format
   * @param text default message
   */
  public ResultMessage(String code, Object[] args, String text) {
    this.code = code;
    this.args = args == null ? EMPTY_ARRAY : args;
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
    return new ResultMessage(null, EMPTY_ARRAY, text);

  }

  /**
   * returns the hash code
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays2.hash(args);
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((text == null) ? 0 : text.hashCode());
    return result;
  }

  /**
   * Returns whether the {@code obj} is equal to current instance of {@code ResultMessage}
   * <p>
   * Returns true if: <br>
   * <ul>
   * <li>the obj is the same instance as the current one. OR</li>
   * <li>if code AND text of the two instances are same respectively (including null values).</li>
   * </ul>
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ResultMessage other = (ResultMessage) obj;
    if (code == null) {
      if (other.code != null) {
        return false;
      }
    } else if (!code.equals(other.code)) {
      return false;
    }
    if (!Arrays2.equal(args, other.args)) {
      return false;
    }
    if (text == null) {
      if (other.text != null) {
        return false;
      }
    } else if (!text.equals(other.text)) {
      return false;
    }
    return true;
  }

  /**
   * Outputs code and text in the {@code toString()} method of {@code ResultMessage}
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ResultMessage [code=" + code + ", args=" + Arrays2.toString(args)
        + ", text=" + text + "]";
  }

  /**
   * 指定された出力ストリームにオブジェクトを書き込みます。
   *
   * @param out オブジェクトを書き込む出力ストリーム (nullであってはならない)
   * @throws IOException オブジェクトの書き込み中にI/Oエラーが発生した場合
   */
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

  /**
   * 与えられた入力ストリームからオブジェクトを読み込みます。
   * <p>
   * このメソッドはオブジェクトのデシリアライゼーションを責任を持ちます。
   *
   * @param in オブジェクトを読み込むための入力ストリーム（nullであってはなりません）
   * @throws IOException オブジェクトの読み取り中にI/Oエラーが発生した場合
   * @throws ClassNotFoundException 読み込まれるオブジェクトのクラスが見つからない場合
   */
  private void readObject(
      ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }
}
