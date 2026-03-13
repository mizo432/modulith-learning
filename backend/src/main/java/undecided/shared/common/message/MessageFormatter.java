package undecided.shared.common.message;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;

/**
 * メッセージフォーマッタを提供するユーティリティクラスです。 指定されたメッセージコードおよびパラメータに基づいて、国際化されたメッセージ文字列を生成します。
 *
 * <p>使用されるリソースバンドルは、UTF-8 エンコーディングのプロパティファイルをサポートしています。 このクラスはインスタンス化できません。
 */
public final class MessageFormatter {

  /**
   * メッセージリソースを管理するための静的リソースバンドルです。 デフォルトのロケールに基づき、"messages" プロパティファイルから メッセージを読み込むよう設定されています。
   * このバンドルは UTF-8 エンコーディングに対応しているため、 非ASCII文字を含むプロパティファイルも正しく処理されます。
   *
   * <p>使用されるリソースバンドルは {@link UTF8Control} を通じてカスタマイズされており、 プロパティファイルのエンコーディングを指定しています。
   *
   * <p>このフィールドは変更不可能 (final) であり、 初期化時に一度だけ設定される静的 (static) リソースです。
   */
  private static final ResourceBundle bundle =
      ResourceBundle.getBundle("messages", Locale.getDefault(), new UTF8Control());

  /**
   * MessageFormatterクラスのコンストラクタです。
   *
   * <p>このコンストラクタはプライベートであり、このクラスのインスタンス化を禁止します。
   * MessageFormatterは静的メソッドと静的リソースのみを提供するユーティリティクラスとして設計されています。
   */
  private MessageFormatter() {}

  /**
   * 指定されたメッセージコードと引数に基づいて、国際化されたメッセージ文字列を生成します。 メッセージコードに対応するパターン文字列はリソースバンドルから取得され、
   * それに指定された引数を適用して整形された文字列を返します。
   *
   * @param code メッセージコード。リソースバンドルから対応するメッセージパターンを取得するために使用されます。
   * @param args メッセージパターン内のプレースホルダーを置き換えるための引数。可変長引数として指定可能です。
   * @return 指定されたコードおよび引数に基づいて整形されたメッセージ文字列。 リソースバンドルにコードが存在しない場合は {@link MissingResourceException}
   *     がスローされます。
   */
  public static String format(String code, Object... args) {
    String pattern = bundle.getString(code);
    MessageFormat mf = new MessageFormat(pattern);
    int expectedArgsCount = mf.getFormatsByArgumentIndex().length;
    if (args.length < expectedArgsCount) {
      throw new IllegalArgumentException(
          "Arguments for message "
              + code
              + " are missing. Expected: "
              + expectedArgsCount
              + ", but provided: "
              + args.length);
    }
    return mf.format(args);
  }

  /**
   * UTF-8エンコーディングでプロパティファイルからリソースバンドルを読み込むためのカスタムコントロールクラスです。
   * ResourceBundle.Controlを拡張することで、プロパティファイルのエンコーディングを明示的にUTF-8に設定します。
   *
   * <p>標準のResourceBundleではプロパティファイルがISO-8859-1として処理されますが、 このクラスを使用することで非ASCII文字を含むプロパティファイルを
   * 正しくUTF-8として読み込むことが可能です。
   *
   * <p>このクラスは、ルートリソースバンドル、および特定ロケールに基づくバンドルの管理を行います。
   *
   * <p>主に静的なメソッドを通じて利用され、直接インスタンス化されることは通常ありません。
   */
  static class UTF8Control extends ResourceBundle.Control {

    /**
     * 指定されたプロパティファイルをUTF-8エンコーディングで読み込み、リソースバンドルを作成します。
     *
     * @param baseName リソースバンドルの基底名
     * @param locale 対応するロケール
     * @param format リソースバンドルの形式 (例: "java.class" や "java.properties")
     * @param loader リソースの読み込みに使用するクラスローダー
     * @param reload リソースの再読み込み指示が必要かどうかを指定するフラグ
     * @return 読み込まれたリソースバンドル、リソースが見つからない場合は null
     * @throws RuntimeException 入出力例外が発生した場合
     */
    @Override
    public ResourceBundle newBundle(
        String baseName, Locale locale, String format, ClassLoader loader, boolean reload) {

      String bundleName = toBundleName(baseName, locale);
      String resourceName = toResourceName(bundleName, "properties");

      try (var stream = loader.getResourceAsStream(resourceName)) {
        if (stream == null) {
          return null;
        }
        return new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
