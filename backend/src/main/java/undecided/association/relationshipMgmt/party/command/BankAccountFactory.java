package undecided.association.relationshipMgmt.party.command;

import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccount;

/**
 * 銀行口座エンティティを作成するためのファクトリインターフェースです。
 */
public interface BankAccountFactory {

  /**
   * 銀行口座エンティティにIDを付与して作成します。
   *
   * @param bankAccount ID未付与の銀行口座エンティティ
   * @return SnowflakeIdが付与された銀行口座エンティティ
   */
  BankAccount create(BankAccount bankAccount);
}
