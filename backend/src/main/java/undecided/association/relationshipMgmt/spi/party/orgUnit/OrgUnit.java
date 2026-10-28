package undecided.association.relationshipMgmt.spi.party.orgUnit;

import lombok.Getter;
import undecided.association.relationshipMgmt.spi.party.Party;
import com.github.mizo432.shared.value.datetime.ValidInterval;

/**
 * 組織単位（部署、チームなど）を表すエンティティクラス。
 *
 * <p>このクラスは {@link Party} を継承し、組織単位固有の属性（名称、有効期間など）を保持します。
 */
@Getter
public class OrgUnit extends Party {

  /** 組織単位の名称。 */
  private OrgUnitName name;

  /** 組織単位の有効期間（開始日から終了日までの範囲）。 */
  private ValidInterval validInterval;
}
