package undecided.association.relationshipMgmt.spi.party.phoneNumber;

import undecided.association.relationshipMgmt.spi.party.phoneNumber.dto.PhoneNumberDto;

/**
 * 電話番号登録のコマンドインターフェースです。
 *
 * <p>新しい電話番号情報を登録するために使用されます。
 */
public interface CreatePhoneNumberCommand {

  /**
   * 電話番号情報を新規登録します。
   *
   * @param phoneNumberDto 電話番号情報のDTO。numberとtypeを含みます。
   */
  void create(PhoneNumberDto phoneNumberDto);
}
