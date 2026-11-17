package undecided.association.relationshipMgmt.spi.party.contact;

import undecided.association.relationshipMgmt.spi.party.contact.dto.CreateContactUseDto;

/**
 * 連絡先と用途を同時に作成するためのコマンドインターフェースです。
 *
 * <p>新しい連絡先情報とそのパーティとの関連付けを一度に登録するために使用されます。
 */
public interface CreateContactUseCommand {

  /**
   * 連絡先情報とその用途情報を新規登録します。
   *
   * @param dto 連絡先情報と用途情報のDTO。partyId、contact、phoneNo、typeを含みます。
   */
  void createAndUse(CreateContactUseDto dto);
}
