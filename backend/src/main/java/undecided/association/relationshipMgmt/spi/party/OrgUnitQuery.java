package undecided.association.relationshipMgmt.spi.party;

import java.util.List;
import java.util.Optional;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnit;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織単位（OrgUnit）情報を取得するためのクエリインターフェースです。
 *
 * <p>このインターフェースを実装することで、組織単位のデータにアクセスするための機能を提供します。
 */
public interface OrgUnitQuery {

  /**
   * 全ての組織単位情報を取得します。
   *
   * @return 現在利用可能な全ての組織単位データのリスト
   */
  List<OrgUnit> findAll();

  /**
   * 指定されたSnowflakeIdに対応する組織単位情報を検索します。
   *
   * @param id 検索対象の組織単位を一意に識別するためのSnowflakeId
   * @return 指定されたIDに対応する組織単位の情報を含むOptional。存在しない場合はOptional.empty()を返します。
   */
  Optional<OrgUnit> findById(SnowflakeId id);
}
