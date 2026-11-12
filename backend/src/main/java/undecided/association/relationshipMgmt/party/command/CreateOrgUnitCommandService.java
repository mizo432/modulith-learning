package undecided.association.relationshipMgmt.party.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.party.repository.OrgRepository;
import undecided.association.relationshipMgmt.party.repository.OrgUnitRepository;
import undecided.association.relationshipMgmt.spi.party.CreateOrgUnitCommand;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.org.Org;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnit;
import undecided.association.relationshipMgmt.spi.party.orgUnit.dto.OrgUnitDto;
import java.time.LocalDateTime;
import com.github.mizo432.shared.value.datetime.ValidInterval;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;
import org.jspecify.annotations.Nullable;

/**
 * 組織単位新規作成のコマンドサービスです。
 *
 * <p>OrgUnitDtoからOrgUnitエンティティを作成し、データベースに保存します。
 */
@RequiredArgsConstructor
@Service
public class CreateOrgUnitCommandService implements CreateOrgUnitCommand {

  private final OrgUnitRepository orgUnitRepository;
  private final OrgRepository orgRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  /**
   * 組織単位を新規作成します。
   *
   * <p>受け取ったDTOからOrgUnitエンティティを作成し、リポジトリに保存します。
   *
   * @param orgUnitDto 組織単位情報のDTO。orgUnitName、orgUnitCode、orgUnitTypeCode、parentOrgUnitId、status、orgIdを含みます。
   * @throws IllegalArgumentException DTOがnullの場合、または必須フィールドが未設定の場合
   */
  @Override
  @Transactional
  public void create(OrgUnitDto orgUnitDto) {
    if (orgUnitDto == null) {
      throw new IllegalArgumentException("OrgUnitDtoはnullであってはいけません");
    }
    if (orgUnitDto.getOrgUnitName() == null || orgUnitDto.getOrgUnitName().isEmpty()) {
      throw new IllegalArgumentException("orgUnitNameは必須です");
    }
    if (orgUnitDto.getOrgUnitCode() == null || orgUnitDto.getOrgUnitCode().isEmpty()) {
      throw new IllegalArgumentException("orgUnitCodeは必須です");
    }
    if (orgUnitDto.getOrgUnitTypeCode() == null || orgUnitDto.getOrgUnitTypeCode().isEmpty()) {
      throw new IllegalArgumentException("orgUnitTypeCodeは必須です");
    }
    if (orgUnitDto.getStatus() == null || orgUnitDto.getStatus().isEmpty()) {
      throw new IllegalArgumentException("statusは必須です");
    }
    if (orgUnitDto.getOrgId() == null) {
      throw new IllegalArgumentException("orgIdは必須です");
    }

    Org org = orgRepository.findById(orgUnitDto.getOrgId()).orElseThrow(
        () -> new IllegalArgumentException("指定されたOrgが存在しません: " + orgUnitDto.getOrgId())
    );

    OrgUnit orgUnit = OrgUnit.createNew();
    orgUnit.setPartyId(SnowflakeId.of(snowflakeIdGenerator.nextId()));
    orgUnit.setPartyTypeCode(PartyTypeCode.ORG_UNIT);
    orgUnit.setCode(orgUnitDto.getOrgUnitCode().value());
    orgUnit.setValidFrom(LocalDateTime.now());
    orgUnit.setValidTo(null);
    orgUnit.setGovtAssignedId(null);
    orgUnit.setSearchName(null);
    // 親組織単位IDがnullでない場合は、親組織単位の存在を確認する
    @Nullable SnowflakeId parentOrgUnitId = orgUnitDto.getParentOrgUnitId();
    if (parentOrgUnitId != null) {
      orgUnitRepository.findById(parentOrgUnitId).orElseThrow(
          () -> new IllegalArgumentException("指定された親OrgUnitが存在しません: " + parentOrgUnitId)
      );
    }

    orgUnit.setOrgUnitName(orgUnitDto.getOrgUnitName());
    orgUnit.setValidInterval(ValidInterval.empty());
    orgUnit.setOrgUnitCode(orgUnitDto.getOrgUnitCode());
    orgUnit.setOrgUnitTypeCode(orgUnitDto.getOrgUnitTypeCode());
    orgUnit.setParentOrgUnitId(parentOrgUnitId);
    orgUnit.setStatus(orgUnitDto.getStatus());
    orgUnit.setOrg(org);
    orgUnitRepository.save(orgUnit);
  }
}
