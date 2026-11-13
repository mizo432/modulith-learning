package undecided.association.relationshipMgmt.spi.party.contact;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;

/**
 * 住所を表す値オブジェクトです。
 *
 * <p>このクラスは {@link ContactBase} を継承し、住所を不変の値として保持します。
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

  private Address(String value, String prefName, String prefKana, String postalCode, String countryName, String countryKana, String cityName, String cityKana, String wardName, String wardKana, String oazaChoName, String oazaChoKana, String chomeName, String chomeKana, String koazaName, String koazaKana, String buildingName, String buildingKana, String roomNo) {
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

  /**
   * 指定された文字列から {@code Address} インスタンスを作成します。
   *
   * @param address 住所。{@code null} は許容されません。
   * @return 住所の {@code Address} インスタンス
   * @throws IllegalArgumentException 住所が {@code null} または空の場合
   */
  public static Address of(String address) {
    return of(address, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
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
    return of(address, prefName, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
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
    return of(address, prefName, prefKana, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
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
    return of(address, prefName, prefKana, postalCode, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
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
    return of(address, prefName, prefKana, postalCode, countryName, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
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
  public static Address of(String address, String prefName, String prefKana, String postalCode, String countryName, String countryKana) {
    return of(address, prefName, prefKana, postalCode, countryName, countryKana, null, null, null, null, null, null, null, null, null, null, null, null, null);
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
  public static Address of(String address, String prefName, String prefKana, String postalCode, String countryName, String countryKana, String cityName) {
    return of(address, prefName, prefKana, postalCode, countryName, countryKana, cityName, null, null, null, null, null, null, null, null, null, null, null, null);
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
  public static Address of(String address, String prefName, String prefKana, String postalCode, String countryName, String countryKana, String cityName, String cityKana) {
    return of(address, prefName, prefKana, postalCode, countryName, countryKana, cityName, cityKana, null, null, null, null, null, null, null, null, null, null, null);
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
  public static Address of(String address, String prefName, String prefKana, String postalCode, String countryName, String countryKana, String cityName, String cityKana, String wardName) {
    return of(address, prefName, prefKana, postalCode, countryName, countryKana, cityName, cityKana, wardName, null, null, null, null, null, null, null, null, null, null);
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
  public static Address of(String address, String prefName, String prefKana, String postalCode, String countryName, String countryKana, String cityName, String cityKana, String wardName, String wardKana) {
    return of(address, prefName, prefKana, postalCode, countryName, countryKana, cityName, cityKana, wardName, wardKana, null, null, null, null, null, null, null, null, null);
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
  public static Address of(String address, String prefName, String prefKana, String postalCode, String countryName, String countryKana, String cityName, String cityKana, String wardName, String wardKana, String oazaChoName) {
    return of(address, prefName, prefKana, postalCode, countryName, countryKana, cityName, cityKana, wardName, wardKana, oazaChoName, null, null, null, null, null, null, null, null);
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
  public static Address of(String address, String prefName, String prefKana, String postalCode, String countryName, String countryKana, String cityName, String cityKana, String wardName, String wardKana, String oazaChoName, String oazaChoKana) {
    return of(address, prefName, prefKana, postalCode, countryName, countryKana, cityName, cityKana, wardName, wardKana, oazaChoName, oazaChoKana, null, null, null, null, null, null, null);
  }

  /**
   * 指定された住所、都道府県名、都道府県名かな、郵便番号、国名、国名かな、市区町村名、市区町村名かな、区名、区名かな、大字町名、大字町名かな、丁目名、丁目名かな、小字名、小字名かな、建物名、建物名かなから {@code Address} インスタンスを作成します。
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
  public static Address of(String address, String prefName, String prefKana, String postalCode, String countryName, String countryKana, String cityName, String cityKana, String wardName, String wardKana, String oazaChoName, String oazaChoKana, String chomeName, String chomeKana, String koazaName, String koazaKana, String buildingName, String buildingKana, String roomNo) {
    if (address == null || address.isBlank()) {
      throw new IllegalArgumentException("address must not be null or blank");
    }
    if (address.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("address must be " + MAX_LENGTH + " characters or less");
    }
    if (prefName != null && prefName.length() > PREF_NAME_MAX_LENGTH) {
      throw new IllegalArgumentException("prefName must be " + PREF_NAME_MAX_LENGTH + " characters or less");
    }
    if (prefKana != null && prefKana.length() > PREF_KANA_MAX_LENGTH) {
      throw new IllegalArgumentException("prefKana must be " + PREF_KANA_MAX_LENGTH + " characters or less");
    }
    if (postalCode != null && postalCode.length() > POSTAL_CODE_MAX_LENGTH) {
      throw new IllegalArgumentException("postalCode must be " + POSTAL_CODE_MAX_LENGTH + " characters or less");
    }
    if (countryName != null && countryName.length() > COUNTRY_NAME_MAX_LENGTH) {
      throw new IllegalArgumentException("countryName must be " + COUNTRY_NAME_MAX_LENGTH + " characters or less");
    }
    if (countryKana != null && countryKana.length() > COUNTRY_KANA_MAX_LENGTH) {
      throw new IllegalArgumentException("countryKana must be " + COUNTRY_KANA_MAX_LENGTH + " characters or less");
    }
    if (cityName != null && cityName.length() > CITY_NAME_MAX_LENGTH) {
      throw new IllegalArgumentException("cityName must be " + CITY_NAME_MAX_LENGTH + " characters or less");
    }
    if (cityKana != null && cityKana.length() > CITY_KANA_MAX_LENGTH) {
      throw new IllegalArgumentException("cityKana must be " + CITY_KANA_MAX_LENGTH + " characters or less");
    }
    if (wardName != null && wardName.length() > WARD_NAME_MAX_LENGTH) {
      throw new IllegalArgumentException("wardName must be " + WARD_NAME_MAX_LENGTH + " characters or less");
    }
    if (wardKana != null && wardKana.length() > WARD_KANA_MAX_LENGTH) {
      throw new IllegalArgumentException("wardKana must be " + WARD_KANA_MAX_LENGTH + " characters or less");
    }
    if (oazaChoName != null && oazaChoName.length() > OAZA_CHO_NAME_MAX_LENGTH) {
      throw new IllegalArgumentException("oazaChoName must be " + OAZA_CHO_NAME_MAX_LENGTH + " characters or less");
    }
    if (oazaChoKana != null && oazaChoKana.length() > OAZA_CHO_KANA_MAX_LENGTH) {
      throw new IllegalArgumentException("oazaChoKana must be " + OAZA_CHO_KANA_MAX_LENGTH + " characters or less");
    }
    if (chomeName != null && chomeName.length() > CHOME_NAME_MAX_LENGTH) {
      throw new IllegalArgumentException("chomeName must be " + CHOME_NAME_MAX_LENGTH + " characters or less");
    }
    if (chomeKana != null && chomeKana.length() > CHOME_KANA_MAX_LENGTH) {
      throw new IllegalArgumentException("chomeKana must be " + CHOME_KANA_MAX_LENGTH + " characters or less");
    }
    if (koazaName != null && koazaName.length() > KOAZA_NAME_MAX_LENGTH) {
      throw new IllegalArgumentException("koazaName must be " + KOAZA_NAME_MAX_LENGTH + " characters or less");
    }
    if (koazaKana != null && koazaKana.length() > KOAZA_KANA_MAX_LENGTH) {
      throw new IllegalArgumentException("koazaKana must be " + KOAZA_KANA_MAX_LENGTH + " characters or less");
    }
    if (buildingName != null && buildingName.length() > BUILDING_NAME_MAX_LENGTH) {
      throw new IllegalArgumentException("buildingName must be " + BUILDING_NAME_MAX_LENGTH + " characters or less");
    }
    if (buildingKana != null && buildingKana.length() > BUILDING_KANA_MAX_LENGTH) {
      throw new IllegalArgumentException("buildingKana must be " + BUILDING_KANA_MAX_LENGTH + " characters or less");
    }
    if (roomNo != null && roomNo.length() > ROOM_NO_MAX_LENGTH) {
      throw new IllegalArgumentException("roomNo must be " + ROOM_NO_MAX_LENGTH + " characters or less");
    }
    return new Address(address.trim(), prefName != null ? prefName.trim() : null, prefKana != null ? prefKana.trim() : null, postalCode != null ? postalCode.trim() : null, countryName != null ? countryName.trim() : null, countryKana != null ? countryKana.trim() : null, cityName != null ? cityName.trim() : null, cityKana != null ? cityKana.trim() : null, wardName != null ? wardName.trim() : null, wardKana != null ? wardKana.trim() : null, oazaChoName != null ? oazaChoName.trim() : null, oazaChoKana != null ? oazaChoKana.trim() : null, chomeName != null ? chomeName.trim() : null, chomeKana != null ? chomeKana.trim() : null, koazaName != null ? koazaName.trim() : null, koazaKana != null ? koazaKana.trim() : null, buildingName != null ? buildingName.trim() : null, buildingKana != null ? buildingKana.trim() : null, roomNo != null ? roomNo.trim() : null);
  }

  /**
   * 空の {@code Address} インスタンスを作成します。
   *
   * @return {@code null} の値を持つ {@code Address} インスタンス
   */
  public static Address empty() {
    return new Address();
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

  /** Addressとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class AddressConverter implements AttributeConverter<Address, String> {

    @Override
    public String convertToDatabaseColumn(Address attribute) {
      if (attribute == null) {
        return null;
      }
      return attribute.value();
    }

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
