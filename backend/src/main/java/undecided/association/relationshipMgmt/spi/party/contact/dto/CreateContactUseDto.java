package undecided.association.relationshipMgmt.spi.party.contact.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import undecided.association.relationshipMgmt.spi.party.contact.Address;
import undecided.association.relationshipMgmt.spi.party.contact.Contact;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 連絡先と用途を同時に作成するためのDTOクラスです。
 *
 * <p>パーティと連絡先アドレスの関連付けを一度に登録するために使用されます。
 */
@Getter
@RequiredArgsConstructor
public class CreateContactUseDto {

  /**
   * 関連するパーティを識別するためのID。
   */
  private final SnowflakeId partyId;

  /**
   * 連絡先アドレス（メールアドレスなど）。
   */
  private final Contact contact;

  /**
   * 電話番号。
   */
  private final PhoneNo phoneNo;

  /**
   * 住所。
   */
  private final Address address;

  /**
   * 連絡先の用途種別（自宅、職場など）。
   */
  private final ContactUseType type;
}
