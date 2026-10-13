package undecided.example;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import undecided.shared.primitive.Strings2;
import undecided.shared.value.StringCode;

/**
 * 文字列ベースの値オブジェクト（Value Object）テンプレートです。
 * <p>
 * このテンプレートは、{@link StringCode} インターフェースを実装する文字列型の値オブジェクトを作成するための
 * 標準的なパターンを提供します。プロジェクト内の {@code AccountNumber} や {@code HoldersName} などの
 * 実装がこのパターンに従っています。
 * <p>
 * 使用例:
 * <pre>{@code
 * @EqualsAndHashCode
 * public class SampleCode implements StringCode<SampleCode> {
 *
 *   public static final int MIN_LENGTH = 1;
 *   public static final int MAX_LENGTH = 0; // 必要に応じて設定
 *
 *   private final String value;
 *
 *   protected SampleCode() {
 *     value = null;
 *   }
 *
 *   private SampleCode(String value) {
 *     this.value = value;
 *   }
 *
 *   public static SampleCode of(String input) {
 *     if (input == null) {
 *       throw new IllegalArgumentException("input must not be null");
 *     }
 *     String trimmed = input.trim();
 *     if (trimmed.isEmpty()) {
 *       throw new IllegalArgumentException("input must not be empty");
 *     }
 *     // 必要に応じて追加のバリデーション（桁数、正規表現など）を追加
 *     return new SampleCode(trimmed);
 *   }
 *
 *   public static SampleCode empty() {
 *     return new SampleCode();
 *   }
 *
 *   @Override
 *   public String value() {
 *     return value;
 *   }
 *
 *   @Override
 *   public boolean isEmpty() {
 *     return Strings2.isEmpty(value);
 *   }
 *
 *   @Override
 *   public String toString() {
 *     return Strings2.toString(value);
 *   }
 *
 *   @Converter(autoApply = true)
 *   public static class SampleCodeConverter
 *       implements AttributeConverter<SampleCode, String> {
 *
 *     @Override
 *     public String convertToDatabaseColumn(SampleCode attribute) {
 *       return attribute != null ? attribute.value() : null;
 *     }
 *
 *     @Override
 *     public SampleCode convertToEntityAttribute(@Nullable String dbData) {
 *       if (dbData == null) {
 *         return SampleCode.empty();
 *       }
 *       return new SampleCode(dbData);
 *     }
 *   }
 * }
 * }</pre>
 * <p>
 * 規約要件:
 * 1. {@code StringCode<VO>} または {@code StringValue<VO>} インターフェースを実装
 * 2. {@code @EqualsAndHashCode} (Lombok) を付与して値の等価性を保証
 * 3. コンストラクタは {@code private} にし、ファクトリメソッド {@code of()} でバリデーションを実行
 * 4. {@code protected} の引数なしコンストラクタを定義して JPA プロキシ対応
 * 5. {@code empty()} ファクトリメソッドを提供して空の値オブジェクトを作成
 * 6. {@code isEmpty()} メソッドで空チェックを実装（{@code Strings2.isEmpty()} を使用）
 * 7. {@code toString()} メソッドで {@code Strings2.toString()} を使用
 * 8. {@code @Converter(autoApply = true)} を付与した内部クラスで JPA コンバーターを実装
 * 9. バリデーションでは {@code IllegalArgumentException} をスロー
 * 10. 定数（{@code MIN_LENGTH}、{@code MAX_LENGTH} など）で制約を明示
 */
@EqualsAndHashCode
public class StringValueObjectTemplate implements StringCode<StringValueObjectTemplate> {

  /** 値の最小文字数（必要に応じて設定）。 */
  public static final int MIN_LENGTH = 1;

  /** 値の最大文字数（必要に応じて設定）。 */
  public static final int MAX_LENGTH = 50;

  private final String value;

  /** JPA プロキシ用の protected コンストラクタ。 */
  protected StringValueObjectTemplate() {
    value = null;
  }

  /**
   * 値を保持する private コンストラクタ。
   *
   * @param value 値オブジェクトが保持する文字列
   */
  private StringValueObjectTemplate(String value) {
    this.value = value;
  }

  /**
   * 文字列から値オブジェクトを作成するファクトリメソッドです。
   * <p>
   * バリデーションに失敗した場合は {@link IllegalArgumentException} をスローします。
   *
   * @param input 値オブジェクトを作成する文字列（null不可、空不可）
   * @return 値オブジェクトのインスタンス
   * @throws IllegalArgumentException 入力がnull、空、または制約に違反している場合
   */
  public static StringValueObjectTemplate of(String input) {
    if (input == null) {
      throw new IllegalArgumentException("input must not be null");
    }
    String trimmed = input.trim();
    if (trimmed.isEmpty()) {
      throw new IllegalArgumentException("input must not be empty");
    }
    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException(
          "input must be between "
              + MIN_LENGTH
              + " and "
              + MAX_LENGTH
              + " characters, but was: "
              + trimmed.length());
    }
    // 必要に応じて正規表現などの追加バリデーションを追加
    // if (!trimmed.matches(REGEX)) {
    //   throw new IllegalArgumentException("input contains invalid characters");
    // }
    return new StringValueObjectTemplate(trimmed);
  }

  /**
   * 空の値オブジェクトを表すインスタンスを返します。
   *
   * @return 空の値オブジェクトインスタンス
   */
  public static StringValueObjectTemplate empty() {
    return new StringValueObjectTemplate();
  }

  /**
   * 値オブジェクトが保持する文字列を返します。
   *
   * @return 保持されている文字列
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * 値オブジェクトが空かどうかを判定します。
   *
   * @return 空である場合は {@code true}、そうでない場合は {@code false}
   */
  @Override
  public boolean isEmpty() {
    return Strings2.isEmpty(value);
  }

  /**
   * 値オブジェクトの文字列表現を返します。
   *
   * @return 値オブジェクトの文字列表現
   */
  @Override
  public String toString() {
    return Strings2.toString(value);
  }

  /**
   * 値オブジェクトとデータベースの文字列を相互に変換するための JPA コンバーターです。
   * <p>
   * {@code @Converter(autoApply = true)} を付与することで、エンティティ内でこの値オブジェクトを使用する際に
   * 明示的な {@code @Convert} アノテーションが不要になります。
   */
  @Converter(autoApply = true)
  public static class StringValueObjectTemplateConverter
      implements AttributeConverter<StringValueObjectTemplate, String> {

    @Override
    public String convertToDatabaseColumn(StringValueObjectTemplate attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public StringValueObjectTemplate convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return StringValueObjectTemplate.empty();
      }
      return new StringValueObjectTemplate(dbData);
    }
  }
}
