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

/**
 * 複数の結果メッセージを管理するためのクラスです。
 *
 * <p>各インスタンスは複数の結果メッセージ（{@link ResultMessage}）とその種類 （{@link ResultMessageType}）を持ちます。
 * このクラスはメッセージの追加、取得、および反復処理を行うための機能を提供します。
 *
 * <p>スレッドセーフではありませんので、必要に応じて適切なスレッド同期を行ってください。
 */
@Getter
public class ResultMessages implements Serializable, Iterable<ResultMessage> {

  /**
   * {@code DEFAULT_MESSAGES_ATTRIBUTE_NAME} は、{@link ResultMessages} クラスに関する デフォルトのメッセージ属性名を表します。
   *
   * <p>このフィールドは、{@link ResultMessages} クラスの単純名を小文字化した値を持つ 静的な定数として定義されています。
   *
   * <p>主にフレームワークやツールで、このクラスが利用される際の属性名のデフォルト値として 使用されることを目的としています。
   */
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
   * ResultMessagesのコンストラクタです。
   *
   * <p>指定されたメッセージタイプを使用してResultMessagesのインスタンスを初期化します。
   * このコンストラクタではメッセージを指定しない場合に使用され、空のメッセージリストが初期化されます。
   *
   * @param type メッセージタイプ。この値はnullであってはいけません。
   */
  public ResultMessages(ResultMessageType type) {
    this(type, new ResultMessage[0]);
  }

  /**
   * 指定されたメッセージタイプと複数のメッセージを使用して、ResultMessagesのインスタンスを構築します。
   *
   * <p>メッセージタイプは必須であり、nullであってはなりません。メッセージ配列がnullでない場合、配列内のすべてのメッセージが追加されます。
   *
   * @param type メッセージタイプ。この値はnullであってはなりません。
   * @param messages ResultMessageの可変長配列。nullが指定されている場合は何も追加されません。
   * @throws IllegalArgumentException メッセージタイプがnullの場合
   */
  public ResultMessages(ResultMessageType type, ResultMessage... messages) {
    checkNotNull(type, () -> new IllegalArgumentException("type must not be null!"));
    this.type = type;
    if (nonNull(messages)) {
      addAll(messages);
    }
  }

  /**
   * 成功メッセージを生成するファクトリメソッドです。
   *
   * @return 成功メッセージのインスタンス
   */
  public static ResultMessages success() {
    return new ResultMessages(SUCCESS);
  }

  /**
   * 情報メッセージを生成するファクトリメソッドです。
   *
   * @return 情報メッセージのインスタンス
   */
  public static ResultMessages info() {
    return new ResultMessages(INFO);
  }

  /**
   * 警告メッセージを生成するファクトリメソッドです。
   *
   * @return 警告メッセージのインスタンス
   */
  public static ResultMessages warning() {
    return new ResultMessages(WARNING);
  }

  /**
   * エラーメッセージを生成するファクトリメソッドです。
   *
   * @return エラーメッセージのインスタンス
   */
  public static ResultMessages error() {
    return new ResultMessages(StandardResultMessageType.ERROR);
  }

  /**
   * 危険メッセージを生成するファクトリメソッドです。
   *
   * @return 危険メッセージのインスタンス
   */
  public static ResultMessages danger() {
    return new ResultMessages(DANGER);
  }

  /**
   * primaryメッセージを生成するファクトリメソッドです。
   *
   * @return primaryメッセージのインスタンス
   */
  public static ResultMessages primary() {
    return new ResultMessages(StandardResultMessageType.PRIMARY);
  }

  /**
   * secondaryメッセージを生成するファクトリメソッドです。
   *
   * @return secondaryメッセージのインスタンス
   */
  public static ResultMessages secondary() {
    return new ResultMessages(StandardResultMessageType.SECONDARY);
  }

  /**
   * lightメッセージを生成するファクトリメソッドです。
   *
   * @return lightメッセージのインスタンス
   */
  public static ResultMessages light() {
    return new ResultMessages(StandardResultMessageType.LIGHT);
  }

  /**
   * ダークメッセージを生成するファクトリメソッドです。
   *
   * @return ダークメッセージのインスタンス
   */
  public static ResultMessages dark() {
    return new ResultMessages(StandardResultMessageType.DARK);
  }

  /**
   * 指定された{@link ResultMessage}をリストに追加します。
   *
   * @param message リストに追加するメッセージ。この値はnullであってはいけません。
   * @return 自身のインスタンスを返します。
   * @throws IllegalArgumentException 引数がnullである場合
   */
  public ResultMessages add(ResultMessage message) {
    checkNotNull(message, () -> new IllegalArgumentException("message must not be null!"));
    this.list.add(message);
    return this;
  }

  /**
   * 指定されたメッセージコードを使用してResultMessageを生成し、リストに追加します。
   *
   * @param code メッセージコード。この値はnullであってはいけません。
   * @return 自身のインスタンスを返します。
   * @throws IllegalArgumentException メッセージコードがnullの場合
   */
  public ResultMessages add(String code) {
    checkNotNull(code, () -> new IllegalArgumentException("code must not be null!"));
    this.add(ResultMessage.fromCode(code));
    return this;
  }

  /**
   * 指定されたメッセージコードおよび関連する引数を使用して{@link ResultMessage}を生成し、メッセージリストに追加します。
   *
   * @param code メッセージコード。この値はnullであってはいけません。
   * @param args メッセージフォーマットの置換値。nullの場合は空の配列に置換されます。
   * @return 自身のインスタンスを返します。
   * @throws IllegalArgumentException メッセージコードがnullの場合
   */
  public ResultMessages add(String code, Object... args) {
    checkNotNull(code, () -> new IllegalArgumentException("code must not be null!"));
    this.add(ResultMessage.fromCode(code, args));
    return this;
  }

  /**
   * 複数の{@link ResultMessage}をリストに追加します。
   *
   * <p>指定されたメッセージ配列内の全ての{@link ResultMessage}を、このインスタンスに追加します。 引数がnullの場合、例外がスローされます。
   *
   * @param messages 追加する{@link ResultMessage}の可変長配列。この値はnullであってはいけません。
   * @return 自身のインスタンスを返します。
   * @throws IllegalArgumentException 引数がnullである場合
   */
  public ResultMessages addAll(ResultMessage... messages) {
    checkNotNull(messages, () -> new IllegalArgumentException("messages must not be null!"));
    for (ResultMessage message : messages) {
      add(message);
    }
    return this;
  }

  /**
   * 指定された{@link ResultMessage}のコレクションをリストに追加します。
   *
   * <p>渡されたコレクション内のすべてのメッセージがこのインスタンスに追加されます。コレクションがnullの場合、 {@link
   * IllegalArgumentException}がスローされます。
   *
   * @param messages 追加する{@link ResultMessage}のコレクション。この値はnullであってはいけません。
   * @return 自身のインスタンスを返します。
   * @throws IllegalArgumentException 引数がnullの場合
   */
  public ResultMessages addAll(Collection<ResultMessage> messages) {
    checkNotNull(messages, () -> new IllegalArgumentException("messages must not be null!"));
    for (ResultMessage message : messages) {
      add(message);
    }
    return this;
  }

  /**
   * リストが空でない場合にtrueを返します。
   *
   * @return リストが空でない場合はtrue、空の場合はfalse
   */
  public boolean isNotEmpty() {
    return !list.isEmpty();
  }

  /**
   * このクラス内に保持されている{@link ResultMessage}のイテレータを返します。
   *
   * @return {@link ResultMessage}を順番に走査するためのイテレータ
   */
  @Override
  @NonNull
  public Iterator<ResultMessage> iterator() {
    return list.iterator();
  }

  /**
   * このメソッドは、ResultMessagesオブジェクトの文字列表現を返します。
   *
   * @return メッセージタイプおよびメッセージリストを含む、このオブジェクトの文字列表現
   */
  @Override
  public String toString() {
    return "ResultMessages [type=" + type + ", list=" + list + "]";
  }

  /**
   * このメソッドは、`ResultMessages` オブジェクトのシリアライズ時に呼び出され、 オブジェクトのフィールドを `ObjectOutputStream` に書き込む処理を行います。
   *
   * @param out オブジェクトの状態を保存するための {@code ObjectOutputStream}
   * @throws IOException ストリームへの書き込み中にI/Oエラーが発生した場合
   */
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
  }

  /**
   * ResultMessagesオブジェクトを復元する際に呼び出されるメソッドです。
   *
   * <p>このメソッドは、シリアライズされたデータストリームを読み取り、 オブジェクトのフィールドを復元します。
   *
   * @param in オブジェクトの状態を復元するための {@code ObjectInputStream}
   * @throws IOException 入力ストリームの読み込みでI/Oエラーが発生した場合
   * @throws ClassNotFoundException 復元中にクラスが見つからない場合
   */
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
  }
}
