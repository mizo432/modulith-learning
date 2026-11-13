package undecided.association.relationshipMgmt.party.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.party.repository.OrgRepository;
import undecided.association.relationshipMgmt.spi.party.CreateOrgCommand;
import undecided.association.relationshipMgmt.spi.party.org.Org;
import undecided.association.relationshipMgmt.spi.party.org.dto.OrgDto;
import java.time.LocalDateTime;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;
import org.jspecify.annotations.Nullable;

/**
 * 組織新規作成のコマンドサービスです。
 *
 * <p>OrgDtoからOrgエンティティを作成し、データベースに保存します。
 */
@RequiredArgsConstructor
@Service
public class CreateOrgCommandService implements CreateOrgCommand {

  private final OrgRepository orgRepository;
  private final SnowflakeIdGenerator snowflakeIdGenerator;

  /**
   * 組織を新規作成します。
   *
   * <p>受け取ったDTOからOrgエンティティを作成し、リポジトリに保存します。
   *
   * @param orgDto 組織情報のDTO。orgName、orgCode、orgTypeCode、parentOrgId、statusを含みます。
   * @throws IllegalArgumentException DTOがnullの場合、または必須フィールドが未設定の場合
   */
  @Override
  @Transactional
  public void create(OrgDto orgDto) {
    if (orgDto == null) {
      throw new IllegalArgumentException("OrgDtoはnullであってはいけません");
    }
    if (orgDto.getOrgName() == null || orgDto.getOrgName().isEmpty()) {
      throw new IllegalArgumentException("orgNameは必須です");
    }
    if (orgDto.getOrgCode() == null || orgDto.getOrgCode().isEmpty()) {
      throw new IllegalArgumentException("orgCodeは必須です");
    }
    if (orgDto.getOrgTypeCode() == null || orgDto.getOrgTypeCode().isEmpty()) {
      throw new IllegalArgumentException("orgTypeCodeは必須です");
    }
    if (orgDto.getStatus() == null || orgDto.getStatus().isEmpty()) {
      throw new IllegalArgumentException("statusは必須です");
    }

    // 親組織IDがnullでない場合は、親組織の存在を確認する
    @Nullable SnowflakeId parentOrgId = orgDto.getParentOrgId();
    if (parentOrgId != null) {
      orgRepository.findById(parentOrgId).orElseThrow(
          () -> new IllegalArgumentException("指定された親Orgが存在しません: " + parentOrgId)
      );
    }

    Org org = Org.create(
        SnowflakeId.of(snowflakeIdGenerator.nextId()),
        orgDto.getOrgCode().value(),
        LocalDateTime.now(),
        orgDto.getOrgName(),
        orgDto.getOrgCode(),
        orgDto.getOrgTypeCode(),
        parentOrgId,
        orgDto.getStatus()
    );
    orgRepository.save(org);
  }
}
