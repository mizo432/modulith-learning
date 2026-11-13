package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;

/**
 * 住所を表す値オブジェクトです。
 *
 * <p>このクラスは {@link ContactBase} を継承し、住所を不変の値として保持します。
 * M2修正により、Builderパターンを導入し、19個の重载メソッドを削除しました。
 */
@EqualsAndHashCode(callSuper = true)
public class Address extends ContactBase {

  /** 住所の最大文字数。 */
  public static final int MAX_LENGTH = 200;

  /** 都道府県名の最大文字数。 */
  public static final int PREF_NAME_MAX_LENGTH = 10;

  /** 都道府県名かなの最大文字数。 */
  public static final int PREF_KANA_MAX_LENGTH = 15;

  /** 郵便番号の最大文字数。 */
  public static final int POSTAL_CODE_MAX_LENGTH = 8;

  /** 国名の最大文字数。 */
  public static final int COUNTRY_NAME_MAX_LENGTH = 50;

  /** 国名かなの最大文字数。 */
  public static final int COUNTRY_KANA_MAX_LENGTH = 50;

  /** 市区町村名の最大文字数。 */
  public static final int CITY_NAME_MAX_LENGTH = 20;

  /** 市区町村名かなの最大文字数。 */
  public static final int CITY_KANA_MAX_LENGTH = 20;

  /** 区名の最大文字数。 */
  public static final int WARD_NAME_MAX_LENGTH = 20;

  /** 区名かなの最大文字数。 */
  public static final int WARD_KANA_MAX_LENGTH = 20;

  /** 大字町名の最大文字数。 */
  public static final int OAZA_CHO_NAME_MAX_LENGTH = 20;

  /** 大字町名かなの最大文字数。 */
  public static final int OAZA_CHO_KANA_MAX_LENGTH = 20;

  /** 丁目名の最大文字数。 */
  public static final int CHOME_NAME_MAX_LENGTH = 20;

  /** 丁目名かなの最大文字数。 */
  public static final int CHOME_KANA_MAX_LENGTH = 20;

  /** 小字名の最大文字数。 */
  public static final int KOAZA_NAME_MAX_LENGTH = 20;

  /** 小字名かなの最大文字数。 */
  public static final int KOAZA_KANA_MAX_LENGTH = 20;

  /** 建物名の最大文字数。 */
  public static final int BUILDING_NAME_MAX_LENGTH = 50;

  /** 建物名かなの最大文字数。 */
  public static final int BUILDING_KANA_MAX_LENGTH = 50;

  /** 部屋番号の最大文字数。 */
  public static final int ROOM_NO_MAX_LENGTH = 20;

  private final ContactType type;

  /** 都道府県名。 */
  private final String prefName;

  /** 都道府県名かな。 */
  private final String prefKana;

  /** 郵便番号。 */
  private final String postalCode;

  /** 国名。 */
  private final String countryName;

  /** 国名かな。 */
  private final String countryKana;

  /** 市区町村名。 */
  private final String cityName;

  /** 市区町村名かな。 */
  private final String cityKana;

  /** 区名。 */
  private final String wardName;

  /** 区名かな。 */
  private final String wardKana;

  /** 大字町名。 */
  private final String oazaChoName;

  /** 大字町名かな。 */
  private final String oazaChoKana;

  /** 丁目名。 */
  private final String chomeName;

  /** 丁目名かな。 */
  private final String chomeKana;

  /** 小字名。 */
  private final String koazaName;

  /** 小字名かな。 */
  private final String koazaKana;

  /** 建物名。 */
  private final String buildingName;

  /** 建物名かな。 */
  private final String buildingKana;

  /** 部屋番号。 */
  private final String roomNo;

  /** null の値を持つインスタンスを作成します。 */
  public Address() {
    super();
    this.type = ContactType.ADDRESS;
    this.prefName = null;
    this.prefKana = null;
    this.postalCode = null;
    this.countryName = null;
    this.countryKana = null;
    this.cityName = null;
    this.cityKana = null;
    this.wardName = null;
    this.wardKana = null;
    this.oazaChoName = null;
    this.oazaChoKana = null;
    this.chomeName = null;
    this.chomeKana = null;
    this.koazaName = null;
    this.koazaKana = null;
    this.buildingName = null;
    this.buildingKana = null;
    this.roomNo = null;
  }

  private Address(String value, String prefName, String prefKana, String postalCode,
                  String countryName, String countryKana, String cityName, String cityKana,
                  String wardName, String wardKana, String oazaChoName, String oazaChoKana,
                  String chomeName, String chomeKana, String koazaName, String koazaKana,
                  String buildingName, String buildingKana, String roomNo) {
    super(value);
    this.type = ContactType.ADDRESS;
    this.prefName = prefName;
    this.prefKana = prefKana;
    this.postalCode = postalCode;
    this.countryName = countryName;
    this.countryKana = countryKana;
    this.cityName = cityName;
    this.cityKana = cityKana;
    this.wardName = wardName;
    this.wardKana = wardKana;
    this.oazaChoName = oazaChoName;
    this.oazaChoKana = oazaChoKana;
    this.chomeName = chomeName;
    this.chomeKana = chomeKana;
    this.koazaName = koazaName;
    this.koazaKana = koazaKana;
    this.buildingName = buildingName;
    this.buildingKana = buildingKana;
    this.roomNo = roomNo;
  }

  // ==================== Factory Methods ====================

  /**
   * 指定された文字列から {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address) {
    return builder().address(address).build();
  }

  /**
   * 指定された住所と都道府県名から {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName) {
    return builder().address(address).prefName(prefName).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かなから {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana) {
    return builder().address(address).prefName(prefName).prefKana(prefKana).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号から {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @param postalCode 郵便番号。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana, String postalCode) {
    return builder().address(address).prefName(prefName).prefKana(prefKana).postalCode(postalCode).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号、国名から {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @param postalCode 郵便番号。{@code null} 許容。
   * @param countryName 国名。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana, String postalCode, String countryName) {
    return builder().address(address).prefName(prefName).prefKana(prefKana)
        .postalCode(postalCode).countryName(countryName).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号、国名、国名かなから {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @param postalCode 郵便番号。{@code null} 許容。
   * @param countryName 国名。{@code null} 許容。
   * @param countryKana 国名かな。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana, String postalCode,
                           String countryName, String countryKana) {
    return builder().address(address).prefName(prefName).prefKana(prefKana)
        .postalCode(postalCode).countryName(countryName).countryKana(countryKana).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名から {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @param postalCode 郵便番号。{@code null} 許容。
   * @param countryName 国名。{@code null} 許容。
   * @param countryKana 国名かな。{@code null} 許容。
   * @param cityName 市区町村名。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana, String postalCode,
                           String countryName, String countryKana, String cityName) {
    return builder().address(address).prefName(prefName).prefKana(prefKana)
        .postalCode(postalCode).countryName(countryName).countryKana(countryKana)
        .cityName(cityName).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かなから {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @param postalCode 郵便番号。{@code null} 許容。
   * @param countryName 国名。{@code null} 許容。
   * @param countryKana 国名かな。{@code null} 許容。
   * @param cityName 市区町村名。{@code null} 許容。
   * @param cityKana 市区町村名かな。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana, String postalCode,
                           String countryName, String countryKana, String cityName, String cityKana) {
    return builder().address(address).prefName(prefName).prefKana(prefKana)
        .postalCode(postalCode).countryName(countryName).countryKana(countryKana)
        .cityName(cityName).cityKana(cityKana).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名から {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @param postalCode 郵便番号。{@code null} 許容。
   * @param countryName 国名。{@code null} 許容。
   * @param countryKana 国名かな。{@code null} 許容。
   * @param cityName 市区町村名。{@code null} 許容。
   * @param cityKana 市区町村名かな。{@code null} 許容。
   * @param wardName 区名。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana, String postalCode,
                           String countryName, String countryKana, String cityName, String cityKana,
                           String wardName) {
    return builder().address(address).prefName(prefName).prefKana(prefKana)
        .postalCode(postalCode).countryName(countryName).countryKana(countryKana)
        .cityName(cityName).cityKana(cityKana).wardName(wardName).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かなから {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @param postalCode 郵便番号。{@code null} 許容。
   * @param countryName 国名。{@code null} 許容。
   * @param countryKana 国名かな。{@code null} 許容。
   * @param cityName 市区町村名。{@code null} 許容。
   * @param cityKana 市区町村名かな。{@code null} 許容。
   * @param wardName 区名。{@code null} 許容。
   * @param wardKana 区名かな。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana, String postalCode,
                           String countryName, String countryKana, String cityName, String cityKana,
                           String wardName, String wardKana) {
    return builder().address(address).prefName(prefName).prefKana(prefKana)
        .postalCode(postalCode).countryName(countryName).countryKana(countryKana)
        .cityName(cityName).cityKana(cityKana).wardName(wardName).wardKana(wardKana).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かな、大字町名から {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @param postalCode 郵便番号。{@code null} 許容。
   * @param countryName 国名。{@code null} 許容。
   * @param countryKana 国名かな。{@code null} 許容。
   * @param cityName 市区町村名。{@code null} 許容。
   * @param cityKana 市区町村名かな。{@code null} 許容。
   * @param wardName 区名。{@code null} 許容。
   * @param wardKana 区名かな。{@code null} 許容。
   * @param oazaChoName 大字町名。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana, String postalCode,
                           String countryName, String countryKana, String cityName, String cityKana,
                           String wardName, String wardKana, String oazaChoName) {
    return builder().address(address).prefName(prefName).prefKana(prefKana)
        .postalCode(postalCode).countryName(countryName).countryKana(countryKana)
        .cityName(cityName).cityKana(cityKana).wardName(wardName).wardKana(wardKana)
        .oazaChoName(oazaChoName).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かな、大字町名、大字町名かなから {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @param postalCode 郵便番号。{@code null} 許容。
   * @param countryName 国名。{@code null} 許容。
   * @param countryKana 国名かな。{@code null} 許容。
   * @param cityName 市区町村名。{@code null} 許容。
   * @param cityKana 市区町村名かな。{@code null} 許容。
   * @param wardName 区名。{@code null} 許容。
   * @param wardKana 区名かな。{@code null} 許容。
   * @param oazaChoName 大字町名。{@code null} 許容。
   * @param oazaChoKana 大字町名かな。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana, String postalCode,
                           String countryName, String countryKana, String cityName, String cityKana,
                           String wardName, String wardKana, String oazaChoName, String oazaChoKana) {
    return builder().address(address).prefName(prefName).prefKana(prefKana)
        .postalCode(postalCode).countryName(countryName).countryKana(countryKana)
        .cityName(cityName).cityKana(cityKana).wardName(wardName).wardKana(wardKana)
        .oazaChoName(oazaChoName).oazaChoKana(oazaChoKana).build();
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かな、大字町名、大字町名かな、丁目名、丁目名かな、小字名、小字名かな、建物名、建物名かな、部屋番号から {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @param prefName 都道府県名。{@code null} 許容。
   * @param prefKana 都道府県名かな。{@code null} 許容。
   * @param postalCode 郵便番号。{@code null} 許容。
   * @param countryName 国名。{@code null} 許容。
   * @param countryKana 国名かな。{@code null} 許容。
   * @param cityName 市区町村名。{@code null} 許容。
   * @param cityKana 市区町村名かな。{@code null} 許容。
   * @param wardName 区名。{@code null} 許容。
   * @param wardKana 区名かな。{@code null} 許容。
   * @param oazaChoName 大字町名。{@code null} 許容。
   * @param oazaChoKana 大字町名かな。{@code null} 許容。
   * @param chomeName 丁目名。{@code null} 許容。
   * @param chomeKana 丁目名かな。{@code null} 許容。
   * @param koazaName 小字名。{@code null} 許容。
   * @param koazaKana 小字名かな。{@code null} 許容。
   * @param buildingName 建物名。{@code null} 許容。
   * @param buildingKana 建物名かな。{@code null} 許容。
   * @param roomNo 部屋番号。{@code null} 許容。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address, String prefName, String prefKana, String postalCode,
                           String countryName, String countryKana, String cityName, String cityKana,
                           String wardName, String wardKana, String oazaChoName, String oazaChoKana,
                           String chomeName, String chomeKana, String koazaName, String koazaKana,
                           String buildingName, String buildingKana, String roomNo) {
    return builder().address(address).prefName(prefName).prefKana(prefKana)
        .postalCode(postalCode).countryName(countryName).countryKana(countryKana)
        .cityName(cityName).cityKana(cityKana).wardName(wardName).wardKana(wardKana)
        .oazaChoName(oazaChoName).oazaChoKana(oazaChoKana)
        .chomeName(chomeName).chomeKana(chomeKana)
        .koazaName(koazaName).koazaKana(koazaKana)
        .buildingName(buildingName).buildingKana(buildingKana)
        .roomNo(roomNo).build();
  }

  /**
   * {@code Address} のビルダーを作成します。
   *
   * @return 新しい {@code Builder} インスタンス
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * 空の {@code Address} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code Address} インスタンス
   */
  public static Address empty() {
    return new Address();
  }

  // ==================== Builder Pattern ====================

  /**
   * {@code Address} インスタンスを構築するためのビルダーです。
   *
   * <p>M2修正により、19個の重载メソッドの代わりにこのBuilderパターンを使用します。
   */
  public static class Builder {
    private String address;
    private String prefName;
    private String prefKana;
    private String postalCode;
    private String countryName;
    private String countryKana;
    private String cityName;
    private String cityKana;
    private String wardName;
    private String wardKana;
    private String oazaChoName;
    private String oazaChoKana;
    private String chomeName;
    private String chomeKana;
    private String koazaName;
    private String koazaKana;
    private String buildingName;
    private String buildingKana;
    private String roomNo;

    private Builder() {
    }

    /**
     * 住所を設定します（必須）。
     *
     * @param address 住所
     * @return このビルダー
     */
    public Builder address(String address) {
      this.address = address;
      return this;
    }

    /**
     * 都道府県名を設定します。
     *
     * @param prefName 都道府県名
     * @return このビルダー
     */
    public Builder prefName(String prefName) {
      this.prefName = prefName;
      return this;
    }

    /**
     * 都道府県名かなを設定します。
     *
     * @param prefKana 都道府県名かな
     * @return このビルダー
     */
    public Builder prefKana(String prefKana) {
      this.prefKana = prefKana;
      return this;
    }

    /**
     * 郵便番号を設定します。
     *
     * @param postalCode 郵便番号
     * @return このビルダー
     */
    public Builder postalCode(String postalCode) {
      this.postalCode = postalCode;
      return this;
    }

    /**
     * 国名を設定します。
     *
     * @param countryName 国名
     * @return このビルダー
     */
    public Builder countryName(String countryName) {
      this.countryName = countryName;
      return this;
    }

    /**
     * 国名かなを設定します。
     *
     * @param countryKana 国名かな
     * @return このビルダー
     */
    public Builder countryKana(String countryKana) {
      this.countryKana = countryKana;
      return this;
    }

    /**
     * 市区町村名を設定します。
     *
     * @param cityName 市区町村名
     * @return このビルダー
     */
    public Builder cityName(String cityName) {
      this.cityName = cityName;
      return this;
    }

    /**
     * 市区町村名かなを設定します。
     *
     * @param cityKana 市区町村名かな
     * @return このビルダー
     */
    public Builder cityKana(String cityKana) {
      this.cityKana = cityKana;
      return this;
    }

    /**
     * 区名を設定します。
     *
     * @param wardName 区名
     * @return このビルダー
     */
    public Builder wardName(String wardName) {
      this.wardName = wardName;
      return this;
    }

    /**
     * 区名かなを設定します。
     *
     * @param wardKana 区名かな
     * @return このビルダー
     */
    public Builder wardKana(String wardKana) {
      this.wardKana = wardKana;
      return this;
    }

    /**
     * 大字町名を設定します。
     *
     * @param oazaChoName 大字町名
     * @return このビルダー
     */
    public Builder oazaChoName(String oazaChoName) {
      this.oazaChoName = oazaChoName;
      return this;
    }

    /**
     * 大字町名かなを設定します。
     *
     * @param oazaChoKana 大字町名かな
     * @return このビルダー
     */
    public Builder oazaChoKana(String oazaChoKana) {
      this.oazaChoKana = oazaChoKana;
      return this;
    }

    /**
     * 丁目名を設定します。
     *
     * @param chomeName 丁目名
     * @return このビルダー
     */
    public Builder chomeName(String chomeName) {
      this.chomeName = chomeName;
      return this;
    }

    /**
     * 丁目名かなを設定します。
     *
     * @param chomeKana 丁目名かな
     * @return このビルダー
     */
    public Builder chomeKana(String chomeKana) {
      this.chomeKana = chomeKana;
      return this;
    }

    /**
     * 小字名を設定します。
     *
     * @param koazaName 小字名
     * @return このビルダー
     */
    public Builder koazaName(String koazaName) {
      this.koazaName = koazaName;
      return this;
    }

    /**
     * 小字名かなを設定します。
     *
     * @param koazaKana 小字名かな
     * @return このビルダー
     */
    public Builder koazaKana(String koazaKana) {
      this.koazaKana = koazaKana;
      return this;
    }

    /**
     * 建物名を設定します。
     *
     * @param buildingName 建物名
     * @return このビルダー
     */
    public Builder buildingName(String buildingName) {
      this.buildingName = buildingName;
      return this;
    }

    /**
     * 建物名かなを設定します。
     *
     * @param buildingKana 建物名かな
     * @return このビルダー
     */
    public Builder buildingKana(String buildingKana) {
      this.buildingKana = buildingKana;
      return this;
    }

    /**
     * 部屋番号を設定します。
     *
     * @param roomNo 部屋番号
     * @return このビルダー
     */
    public Builder roomNo(String roomNo) {
      this.roomNo = roomNo;
      return this;
    }

    /**
     * {@code Address} インスタンスを構築します。
     *
     * @return 構築された {@code Address} インスタンス
     * @throws IllegalArgumentException 住所が {@code null} または空の場合
     */
    public Address build() {
      validate();
      return new Address(
          trim(address), trim(prefName), trim(prefKana), trim(postalCode),
          trim(countryName), trim(countryKana), trim(cityName), trim(cityKana),
          trim(wardName), trim(wardKana), trim(oazaChoName), trim(oazaChoKana),
          trim(chomeName), trim(chomeKana), trim(koazaName), trim(koazaKana),
          trim(buildingName), trim(buildingKana), trim(roomNo));
    }

    private void validate() {
      if (address == null || address.isBlank()) {
        throw new IllegalArgumentException("address must not be null or blank");
      }
      if (address.length() > MAX_LENGTH) {
        throw new IllegalArgumentException("address must be " + MAX_LENGTH + " characters or less");
      }
      validateLength("prefName", prefName, PREF_NAME_MAX_LENGTH);
      validateLength("prefKana", prefKana, PREF_KANA_MAX_LENGTH);
      validateLength("postalCode", postalCode, POSTAL_CODE_MAX_LENGTH);
      validateLength("countryName", countryName, COUNTRY_NAME_MAX_LENGTH);
      validateLength("countryKana", countryKana, COUNTRY_KANA_MAX_LENGTH);
      validateLength("cityName", cityName, CITY_NAME_MAX_LENGTH);
      validateLength("cityKana", cityKana, CITY_KANA_MAX_LENGTH);
      validateLength("wardName", wardName, WARD_NAME_MAX_LENGTH);
      validateLength("wardKana", wardKana, WARD_KANA_MAX_LENGTH);
      validateLength("oazaChoName", oazaChoName, OAZA_CHO_NAME_MAX_LENGTH);
      validateLength("oazaChoKana", oazaChoKana, OAZA_CHO_KANA_MAX_LENGTH);
      validateLength("chomeName", chomeName, CHOME_NAME_MAX_LENGTH);
      validateLength("chomeKana", chomeKana, CHOME_KANA_MAX_LENGTH);
      validateLength("koazaName", koazaName, KOAZA_NAME_MAX_LENGTH);
      validateLength("koazaKana", koazaKana, KOAZA_KANA_MAX_LENGTH);
      validateLength("buildingName", buildingName, BUILDING_NAME_MAX_LENGTH);
      validateLength("buildingKana", buildingKana, BUILDING_KANA_MAX_LENGTH);
      validateLength("roomNo", roomNo, ROOM_NO_MAX_LENGTH);
    }

    private void validateLength(String fieldName, @Nullable String value, int maxLength) {
      if (value != null && value.length() > maxLength) {
        throw new IllegalArgumentException(fieldName + " must be " + maxLength + " characters or less");
      }
    }

    private @Nullable String trim(@Nullable String value) {
      return value != null ? value.trim() : null;
    }
  }

  @Override
  public ContactType type() {
    return type;
  }

  /**
   * 都道府県名を返します。
   *
   * @return 都道府県名。{@code null} の可能性があります。
   */
  public String getPrefName() {
    return prefName;
  }

  /**
   * 都道府県名かなを返します。
   *
   * @return 都道府県名かな。{@code null} の可能性があります。
   */
  public String getPrefKana() {
    return prefKana;
  }

  /**
   * 郵便番号を返します。
   *
   * @return 郵便番号。{@code null} の可能性があります。
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * 国名を返します。
   *
   * @return 国名。{@code null} の可能性があります。
   */
  public String getCountryName() {
    return countryName;
  }

  /**
   * 国名かなを返します。
   *
   * @return 国名かな。{@code null} の可能性があります。
   */
  public String getCountryKana() {
    return countryKana;
  }

  /**
   * 市区町村名を返します。
   *
   * @return 市区町村名。{@code null} の可能性があります。
   */
  public String getCityName() {
    return cityName;
  }

  /**
   * 市区町村名かなを返します。
   *
   * @return 市区町村名かな。{@code null} の可能性があります。
   */
  public String getCityKana() {
    return cityKana;
  }

  /**
   * 区名を返します。
   *
   * @return 区名。{@code null} の可能性があります。
   */
  public String getWardName() {
    return wardName;
  }

  /**
   * 区名かなを返します。
   *
   * @return 区名かな。{@code null} の可能性があります。
   */
  public String getWardKana() {
    return wardKana;
  }

  /**
   * 大字町名を返します。
   *
   * @return 大字町名。{@code null} の可能性があります。
   */
  public String getOazaChoName() {
    return oazaChoName;
  }

  /**
   * 大字町名かなを返します。
   *
   * @return 大字町名かな。{@code null} の可能性があります。
   */
  public String getOazaChoKana() {
    return oazaChoKana;
  }

  /**
   * 丁目名を返します。
   *
   * @return 丁目名。{@code null} の可能性があります。
   */
  public String getChomeName() {
    return chomeName;
  }

  /**
   * 丁目名かなを返します。
   *
   * @return 丁目名かな。{@code null} の可能性があります。
   */
  public String getChomeKana() {
    return chomeKana;
  }

  /**
   * 小字名を返します。
   *
   * @return 小字名。{@code null} の可能性があります。
   */
  public String getKoazaName() {
    return koazaName;
  }

  /**
   * 小字名かなを返します。
   *
   * @return 小字名かな。{@code null} の可能性があります。
   */
  public String getKoazaKana() {
    return koazaKana;
  }

  /**
   * 建物名を返します。
   *
   * @return 建物名。{@code null} の可能性があります。
   */
  public String getBuildingName() {
    return buildingName;
  }

  /**
   * 建物名かなを返します。
   *
   * @return 建物名かな。{@code null} の可能性があります。
   */
  public String getBuildingKana() {
    return buildingKana;
  }

  /**
   * 部屋番号を返します。
   *
   * @return 部屋番号。{@code null} の可能性があります。
   */
  public String getRoomNo() {
    return roomNo;
  }

  @Override
  public String toString() {
    if (value == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    if (countryName != null) {
      if (countryKana != null) {
        sb.append(countryName).append("(").append(countryKana).append(") ");
      } else {
        sb.append(countryName).append(" ");
      }
    }
    if (postalCode != null) {
      sb.append("[").append(postalCode).append("] ");
    }
    if (prefName != null && prefKana != null) {
      sb.append(prefName).append("(").append(prefKana).append(") ");
    } else if (prefName != null) {
      sb.append(prefName).append(" ");
    }
    if (cityName != null) {
      if (cityKana != null) {
        sb.append(cityName).append("(").append(cityKana).append(") ");
      } else {
        sb.append(cityName).append(" ");
      }
    }
    if (wardName != null) {
      if (wardKana != null) {
        sb.append(wardName).append("(").append(wardKana).append(") ");
      } else {
        sb.append(wardName).append(" ");
      }
    }
    if (oazaChoName != null) {
      if (oazaChoKana != null) {
        sb.append(oazaChoName).append("(").append(oazaChoKana).append(") ");
      } else {
        sb.append(oazaChoName).append(" ");
      }
    }
    if (chomeName != null) {
      if (chomeKana != null) {
        sb.append(chomeName).append("(").append(chomeKana).append(") ");
      } else {
        sb.append(chomeName).append(" ");
      }
    }
    if (koazaName != null) {
      if (koazaKana != null) {
        sb.append(koazaName).append("(").append(koazaKana).append(") ");
      } else {
        sb.append(koazaName).append(" ");
      }
    }
    if (buildingName != null) {
      if (buildingKana != null) {
        sb.append(buildingName).append("(").append(buildingKana).append(") ");
      } else {
        sb.append(buildingName).append(" ");
      }
    }
    if (roomNo != null) {
      sb.append(roomNo).append(" ");
    }
    sb.append(value);
    return sb.toString();
  }

  /**
   * Addressとデータベースの文字列を相互に変換するためのJPAコンバーターです。
   *
   * <p>データベースから読み込んだ不正なデータに対しては、{@code empty()}を返して安全にフォールバックします。
   */
  @Converter(autoApply = true)
  public static class AddressConverter implements AttributeConverter<Address, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code Address} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(Address attribute) {
      if (attribute == null) {
        return null;
      }
      return attribute.value();
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 変換された {@code Address} インスタンス。データベースデータが {@code null} または不正な場合は空のインスタンス
     */
    @Override
    public Address convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return Address.empty();
      }
      try {
        return Address.of(dbData);
      } catch (IllegalArgumentException e) {
        return Address.empty();
      }
    }
  }
}
