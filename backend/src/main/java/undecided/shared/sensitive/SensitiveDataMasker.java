package undecided.shared.sensitive;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;

/**
 * 機微な個人情報をマスクするためのユーティリティクラスです。
 *
 * <p>ログ出力時に氏名、メールアドレス、電話番号、生年月日などの機微な情報を
 * 自動的にマスクし、個人情報の漏洩を防ぎます。
 */
public final class SensitiveDataMasker {

  /**
   * メールアドレスの正規表現パターンです。
   * ローカルパートとドメインパートを識別してマスクします。
   */
  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

  /**
   * 電話番号の正規表現パターンです。
   * 0から始まる10桁または11桁の数字列にマッチします。
   */
  private static final Pattern PHONE_PATTERN = Pattern.compile("0\\d{9,10}");

  private SensitiveDataMasker() {
    // ユーティリティクラスなのでインスタンス化しない
  }

  /**
   * 氏名をマスクします。最初の文字のみ表示し、残りをアスタリスクで置き換えます。
   *
   * @param name マスク対象の氏名。{@code null} または空文字列の可能性があります。
   * @return マスクされた氏名。{@code null} 入力の場合は {@code null}、空文字列の場合は空文字列、
   *         1文字の場合はそのまま返します。
   * @example "太郎" → "太*", "田中" → "田*"
   */
  public static @Nullable String maskName(@Nullable String name) {
    if (name == null || name.length() <= 1) {
      return name;
    }
    return name.charAt(0) + "*";
  }

  /**
   * メールアドレスをマスクします。ドメイン前のローカルパートの最初の文字のみ表示し、残りを "***" に置き換えます。
   *
   * @param email マスク対象のメールアドレス。{@code null} または空文字列の可能性があります。
   * @return マスクされたメールアドレス。{@code null} 入力の場合は {@code null}、
   *         空文字列の場合は空文字列、@" が含まれない場合はそのまま返します。
   * @example "taro@example.com" → "t***@example.com"
   */
  public static @Nullable String maskEmail(@Nullable String email) {
    if (email == null || email.isEmpty()) {
      return email;
    }
    int atIndex = email.indexOf('@');
    if (atIndex <= 0) {
      return email;
    }
    return email.charAt(0) + "***" + email.substring(atIndex);
  }

  /**
   * 電話番号をマスクします。下4桁のみ表示し、それより上の桁をアスタリスクで置き換えます。
   *
   * @param phone マスク対象の電話番号。{@code null} または空文字列の可能性があります。
   * @return マスクされた電話番号。{@code null} 入力の場合は {@code null}、
   *         空文字列の場合は空文字列、4桁以下の場合はアスタリスクのみ返します。
   * @example "09012345678" → "****5678"
   */
  public static @Nullable String maskPhone(@Nullable String phone) {
    if (phone == null || phone.isEmpty()) {
      return phone;
    }
    if (phone.length() <= 4) {
      return "****";
    }
    int asteriskCount = phone.length() - 4;
    return "*".repeat(asteriskCount) + phone.substring(phone.length() - 4);
  }

  /**
   * 生年月日をマスクします。年のみ表示し、月日を "**-**" に置き換えます。
   *
   * @param dateOfBirth マスク対象の生年月日。{@code null} の可能性があります。
   * @return マスクされた生年月日文字列。{@code null} 入力の場合は {@code null} を返します。
   * @example 1990-05-15 → "1990-**-**"
   */
  public static @Nullable String maskDateOfBirth(@Nullable LocalDate dateOfBirth) {
    if (dateOfBirth == null) {
      return null;
    }
    return dateOfBirth.getYear() + "-**-**";
  }

  /**
   * 任意の文字列に含まれる機微な個人情報（メールアドレス、電話番号）を自動的に検出してマスクします。
   *
   * <p>このメソッドは、文字列内に含まれるメールアドレスと電話番号を正規表現で検出し、
   * それぞれ {@link #maskEmail(String)} と {@link #maskPhone(String)} を使用してマスクします。
   *
   * @param text マスク対象の文字列。{@code null} または空文字列の可能性があります。
   * @return 機微な情報がマスクされた文字列。{@code null} 入力の場合は {@code null}、
   *         空文字列の場合は空文字列を返します。
   * @example "Contact taro@example.com or 09012345678" → "Contact t***@example.com or *******5678"
   */
  public static @Nullable String mask(@Nullable String text) {
    if (text == null || text.isEmpty()) {
      return text;
    }

    String result = text;

    // メールアドレスをマスク
    result = maskEmails(result);

    // 電話番号をマスク
    result = maskPhones(result);

    return result;
  }

  /**
   * 文字列内のすべてのメールアドレスをマスクします。
   *
   * @param text 対象の文字列
   * @return メールアドレスがマスクされた文字列
   */
  private static String maskEmails(String text) {
    Matcher matcher = EMAIL_PATTERN.matcher(text);
    StringBuffer stringBuffer = new StringBuffer();
    while (matcher.find()) {
      String email = matcher.group();
      String masked = maskEmail(email);
      matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(masked));
    }
    matcher.appendTail(stringBuffer);
    return stringBuffer.toString();
  }

  /**
   * 文字列内のすべての電話番号をマスクします。
   *
   * @param text 対象の文字列
   * @return 電話番号がマスクされた文字列
   */
  private static String maskPhones(String text) {
    Matcher matcher = PHONE_PATTERN.matcher(text);
    StringBuffer stringBuffer = new StringBuffer();
    while (matcher.find()) {
      String phone = matcher.group();
      String masked = maskPhone(phone);
      matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(masked));
    }
    matcher.appendTail(stringBuffer);
    return stringBuffer.toString();
  }
}
