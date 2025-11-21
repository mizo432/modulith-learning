package undecided.erp.common.entity;

import static undecided.erp.common.primitive.Objects2.isNull;

import java.io.Serializable;
import java.util.UUID;
import undecided.erp.common.uuidV7Provider.UuidV7Provider;

/**
 * UuidV7IdValueクラスは、UUIDバージョン7に基づいた識別子を表現するクラスです。 このクラスは不変であり、一度生成されたインスタンスの値を変更することはできません。
 * UuidValueインターフェースおよびSerializableインターフェースを実装しています。
 *
 * <p>主な役割: - UUIDバージョン7に基づく一意な識別子を管理する - 空のIDおよびUUIDを簡便に生成するためのメソッドを提供する
 */
public class UuidV7IdValue implements UuidValue<UuidV7IdValue>, Serializable {
  /**
   * このクラスのシリアライズにおけるバージョン管理を行うための識別子です。
   *
   * <p>クラスの構造が変更された場合に、以前にシリアライズされたオブジェクトを 正しくデシリアライズできるよう、この定数でバージョンを管理します。
   * 同一バージョンを持つ場合、互換性があるとみなされます。
   */
  private static final long serialVersionUID = 1L;

  /**
   * UUID型の値を保持するフィールドです。
   *
   * <p>このフィールドは不変であり、一度設定されると変更することはできません。 UuidV7IdValueクラスの各インスタンスごとに固有のUUIDを管理します。
   */
  private final UUID value;

  /**
   * UuidV7IdValueクラスのデフォルトコンストラクタです。
   *
   * <p>このコンストラクタはUUID値をnullで初期化した新しいインスタンスを生成します。
   */
  public UuidV7IdValue() {
    this(null);
  }

  /**
   * 指定されたUUID値を使用して、UuidV7IdValueの新しいインスタンスを生成します。
   *
   * @param value このインスタンスに設定されるUUID値
   */
  UuidV7IdValue(UUID value) {
    this.value = value;
  }

  /**
   * 新しいUuidV7IdValueインスタンスを生成して返します。
   *
   * <p>このメソッドはUuidV7Providerにより生成されたUUIDを使用して、UuidV7IdValueのインスタンスを作成します。
   *
   * @return 新しく生成されたUuidV7IdValueインスタンス
   */
  public static UuidValue<UuidV7IdValue> newInstance() {
    return new UuidV7IdValue(UuidV7Provider.newInstanse());
  }

  /**
   * 空のUuidValueインスタンスを返します。
   *
   * @return UUIDがnullで初期化された新しいUuidV7IdValueインスタンス
   */
  UuidValue<UuidV7IdValue> empty() {
    return new UuidV7IdValue(null);
  }

  /**
   * UUIDの値を取得します。
   *
   * @return このオブジェクトに関連付けられているUUIDの値
   */
  @Override
  public UUID value() {
    return value;
  }

  /**
   * このオブジェクトが空であるかどうかを判定します。
   *
   * @return オブジェクトが空の場合はtrue、それ以外の場合はfalse
   */
  @Override
  public boolean isEmpty() {
    return isNull(value);
  }

  /**
   * このオブジェクトが指定されたオブジェクトと等しいかどうかを判定します。
   *
   * @param o 比較対象のオブジェクト
   * @return このオブジェクトが指定されたオブジェクトと等しい場合はtrue、それ以外の場合はfalse
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UuidV7IdValue that = (UuidV7IdValue) o;
    return java.util.Objects.equals(value, that.value);
  }

  /**
   * オブジェクトのハッシュコードを生成します。
   *
   * @return このオブジェクトのハッシュコード値。内部に保持するフィールド値に基づいて計算されます。
   */
  @Override
  public int hashCode() {
    return java.util.Objects.hash(value);
  }

  /**
   * 現在のオブジェクトの文字列表現を返します。
   *
   * @return このオブジェクトの文字列表現。具体的にはフィールド値を文字列に変換した結果。
   */
  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
