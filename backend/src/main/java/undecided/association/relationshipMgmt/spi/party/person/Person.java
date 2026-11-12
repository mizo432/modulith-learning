package undecided.association.relationshipMgmt.spi.party.person;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.association.relationshipMgmt.spi.party.GovtAssignedId;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.SearchName;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 個人を表すJPAエンティティです。
 *
 * <p>このエンティティは {@link Party} を拡張し、個人特有の属性（法的名前、生年月日など）を保持します。
 *
 * <p>親クラス {@link Party} の {@code @Audited} アノテーションにより、Hibernate Envers による履歴管理が有効です。
 */
@Table(name = "People", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Person extends Party {
  /** 法的名前。 */
  @Convert(converter = LegalName.LegalNameConverter.class)
  private LegalName legalName;

  /** 生年月日。 */
  @Convert(converter = DateOfBirth.DateOfBirthConverter.class)
  private DateOfBirth dateOfBirth;

  public Person(
      SnowflakeId partyId,
      PartyTypeCode partyTypeCode,
      String name,
      String code,
      GovtAssignedId govtAssignedId,
      SearchName searchName,
      LegalName legalName,
      DateOfBirth dateOfBirth) {
    super(partyId, partyTypeCode, name, code, govtAssignedId, searchName);
    this.legalName = legalName;
    this.dateOfBirth = dateOfBirth;
  }
}
