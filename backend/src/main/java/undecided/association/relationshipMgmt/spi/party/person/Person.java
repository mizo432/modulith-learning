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
 *
 * <p>注意: {@code @AllArgsConstructor}はpublicアクセスレベルで付与されており、外部からすべてのフィールドを設定できるコンストラクタが公開されています。
 * これはC3修正の趣旨（ドメインの不変性保護）に反するため、将来的には{@code @AllArgsConstructor(access = AccessLevel.PROTECTED)}に変更するか、
 * ファクトリメソッドを追加することを検討してください。
 */
@Table(name = "People", schema = SchemaRelationshipMgmt.NAME)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Person extends Party {

  /** 個人の法的名前。公式な文書や識別に使用されます。 */
  private LegalName legalName;

  /** 個人の生年月日。年齢計算や身分証明に使用されます。 */
  private DateOfBirth dateOfBirth;

  /** 個人の死亡日。nullの場合は生存中とみなされます。 */
  private DateOfDie dateOfDie;
}
