package undecided.association.relationshipMgmt.spi.party.person;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.common.primitive.Strings2;
import com.github.mizo432.shared.sensitive.SensitiveDataMasker;
import com.github.mizo432.shared.value.StringCode;

/**
 * 法的名前を表す値オブジェクトです。
 *
 * <p>このクラスは {@link StringCode} インターフェースを実装し、個人の本名（法的名前）を不変の値として保持します。 法的名前は {@link LastName}（姓）と
 * {@link FirstName}（名）の2つの値オブジェクトで構成されます。
 */
@EqualsAndHashCode
public class LegalName implements StringCode<LegalName> {

  /** 姓。 */
  private final LastName lastName;

  /** 名。 */
  private final FirstName firstName;

  protected LegalName() {
    lastName = null;
    firstName = null;
  }

  private LegalName(LastName lastName, FirstName firstName) {
    this.lastName = lastName;
    this.firstName = firstName;
  }

  /**
   * 姓と名から法的名前を作成します。
   *
   * @param lastName 姓（1文字以上50文字以下）
   * @param firstName 名（1文字以上50文字以下）
   * @return 法的名前の値オブジェクト
   * @throws IllegalArgumentException 姓または名がnull、空、または文字数制限を超えている場合
   */
  public static LegalName of(String lastName, String firstName) {
    return new LegalName(LastName.of(lastName), FirstName.of(firstName));
  }

  /**
   * 姓と名から法的名前を作成します。
   *
   * @param lastName 姓の値オブジェクト
   * @param firstName 名の値オブジェクト
   * @return 法的名前の値オブジェクト
   */
  public static LegalName of(LastName lastName, FirstName firstName) {
    return new LegalName(lastName, firstName);
  }

  /**
   * 空の法的名前を表すインスタンスを返します。
   *
   * @return 空の {@link LegalName} インスタンス
   */
  public static LegalName empty() {
    return new LegalName();
  }

  /**
   * 姓を返します。
   *
   * @return 姓の値オブジェクト
   */
  public LastName lastName() {
    return lastName;
  }

  /**
   * 名を返します。
   *
   * @return 名の値オブジェクト
   */
  public FirstName firstName() {
    return firstName;
  }

  /**
   * 法的名前の値を「姓 名」の形式で返します。
   *
   * @return 法的名前の文字列（例: "田中 太郎"）
   */
  @Override
  public String value() {
    if (lastName == null && firstName == null) {
      return null;
    }
    String lastNameValue = lastName != null ? lastName.value() : "";
    String firstNameValue = firstName != null ? firstName.value() : "";
    return lastNameValue + " " + firstNameValue;
  }

  /**
   * 法的名前が空かどうかを判定します。
   *
   * @return 法的名前が空である場合は {@code true} を返し、そうでない場合は {@code false} を返します。
   */
  @Override
  public boolean isEmpty() {
    return (lastName == null || lastName.isEmpty()) && (firstName == null || firstName.isEmpty());
  }

  /**
   * 法的名前の文字列表現を返します。
   *
   * @return 法的名前の文字列（例: "田中 太郎"）
   */
  @Override
  public String toString() {
    return SensitiveDataMasker.maskName(value());
  }

  /** LegalNameとデータベースの文字列を相互に変換するためのJPAコンバーターです。 */
  @Converter(autoApply = true)
  public static class LegalNameConverter implements AttributeConverter<LegalName, String> {

    /**
     * エンティティ属性をデータベースカラムに変換します。
     *
     * @param attribute 変換対象の {@code LegalName} 属性
     * @return データベースに保存する {@code String}。属性が {@code null} の場合は {@code null}
     */
    @Override
    public String convertToDatabaseColumn(LegalName attribute) {
      return attribute != null ? attribute.value() : null;
    }

    /**
     * データベースカラムをエンティティ属性に変換します。
     *
     * @param dbData データベースから読み込んだ {@code String}。{@code null} の可能性があります。
     * @return 変換された {@code LegalName} インスタンス。データベースデータが {@code null} の場合は空のインスタンス
     */
    @Override
    public LegalName convertToEntityAttribute(@Nullable String dbData) {
      if (dbData == null) {
        return LegalName.empty();
      }
      String[] parts = dbData.split(" ", 2);
      if (parts.length == 2) {
        return new LegalName(new LastName(parts[0]), new FirstName(parts[1]));
      } else if (parts.length == 1) {
        return new LegalName(new LastName(parts[0]), new FirstName(""));
      }
      return LegalName.empty();
    }
  }
}
