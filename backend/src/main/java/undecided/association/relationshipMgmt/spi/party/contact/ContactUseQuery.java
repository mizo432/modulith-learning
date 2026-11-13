package undecided.association.relationshipMgmt.spi.party.contact;

import java.util.List;
import java.util.Optional;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 連絡先（ContactUse）情報を取得するためのクエリインターフェースです。
 *
 * <p>このインターフェースを実装することで、連絡先のデータにアクセスするための機能を提供します。
 */
public interface ContactUseQuery {

  /**
   * 全ての連絡先情報を取得します。
   *
   * @return 現在利用可能な全ての連絡先データのリスト
   */
  List<ContactUse> findAll();

  /**
   * 指定されたSnowflakeIdに対応する連絡先情報を検索します。
   *
   * @param id 検索対象の連絡先を一意に識別するためのSnowflakeId
   * @return 指定されたIDに対応する連絡先の情報を含むOptional。存在しない場合はOptional.empty()を返します。
   */
  Optional<ContactUse> findById(SnowflakeId id);

  /**
   * 指定されたパーティIDに関連する連絡先情報を検索します。
   *
   * @param partyId 検索対象のパーティを識別するためのSnowflakeId
   * @return 指定されたパーティに関連する連絡先データのリスト
   */
  List<ContactUse> findByPartyId(SnowflakeId partyId);
}
