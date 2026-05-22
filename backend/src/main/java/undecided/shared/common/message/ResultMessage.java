package undecided.shared.common.message;

import java.util.Arrays;
import org.jspecify.annotations.NonNull;
import undecided.shared.common.primitiveOld.Objects2;

/** メッセージを格納するためのクラス。メッセージコード、引数、デフォルトメッセージを保持します。 */
public record ResultMessage(String code, Object[] args, String text) {

  /**
   * ResultMessageのコンストラクタです。
   *
   * <p>指定されたメッセージコード、引数、メッセージテキストを用いて初期化します。
   *
   * @param code メッセージコード。この値はnullではいけません。
   * @param args メッセージフォーマットの置換値。nullが指定された場合は空の配列に置換されます。
   * @param text メッセージテキスト。この値は任意で指定可能です。
   */
  public ResultMessage(String code, Object[] args, String text) {
    this.code = code;
    this.args = args == null ? Objects2.EMPTY_ARRAY : args;
    this.text = text;
  }

  /**
   * 指定されたメッセージコードおよび可変長引数を使用して、ResultMessageのインスタンスを生成します。
   *
   * @param code メッセージコード。この値はnullではいけません。
   * @param args メッセージフォーマットの置換値。nullが指定された場合は空の配列に置換されます。
   * @return 生成されたResultMessageインスタンス
   */
  public static ResultMessage fromCode(@NonNull String code, Object... args) {
    return new ResultMessage(code, args, null);
  }

  /**
   * 指定されたテキストを使用してResultMessageのインスタンスを生成します。
   *
   * @param text メッセージテキスト。この値はnullではいけません。
   * @return 生成されたResultMessageインスタンス
   */
  public static ResultMessage fromText(@NonNull String text) {
    return new ResultMessage(null, Objects2.EMPTY_ARRAY, text);
  }

  /**
   * このメソッドはResultMessageオブジェクトの文字列表現を返します。
   *
   * @return メッセージコード、引数の配列、テキストを含むResultMessageオブジェクトの文字列表現
   */
  @Override
  @NonNull
  public String toString() {
    return "ResultMessage{"
        + "code='"
        + code
        + '\''
        + ", args="
        + Arrays.toString(args)
        + ", text='"
        + text
        + '\''
        + '}';
  }
}
