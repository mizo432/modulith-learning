package undecided.association.relationshipMgmt.spi.party.org;

import lombok.Getter;
import undecided.association.relationshipMgmt.spi.party.Party;

/**
 * 組織（法人、団体など）を表すエンティティクラス。
 *
 * <p>このクラスは {@link Party} を継承し、組織固有の属性（組織名、有効期間など）を保持します。
 */
@Getter
public class Org extends Party {

  /** 組織の正式名称。 */
  private OrgName name;

  /** 組織の有効期間（開始日から終了日までの範囲）。 */
  private ValidInterval validInterval;
}
