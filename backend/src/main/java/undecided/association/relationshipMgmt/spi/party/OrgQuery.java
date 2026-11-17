package undecided.association.relationshipMgmt.spi.party;

import java.util.List;
import java.util.Optional;
import undecided.association.relationshipMgmt.spi.party.org.Org;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織（Org）情報を取得するためのクエリインターフェースです。
 *
 * <p>このインターフェースを実装することで、組織のデータにアクセスするための機能を提供します。
 */
public interface OrgQuery {

  /**
   * 全ての組織情報を取得します。
   *
   * @return 現在利用可能な全ての組織データのリスト
   */
  List<Org> findAll();

  /**
   * 指定されたSnowflakeIdに対応する組織情報を検索します。
   *
   * @param id 検索対象の組織を一意に識別するためのSnowflakeId
   * @return 指定されたIDに対応する組織の情報を含むOptional。存在しない場合はOptional.empty()を返します。
   */
  Optional<Org> findById(SnowflakeId id);
}
