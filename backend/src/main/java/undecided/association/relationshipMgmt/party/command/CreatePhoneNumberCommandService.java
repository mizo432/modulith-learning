package undecided.association.relationshipMgmt.party.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.association.relationshipMgmt.party.repository.PhoneNumberRepository;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.CreatePhoneNumberCommand;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.PhoneNumberEntity;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.PhoneNumberType;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.dto.PhoneNumberDto;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

/**
 * 電話番号登録のコマンドサービスです。
 *
 * <p>PhoneNumberDtoからPhoneNumberEntityエンティティを作成し、データベースに保存します。
 */
@RequiredArgsConstructor
@Service
public class CreatePhoneNumberCommandService implements CreatePhoneNumberCommand {

  private final SnowflakeIdGenerator snowflakeIdGenerator;
  private final PhoneNumberRepository phoneNumberRepository;

  /**
   * 電話番号情報を新規登録します。
   *
   * <p>受け取ったDTOからPhoneNumberEntityエンティティを作成し、Snowflake IDを付与した後、リポジトリに保存します。
   *
   * @param phoneNumberDto 電話番号情報のDTO。numberとtypeを含みます。
   * @throws IllegalArgumentException DTOがnullの場合、または必須フィールドが未設定の場合
   */
  @Override
  @Transactional
  public void create(PhoneNumberDto phoneNumberDto) {
    if (phoneNumberDto == null) {
      throw new IllegalArgumentException("PhoneNumberDtoはnullであってはいけません");
    }
    if (phoneNumberDto.getNumber() == null || phoneNumberDto.getNumber().isEmpty()) {
      throw new IllegalArgumentException("numberは必須です");
    }

    SnowflakeId phoneNumberId = snowflakeIdGenerator.nextIdValue();
    PhoneNumberEntity phoneNumberEntity =
        PhoneNumberEntity.create(
            phoneNumberId,
            phoneNumberDto.getNumber(),
            phoneNumberDto.getType() != null ? phoneNumberDto.getType() : PhoneNumberType.UNKNOWN);
    phoneNumberRepository.save(phoneNumberEntity);
  }
}
