package undecided.association.relationshipMgmt.spi.party.person;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import undecided.association.relationshipMgmt.SchemaRelationshipMgmt;
import undecided.association.relationshipMgmt.spi.party.Party;

/**
 * 個人を表すJPAエンティティです。
 *
 * <p>このエンティティは {@link Party} を拡張し、個人特有の属性（法的名前、生年月日など）を保持します。
 */
@Table(name = "People", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Person extends Party {

  /** 法的名前。 */
  private LegalName legalName;

  /** 生年月日。 */
  private DateOfBirth dateOfBirth;
}
