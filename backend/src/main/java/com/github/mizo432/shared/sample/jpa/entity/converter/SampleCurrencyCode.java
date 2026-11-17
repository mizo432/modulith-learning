package com.github.mizo432.shared.sample.jpa.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;

/**
 * 通貨コード（CurrencyCode）を表す値オブジェクトです。
 *
 * <p>{@code @Converter}を使用して、値オブジェクトとデータベースのString型間の 双方向変換を定義するパターンを示します。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>Converterは値オブジェクトクラス内に{@code static nested class}として定義
 *   <li>{@code autoApply = true}でグローバルに自動適用
 *   <li>変換先は主に{@code String}型
 * </ul>
 */
@EqualsAndHashCode
public class SampleCurrencyCode {

  /** 空の通貨コード */
  public static final SampleCurrencyCode EMPTY = new SampleCurrencyCode("");

  /** 通貨コードの値（例: "JPY", "USD", "EUR"） */
  private final String value;

  /** JPA用のデフォルトコンストラクタ */
  public SampleCurrencyCode() {
    this("");
  }

  /**
   * 通貨コードインスタンスを作成します。
   *
   * @param value 通貨コードの値
   */
  private SampleCurrencyCode(String value) {
    this.value = value;
  }

  /** 通貨コードインスタンスを作成します。 */
  public static SampleCurrencyCode of(String value) {
    if (value == null || value.isEmpty()) {
      return EMPTY;
    }
    return new SampleCurrencyCode(value.toUpperCase());
  }

  /** 空のインスタンスを返します。 */
  public static SampleCurrencyCode empty() {
    return EMPTY;
  }

  /** 通貨コードの値を返します。 */
  public String getValue() {
    return value;
  }

  /** 空かどうかを判定します。 */
  public boolean isEmpty() {
    return value == null || value.isEmpty();
  }

  @Override
  public String toString() {
    return value;
  }

  /**
   * SampleCurrencyCodeとデータベースのStringを相互に変換するためのコンバーターです。
   *
   * <p>{@code autoApply = true}により、この値オブジェクト型が使用されるたびに 自動的に変換が適用されます。
   */
  @Converter(autoApply = true)
  public static class SampleCurrencyCodeConverter
      implements AttributeConverter<SampleCurrencyCode, String> {

    @Override
    public String convertToDatabaseColumn(SampleCurrencyCode attribute) {
      return attribute != null ? attribute.value : null;
    }

    @Override
    public SampleCurrencyCode convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return SampleCurrencyCode.empty();
      }
      return new SampleCurrencyCode(dbData);
    }
  }
}
