package undecided.association.relationshipMgmt.party.query;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.party.repository.ContactUseRepository;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseQuery;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 連絡先（ContactUse）情報を取得するためのクエリサービスです。
 *
 * <p>ContactUseQueryインターフェースを実装し、連絡先データの検索機能を提供します。
 */
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ContactUseQueryService implements ContactUseQuery {

  private final ContactUseRepository contactUseRepository;

  /**
   * 全ての連絡先情報を取得します。
   *
   * @return 現在利用可能な全ての連絡先データのリスト
   */
  @Override
  public List<ContactUse> findAll() {
    return contactUseRepository.findAll();
  }

  /**
   * 指定されたSnowflakeIdに対応する連絡先情報を検索します。
   *
   * @param id 検索対象の連絡先を一意に識別するためのSnowflakeId
   * @return 指定されたIDに対応する連絡先の情報を含むOptional。存在しない場合はOptional.empty()を返します。
   */
  @Override
  public Optional<ContactUse> findById(SnowflakeId id) {
    return contactUseRepository.findById(id);
  }

  /**
   * 指定されたパーティIDに関連する連絡先情報を検索します。
   *
   * @param partyId 検索対象のパーティを識別するためのSnowflakeId
   * @return 指定されたパーティに関連する連絡先データのリスト
   */
  @Override
  public List<ContactUse> findByPartyId(SnowflakeId partyId) {
    return contactUseRepository.findByPartyId(partyId);
  }
}
