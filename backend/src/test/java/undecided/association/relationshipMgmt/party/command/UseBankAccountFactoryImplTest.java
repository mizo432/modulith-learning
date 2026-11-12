package undecided.association.relationshipMgmt.party.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUse;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUseType;
import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.BankAccountUseDto;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("UseBankAccountFactoryImplの単体テスト")
class UseBankAccountFactoryImplTest {

  private final SnowflakeIdGenerator snowflakeIdGenerator = mock(SnowflakeIdGenerator.class);
  private final UseBankAccountFactoryImpl factory =
      new UseBankAccountFactoryImpl(snowflakeIdGenerator);

  @Nested
  @DisplayName("createUseBankAccountメソッドのテスト")
  class CreateUseBankAccountTest {

    @Test
    @DisplayName("BankAccountUseDtoからBankAccountUseを正しく作成すること")
    void shouldCreateBankAccountUseFromDto() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(1234567890L);
      SnowflakeId bankAccountId = SnowflakeId.of(1111111111L);
      SnowflakeId partyId = SnowflakeId.of(2222222222L);

      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      BankAccountUseDto dto =
          new BankAccountUseDto(
              bankAccountId, SnowflakeId.empty(), partyId, BankAccountUseType.UNKNOWN, null);

      // Act
      BankAccountUse result = factory.createUseBankAccount(dto);

      // Assert
      assertThat(result).as("BankAccountUseインスタンスが作成されること").isNotNull();
      assertThat(result.getBankAccountUseId()).as("生成されたIDが設定されること").isEqualTo(generatedId);
      assertThat(result.getBankAccountId()).as("銀行口座IDが設定されること").isEqualTo(bankAccountId);
      assertThat(result.getPartyId()).as("パーティIDが設定されること").isEqualTo(partyId);
      assertThat(result.getType()).as("用途種別が設定されること").isEqualTo(BankAccountUseType.UNKNOWN);
      assertThat(result.getWhenOccured()).as("発生時刻が設定されること").isNotNull();
    }

    @Test
    @DisplayName("異なる値を持つBankAccountUseDtoからBankAccountUseを作成すること")
    void shouldCreateBankAccountUseWithDifferentValues() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(9876543210L);
      SnowflakeId bankAccountId = SnowflakeId.of(3333333333L);
      SnowflakeId partyId = SnowflakeId.of(4444444444L);

      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      BankAccountUseDto dto =
          new BankAccountUseDto(
              bankAccountId, SnowflakeId.empty(), partyId, BankAccountUseType.UNKNOWN, null);

      // Act
      BankAccountUse result = factory.createUseBankAccount(dto);

      // Assert
      assertThat(result).as("BankAccountUseインスタンスが作成されること").isNotNull();
      assertThat(result.getBankAccountUseId()).as("生成されたIDが設定されること").isEqualTo(generatedId);
      assertThat(result.getBankAccountId()).as("銀行口座IDが設定されること").isEqualTo(bankAccountId);
      assertThat(result.getPartyId()).as("パーティIDが設定されること").isEqualTo(partyId);
    }

    @Test
    @DisplayName("作成されたBankAccountUseは元のBankAccountUseDtoと独立したインスタンスであること")
    void shouldCreateIndependentInstance() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(5555555555L);
      SnowflakeId bankAccountId = SnowflakeId.of(6666666666L);
      SnowflakeId partyId = SnowflakeId.of(7777777777L);

      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      BankAccountUseDto dto =
          new BankAccountUseDto(
              bankAccountId, SnowflakeId.empty(), partyId, BankAccountUseType.UNKNOWN, null);

      // Act
      BankAccountUse result = factory.createUseBankAccount(dto);

      // Assert
      assertThat(result).as("作成されたインスタンスはDTOと異なること").isNotSameAs(dto);
    }

    @Test
    @DisplayName("SnowflakeIdGeneratorから生成されたIDが正しく設定されること")
    void shouldSetGeneratedIdFromSnowflakeIdGenerator() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(8888888888L);
      SnowflakeId bankAccountId = SnowflakeId.of(9999999999L);
      SnowflakeId partyId = SnowflakeId.of(1010101010L);

      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      BankAccountUseDto dto =
          new BankAccountUseDto(
              bankAccountId, SnowflakeId.empty(), partyId, BankAccountUseType.UNKNOWN, null);

      // Act
      BankAccountUse result = factory.createUseBankAccount(dto);

      // Assert
      assertThat(result.getBankAccountUseId())
          .as("SnowflakeIdGeneratorから生成されたIDが設定されること")
          .isEqualTo(generatedId);
    }
  }
}
