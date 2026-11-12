package undecided.association.relationshipMgmt.party.query;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.association.relationshipMgmt.party.repository.OrgRepository;
import undecided.association.relationshipMgmt.spi.party.OrgQuery;
import undecided.association.relationshipMgmt.spi.party.org.Org;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織（Org）情報を取得するためのクエリサービスです。
 *
 * <p>OrgQueryインターフェースを実装し、組織データの検索機能を提供します。
 */
@RequiredArgsConstructor
@Service
public class OrganizationQueryService implements OrgQuery {

  private final OrgRepository orgRepository;

  /**
   * 全ての組織情報を取得します。
   *
   * @return 現在利用可能な全ての組織データのリスト
   */
  @Override
  public List<Org> findAll() {
    return orgRepository.findAll();
  }

  /**
   * 指定されたSnowflakeIdに対応する組織情報を検索します。
   *
   * @param id 検索対象の組織を一意に識別するためのSnowflakeId
   * @return 指定されたIDに対応する組織の情報を含むOptional。存在しない場合はOptional.empty()を返します。
   */
  @Override
  public Optional<Org> findById(SnowflakeId id) {
    return orgRepository.findById(id);
  }
}
