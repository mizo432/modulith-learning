package undecided.shared.sample.jpa.feature.schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Schema分離パターンのサンプルエンティティです（undecided_efスキーマ）。
 *
 * <p>{@code @Table.schema}を使用して、エンティティを特定のスキーマに配置します。
 */
@Entity
@Table(schema = SchemaSampleEf.NAME, name = "sample_bank_accounts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleBankAccount {

  @Id
  @Column(name = "account_id", nullable = false)
  private Long accountId;

  @Column(name = "account_number", nullable = false, unique = true, length = 20)
  private String accountNumber;

  @Column(name = "balance", nullable = false)
  private Long balance;

  @Column(name = "currency", nullable = false, length = 3)
  private String currency;

  public SampleBankAccount(Long accountId, String accountNumber, Long balance, String currency) {
    this.accountId = accountId;
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.currency = currency;
  }
}
