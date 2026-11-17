package undecided.association.relationshipMgmt.party.query;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.association.relationshipMgmt.party.repository.OrgUnitRepository;
import undecided.association.relationshipMgmt.spi.party.OrgUnitQuery;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnit;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織単位（OrgUnit）情報を取得するためのクエリサービスです。
 *
 * <p>OrgUnitQueryインターフェースを実装し、組織単位データの検索機能を提供します。
 */
@RequiredArgsConstructor
@Service
public class OrgUnitQueryService implements OrgUnitQuery {

  private final OrgUnitRepository orgUnitRepository;

  /**
   * 全ての組織単位情報を取得します。
   *
   * @return 現在利用可能な全ての組織単位データのリスト
   */
  @Override
  public List<OrgUnit> findAll() {
    return orgUnitRepository.findAll();
  }

  /**
   * 指定されたSnowflakeIdに対応する組織単位情報を検索します。
   *
   * @param id 検索対象の組織単位を一意に識別するためのSnowflakeId
   * @return 指定されたIDに対応する組織単位の情報を含むOptional。存在しない場合はOptional.empty()を返します。
   */
  @Override
  public Optional<OrgUnit> findById(SnowflakeId id) {
    return orgUnitRepository.findById(id);
  }
}
