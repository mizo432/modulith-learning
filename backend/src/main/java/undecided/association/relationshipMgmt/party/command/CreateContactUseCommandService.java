package undecided.association.relationshipMgmt.party.command;

import com.github.mizo432.shared.value.datetime.WhenOccured;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.party.repository.ContactUseRepository;
import undecided.association.relationshipMgmt.party.repository.OrgRepository;
import undecided.association.relationshipMgmt.party.repository.OrgUnitRepository;
import undecided.association.relationshipMgmt.party.repository.PersonRepository;
import undecided.association.relationshipMgmt.spi.party.contact.Contact;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.association.relationshipMgmt.spi.party.contact.CreateContactUseCommand;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.association.relationshipMgmt.spi.party.contact.dto.CreateContactUseDto;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * 連絡先と用途を同時に作成するためのコマンドサービスです。
 *
 * <p>CreateContactUseDtoからContactUseエンティティを作成し、データベースに保存します。
 */
@RequiredArgsConstructor
@Service
public class CreateContactUseCommandService implements CreateContactUseCommand {

  private final SnowflakeIdGenerator snowflakeIdGenerator;
  private final ContactUseRepository contactUseRepository;
  private final PersonRepository personRepository;
  private final OrgRepository orgRepository;
  private final OrgUnitRepository orgUnitRepository;

  /**
   * 連絡先情報とその用途情報を新規登録します。
   *
   * <p>受け取ったDTOからContactUseエンティティを作成し、リポジトリに保存します。
   *
   * @param dto 連絡先情報と用途情報のDTO。partyId、contact、phoneNo、typeを含みます。
   * @throws IllegalArgumentException DTOがnullの場合、または必須フィールドが未設定の場合
   */
  @Override
  @Transactional
  public void createAndUse(CreateContactUseDto dto) {
    if (dto == null) {
      throw new IllegalArgumentException("CreateContactUseDtoはnullであってはいけません");
    }
    if (dto.getPartyId() == null || dto.getPartyId().isEmpty()) {
      throw new IllegalArgumentException("partyIdは必須です");
    }
    if (dto.getContact() == null || dto.getContact().isEmpty()) {
      throw new IllegalArgumentException("contactは必須です");
    }

    // パーティの存在を確認
    boolean exists =
        personRepository.existsById(dto.getPartyId())
            || orgRepository.existsById(dto.getPartyId())
            || orgUnitRepository.existsById(dto.getPartyId());
    if (!exists) {
      throw new IllegalArgumentException("指定されたpartyIdを持つパーティが存在しません");
    }

    // 連絡先用途を作成
    SnowflakeId contactUseId = snowflakeIdGenerator.nextIdValue();
    ContactUse contactUse =
        new ContactUse(
            contactUseId,
            dto.getPartyId(),
            dto.getContact(),
            dto.getPhoneNo() != null ? dto.getPhoneNo() : PhoneNo.empty(),
            dto.getType() != null ? dto.getType() : ContactUseType.UNKNOWN,
            WhenOccured.current());
    contactUseRepository.save(contactUse);
  }
}
