package undecided.association.relationshipMgmt.spi.party.phoneNumber.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.PhoneNumber;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.PhoneNumberType;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 電話番号情報を表すDTOクラスです。
 *
 * <p>電話番号の新規登録時に使用されます。
 */
@Getter
@AllArgsConstructor
public class PhoneNumberDto {

  /**
   * 電話番号を一意に識別するためのID。
   */
  private SnowflakeId phoneNumberId = SnowflakeId.EMPTY;

  /**
   * 電話番号。
   */
  private PhoneNumber number;

  /**
   * 電話番号の種別（携帯電話、固定電話、FAXなど）。
   */
  private PhoneNumberType type = PhoneNumberType.UNKNOWN;

  protected PhoneNumberDto() {
    phoneNumberId = SnowflakeId.EMPTY;
    number = PhoneNumber.empty();
    type = PhoneNumberType.UNKNOWN;
  }
}
