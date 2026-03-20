package undecided.shared.common.message;

import static undecided.shared.common.message.StandardResultMessageType.DANGER;
import static undecided.shared.common.message.StandardResultMessageType.INFO;
import static undecided.shared.common.message.StandardResultMessageType.SUCCESS;
import static undecided.shared.common.message.StandardResultMessageType.WARNING;
import static undecided.shared.common.precondition.ObjectPrecondition.checkNotNull;
import static undecided.shared.common.primitiveOld.Lists2.newArrayList;
import static undecided.shared.common.primitiveOld.Objects2.nonNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import undecided.shared.common.primitiveOld.Strings2;

@Getter
public class ResultMessages implements Serializable, Iterable<ResultMessage> {

  /** default attribute name for ResultMessages */
  public static final String DEFAULT_MESSAGES_ATTRIBUTE_NAME =
      Strings2.uncapitalize(ResultMessages.class.getSimpleName());

  /**
   * 結果メッセージの種類を表します。
   *
   * <p>この変数は{@link ResultMessageType}のインスタンスを保持し、関連付けられた {@link
   * ResultMessages}インスタンスに含まれるメッセージの種類（例: 成功、情報、警告、エラー） を定義します。
   *
   * <p>{@code private final}として宣言されているため、この変数は不変であり、 {@link
   * ResultMessages}の対応するコンストラクタを通じてオブジェクトの構築時に 初期化する必要があります。
   */
  private final ResultMessageType type;

  /**
   * {@link ResultMessage} オブジェクトのリストを表します。
   *
   * <p>このリストは、複数の結果メッセージを格納して管理するために使用されます。 初期状態では空の ArrayList として初期化され、直接変更することはできません。
   */
  private final List<ResultMessage> list = newArrayList();

  /**
   * Constructor.
   *
   * @param type message type
   */
  public ResultMessages(ResultMessageType type) {
    this(type, new ResultMessage[0]);
  }

  /**
   * Constructor.
   *
   * @param type message type
   * @param messages messages to add
   */
  public ResultMessages(ResultMessageType type, ResultMessage... messages) {
    checkNotNull(type, () -> new IllegalArgumentException("type must not be null!"));
    this.type = type;
    if (nonNull(messages)) {
      addAll(messages);
    }
  }

  /**
   * factory method for success messages.
   *
   * @return success messages
   */
  public static ResultMessages success() {
    return new ResultMessages(SUCCESS);
  }

  /**
   * factory method for info messages.
   *
   * @return info messages
   */
  public static ResultMessages info() {
    return new ResultMessages(INFO);
  }

  /**
   * factory method for warning messages.
   *
   * @return warning messages
   */
  public static ResultMessages warning() {
    return new ResultMessages(WARNING);
  }

  /**
   * factory method for error messages.
   *
   * @return error messages
   */
  public static ResultMessages error() {
    return new ResultMessages(StandardResultMessageType.ERROR);
  }

  /**
   * factory method for danger messages.
   *
   * @return danger messages
   */
  public static ResultMessages danger() {
    return new ResultMessages(DANGER);
  }

  /**
   * factory method for primary messages.
   *
   * @return primary messages
   */
  public static ResultMessages primary() {
    return new ResultMessages(StandardResultMessageType.PRIMARY);
  }

  /**
   * factory method for secondary messages.
   *
   * @return secondary messages
   */
  public static ResultMessages secondary() {
    return new ResultMessages(StandardResultMessageType.SECONDARY);
  }

  /**
   * factory method for light messages.
   *
   * @return light messages
   */
  public static ResultMessages light() {
    return new ResultMessages(StandardResultMessageType.LIGHT);
  }

  /**
   * factory method for dark messages.
   *
   * @return dark messages
   */
  public static ResultMessages dark() {
    return new ResultMessages(StandardResultMessageType.DARK);
  }

  /**
   * add a ResultMessage
   *
   * @param message ResultMessage instance
   * @return this result messages
   */
  public ResultMessages add(ResultMessage message) {
    checkNotNull(message, () -> new IllegalArgumentException("message must not be null!"));
    this.list.add(message);
    return this;
  }

  /**
   * add code to create and add ResultMessages
   *
   * @param code message code
   * @return this result messages
   */
  public ResultMessages add(String code) {
    checkNotNull(code, () -> new IllegalArgumentException("code must not be null!"));
    this.add(ResultMessage.fromCode(code));
    return this;
  }

  /**
   * add code and args to create and add ResultMessages
   *
   * @param code message code
   * @param args replacement values of message format
   * @return this result messages
   */
  public ResultMessages add(String code, Object... args) {
    checkNotNull(code, () -> new IllegalArgumentException("code must not be null!"));
    this.add(ResultMessage.fromCode(code, args));
    return this;
  }

  /**
   * add all messages (excludes <code>null</code> message)<br>
   *
   * <p>if <code>messages</code> is <code>null</code>, no message is added.
   *
   * @param messages messages to add
   * @return this messages
   */
  public ResultMessages addAll(ResultMessage... messages) {
    checkNotNull(messages, () -> new IllegalArgumentException("messages must not be null!"));
    for (ResultMessage message : messages) {
      add(message);
    }
    return this;
  }

  /**
   * add all messages (excludes <code>null</code> message)<br>
   *
   * <p>if <code>messages</code> is <code>null</code>, no message is added.
   *
   * @param messages messages to add
   * @return this messages
   */
  public ResultMessages addAll(Collection<ResultMessage> messages) {
    checkNotNull(messages, () -> new IllegalArgumentException("messages must not be null!"));
    for (ResultMessage message : messages) {
      add(message);
    }
    return this;
  }

  /**
   * returns whether messages are not empty.
   *
   * @return whether messages are not empty
   */
  public boolean isNotEmpty() {
    return !list.isEmpty();
  }

  /**
   * Returns {@link Iterator} instance that iterates over a list of {@link ResultMessage}
   *
   * @see java.lang.Iterable#iterator()
   */
  @Override
  @NonNull
  public Iterator<ResultMessage> iterator() {
    return list.iterator();
  }

  /**
   * Outputs type of messages in this {@code ResultMessages} and the list of messages itself
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ResultMessages [type=" + type + ", list=" + list + "]";
  }

  /**
   * special handling for the serialization and deserialization process
   *
   * @param out ObjectOutputStream
   * @throws IOException see {@link java.io.ObjectOutputStream#defaultWriteObject()}
   * @see java.io.Serializable
   */
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

  /**
   * {@code ResultMessages} オブジェクトのデシリアライズを処理します。
   *
   * @param in オブジェクトの状態を読み取るための {@code ObjectInputStream}
   * @throws IOException ストリームから読み取る際にI/Oエラーが発生した場合
   * @throws ClassNotFoundException シリアライズされたオブジェクトのクラスが見つからない場合
   */
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }
}
