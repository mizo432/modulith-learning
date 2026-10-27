package undecided.shared.sample.jpa.feature.schema;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("small")
@DisplayName("Schema分離パターンの単体テスト")
class SchemaSeparationPatternTest {

  @Nested
  @DisplayName("SchemaSampleEfのテスト")
  class SchemaSampleEfTest {

    @Test
    @DisplayName("SchemaSampleEf.NAMEはundecided_efである")
    void shouldSchemaNameBeUndecidedEf() {
      // Assert
      assertThat(SchemaSampleEf.NAME).isEqualTo("undecided_ef");
    }
  }

  @Nested
  @DisplayName("SampleBankAccountのテスト")
  class SampleBankAccountTest {

    @Test
    @DisplayName("SampleBankAccountが正常に作成される")
    void shouldCreateSampleBankAccount() {
      // Arrange
      Long accountId = 1L;
      String accountNumber = "1234567890";
      Long balance = 100000L;
      String currency = "JPY";

      // Act
      SampleBankAccount account =
          new SampleBankAccount(accountId, accountNumber, balance, currency);

      // Assert
      assertThat(account).isNotNull();
      assertThat(account.getAccountId()).isEqualTo(accountId);
      assertThat(account.getAccountNumber()).isEqualTo(accountNumber);
      assertThat(account.getBalance()).isEqualTo(balance);
      assertThat(account.getCurrency()).isEqualTo(currency);
    }

    @Test
    @DisplayName("@Table.schemaはSchemaSampleEf.NAMEに設定されている")
    void shouldTableSchemaBeSet() {
      // Arrange & Act
      Table table = SampleBankAccount.class.getAnnotation(Table.class);

      // Assert
      assertThat(table).isNotNull();
      assertThat(table.schema()).isEqualTo(SchemaSampleEf.NAME);
      assertThat(table.name()).isEqualTo("sample_bank_accounts");
    }
  }
}
