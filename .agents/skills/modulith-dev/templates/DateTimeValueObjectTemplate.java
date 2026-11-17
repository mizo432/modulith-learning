package undecided.example;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.dateProvider.DateProvider;
import com.github.mizo432.shared.value.DateTimeValue;

/**
 * 日付・時刻ベースの値オブジェクト（Value Object）テンプレートです。
 * <p>
 * このテンプレートは、{@link DateTimeValue} インターフェースを実装する日付・時刻型の値オブジェクトを作成するための
 * 標準的なパターンを提供します。プロジェクト内の {@code WhenOccured} などの実装がこのパターンに従っています。
 * <p>
 * 使用例:
 * <pre>{@code
 * @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
 * @EqualsAndHashCode
 * public class SampleDateTime implements DateTimeValue {
 *   private final LocalDateTime value;
 *
 *   public SampleDateTime() {
 *     value = null;
 *   }
 *
 *   public static SampleDateTime current() {
 *     return new SampleDateTime(DateProvider.currentLocalDateTime());
 *   }
 *
 *   public static SampleDateTime of(LocalDateTime dateTime) {
 *     if (dateTime == null) {
 *       throw new IllegalArgumentException("dateTime must not be null");
 *     }
 *     return new SampleDateTime(dateTime);
 *   }
 *
 *   public static SampleDateTime empty() {
 *     return new SampleDateTime();
 *   }
 *
 *   @Override
 *   public LocalDateTime value() {
 *     return value;
 *   }
 *
 *   @Override
 *   public String toString() {
 *     return String.valueOf(value);
 *   }
 *
 *   @Converter(autoApply = true)
 *   public static class SampleDateTimeConverter
 *       implements AttributeConverter<SampleDateTime, LocalDateTime> {
 *
 *     @Override
 *     public LocalDateTime convertToDatabaseColumn(SampleDateTime attribute) {
 *       return attribute != null ? attribute.value() : null;
 *     }
 *
 *     @Override
 *     public SampleDateTime convertToEntityAttribute(@Nullable LocalDateTime dbData) {
 *       if (dbData == null) {
 *         return SampleDateTime.empty();
 *       }
 *       return new SampleDateTime(dbData);
 *     }
 *   }
 * }
 * }</pre>
 * <p>
 * 規約要件:
 * 1. {@code DateTimeValue} インターフェースを実装
 * 2. {@code @RequiredArgsConstructor(access = AccessLevel.PRIVATE)} (Lombok) を付与
 * 3. {@code @EqualsAndHashCode} (Lombok) を付与して値の等価性を保証
 * 4. {@code protected} または public の引数なしコンストラクタを定義して JPA プロキシ対応
 * 5. {@code current()} ファクトリメソッドを提供して現在時刻の値オブジェクトを作成（{@code DateProvider} を使用）
 * 6. {@code of(LocalDateTime)} ファクトリメソッドを提供して指定された時刻の値オブジェクトを作成
 * 7. {@code empty()} ファクトリメソッドを提供して空の値オブジェクトを作成
 * 8. {@code toString()} メソッドで {@code String.valueOf(value)} を使用
 * 9. {@code @Converter(autoApply = true)} を付与した内部クラスで JPA コンバーターを実装
 * 10. バリデーションでは {@code IllegalArgumentException} をスロー
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class DateTimeValueObjectTemplate implements DateTimeValue {

  private final LocalDateTime value;

  /** JPA プロキシ用の引数なしコンストラクタ。 */
  public DateTimeValueObjectTemplate() {
    value = null;
  }

  /**
   * 現在時刻の値オブジェクトを作成するファクトリメソッドです。
   * <p>
   * {@link DateProvider} を使用することで、テスト時に時刻を固定することが可能です。
   *
   * @return 現在時刻の値オブジェクトインスタンス
   */
  public static DateTimeValueObjectTemplate current() {
    return new DateTimeValueObjectTemplate(DateProvider.currentLocalDateTime());
  }

  /**
   * 指定された日付・時刻から値オブジェクトを作成するファクトリメソッドです。
   *
   * @param dateTime 値オブジェクトを作成する日付・時刻（null不可）
   * @return 値オブジェクトのインスタンス
   * @throws IllegalArgumentException 入力がnullの場合
   */
  public static DateTimeValueObjectTemplate of(LocalDateTime dateTime) {
    if (dateTime == null) {
      throw new IllegalArgumentException("dateTime must not be null");
    }
    return new DateTimeValueObjectTemplate(dateTime);
  }

  /**
   * 空の値オブジェクトを表すインスタンスを返します。
   *
   * @return 空の値オブジェクトインスタンス
   */
  public static DateTimeValueObjectTemplate empty() {
    return new DateTimeValueObjectTemplate();
  }

  /**
   * 値オブジェクトが保持する日付・時刻を返します。
   *
   * @return 保持されている {@link LocalDateTime}
   */
  @Override
  public LocalDateTime value() {
    return value;
  }

  /**
   * 値オブジェクトの文字列表現を返します。
   *
   * @return 値オブジェクトの文字列表現
   */
  @Override
  public String toString() {
    return String.valueOf(value);
  }

  /**
   * 値オブジェクトとデータベースの {@link LocalDateTime} を相互に変換するための JPA コンバーターです。
   * <p>
   * {@code @Converter(autoApply = true)} を付与することで、エンティティ内でこの値オブジェクトを使用する際に
   * 明示的な {@code @Convert} アノテーションが不要になります。
   */
  @Converter(autoApply = true)
  public static class DateTimeValueObjectTemplateConverter
      implements AttributeConverter<DateTimeValueObjectTemplate, LocalDateTime> {

    @Override
    public LocalDateTime convertToDatabaseColumn(DateTimeValueObjectTemplate attribute) {
      return attribute != null ? attribute.value() : null;
    }

    @Override
    public DateTimeValueObjectTemplate convertToEntityAttribute(@Nullable LocalDateTime dbData) {
      if (dbData == null) {
        return DateTimeValueObjectTemplate.empty();
      }
      return new DateTimeValueObjectTemplate(dbData);
    }
  }
}
