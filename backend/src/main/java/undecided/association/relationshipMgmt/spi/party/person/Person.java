package undecided.association.relationshipMgmt.spi.party.person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import com.github.mizo432.shared.common.precondition.ObjectPrecondition;
import undecided.supporting.snowflake.SnowflakeId;
import java.time.LocalDateTime;

/**
 * 個人を表すJPAエンティティです。
 *
 * <p>このエンティティは {@link Party} を拡張し、個人特有の属性（法的名前、生年月日など）を保持します。
 *
 * <p>C3修正により、{@code @AllArgsConstructor}は削除され、外部からの直接インスタンス化を防いでいます。
 * JPA用には{@code @NoArgsConstructor(access = AccessLevel.PROTECTED)}が設定されています。
 */
@Table(name = "People", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Person extends Party {

  /** 個人の法的名前。公式な文書や識別に使用されます。 */
  protected LegalName legalName;

  /** 個人の生年月日。年齢計算や身分証明に使用されます。 */
  protected DateOfBirth dateOfBirth;

  /** 個人の死亡日。nullの場合は生存中とみなされます。 */
  protected DateOfDie dateOfDie;

  /**
   * 個人インスタンスを作成します。
   *
   * @param partyId 一意のパーティ識別子
   * @param partyTypeCode パーティ種別コード
   * @param code コード
   * @param validFrom 有効開始日
   * @param legalName 法的名前
   * @param dateOfBirth 生年月日
   * @param dateOfDie 死亡日（nullの場合は生存中）
   * @return 作成されたPersonインスタンス
   */
  public static Person create(
      SnowflakeId partyId,
      PartyTypeCode partyTypeCode,
      String code,
      LocalDateTime validFrom,
      LegalName legalName,
      DateOfBirth dateOfBirth,
      @Nullable DateOfDie dateOfDie) {
    Person person = new Person();
    person.initParty(partyId, partyTypeCode, code, validFrom, null, null, null);
    person.legalName = legalName;
    person.dateOfBirth = dateOfBirth;
    person.dateOfDie = dateOfDie;
    return person;
  }

  /**
   * 個人の情報を更新します。
   *
   * @param legalName 法的名前
   * @param dateOfBirth 生年月日
   * @param dateOfDie 死亡日（nullの場合は生存中とみなされます）
   */
  public void update(LegalName legalName, DateOfBirth dateOfBirth, @Nullable DateOfDie dateOfDie) {
    ObjectPrecondition.checkNotNull(legalName, () -> new IllegalArgumentException("legalName must not be null"));
    ObjectPrecondition.checkNotNull(dateOfBirth, () -> new IllegalArgumentException("dateOfBirth must not be null"));
    this.legalName = legalName;
    this.dateOfBirth = dateOfBirth;
    this.dateOfDie = dateOfDie;
  }
}
