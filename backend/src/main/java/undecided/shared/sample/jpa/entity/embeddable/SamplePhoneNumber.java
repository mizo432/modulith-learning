package undecided.shared.sample.jpa.entity.embeddable;

import jakarta.persistence.Embeddable;

/**
 * 電話番号（PhoneNumber）を表す埋め込み可能クラスです。
 *
 * <p>{@code record}型での{@code @Embeddable}の例です。 record型は不変性を持ち、値オブジェクトとして適しています。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>{@code record}型と{@code class}型の両方が@Embeddableで使用可能
 *   <li>個別のテーブルを持たず、エンティティのテーブルにカラムとして展開される
 * </ul>
 */
@Embeddable
public record SamplePhoneNumber(String value) {

  /** 空の電話番号 */
  public static final SamplePhoneNumber EMPTY = new SamplePhoneNumber("");

  /** 電話番号インスタンスを作成します。 */
  public static SamplePhoneNumber of(String value) {
    if (value == null) {
      return EMPTY;
    }
    return new SamplePhoneNumber(value);
  }
}
