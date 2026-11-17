package com.github.mizo432.shared.sample.jpa.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 住所（Address）を表す埋め込み可能クラスです。
 *
 * <p>{@code @Embeddable}アノテーションを使用して、 このクラスをエンティティに埋め込んでマッピングできます。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>個別のテーブルを持たず、エンティティのテーブルにカラムとして展開される
 *   <li>{@code class}型での@Embeddableの例
 *   <li>不変な値オブジェクトとして設計
 * </ul>
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SampleAddress {

  /** 国 */
  @Column(name = "address_country", length = 50)
  private String country;

  /** 都道府県 */
  @Column(name = "address_prefecture", length = 50)
  private String prefecture;

  /** 市区町村 */
  @Column(name = "address_city", length = 100)
  private String city;

  /** 番地 */
  @Column(name = "address_street", length = 200)
  private String street;

  /** 住所インスタンスを作成します。 */
  public static SampleAddress of(String country, String prefecture, String city, String street) {
    return new SampleAddress(country, prefecture, city, street);
  }

  public String getCountry() {
    return country;
  }

  public String getPrefecture() {
    return prefecture;
  }

  public String getCity() {
    return city;
  }

  public String getStreet() {
    return street;
  }

  @Override
  public String toString() {
    return country + " " + prefecture + " " + city + " " + street;
  }
}
