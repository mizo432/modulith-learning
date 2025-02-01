package undecided.erp.relationship.domain.model.partyRole.orgRole.myCompany;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;
import static undecided.erp.common.precondition.StringPrecondition.checkHalfWidthLengthClosed;
import static undecided.erp.common.precondition.StringPrecondition.checkNonEmpty;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.common.exception.BusinessException;

/**
 * MyCompanyクラスは、会社情報を表現するエンティティクラスです。
 * <p>
 * このクラスは、データベースの「my_companies」テーブルにマッピングされています。 会社のID、名前、名前のイニシャル、カナ表記の情報を保持します。
 */
@Entity
@Table(name = "my_companies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyCompany {

  /**
   * 会社を一意に識別するための識別子を表します。
   * <p>
   * このIDは、MyCompanyエンティティの主キーとして使用され、 エンティティのキー構造の一部として埋め込まれています。
   */
  @EmbeddedId
  @AttributeOverrides({
      @AttributeOverride(name = "value", column = @Column(name = "my_company_id", nullable = false)
      )})
  private SnowflakeId myCompanyId;

  /**
   * 会社の名前を表します。
   * <p>
   * このフィールドには会社名を格納します。必須項目であり、最大長は100文字です。
   */
  @Column(nullable = false, length = 100)
  private String name;

  /**
   * 会社の名前のイニシャルを表します。
   * <p>
   * このフィールドには、会社名を短縮するための3文字以内のイニシャルを格納します。 必須項目であり、最大長は3文字です。
   */
  @Column(nullable = false, length = 3)
  private String initials;

  /**
   * 会社名のカナ表記を表します。
   * <p>
   * このフィールドには、会社名をカナで表記した文字列を格納します。 必須項目であり、最大長は100文字です。
   */
  @Column(nullable = false, length = 100)
  private String kanaName;

  public static MyCompany newInstance(String name, String kanaName) {
    checkNotNull(name, () -> new BusinessException("name must not be null"));
    checkNonEmpty(name, () -> new BusinessException("name must not be empty"));
    checkNotNull(kanaName, () -> new BusinessException("kanaName must not be null"));
    checkNonEmpty(kanaName, () -> new BusinessException("kanaName must not be empty"));
    checkHalfWidthLengthClosed(kanaName, () -> new BusinessException("kanaName must be half width"),
        1, 100);

    return new MyCompany(SnowflakeId.newInstance(), name, String.valueOf(kanaName.charAt(0)),
        kanaName);
  }

}
