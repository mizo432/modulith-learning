package undecided.association.relationshipMgmt.party.internal.bankAccountUse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

    @Test
    @DisplayName("DTOがnullの場合、NullPointerExceptionをスローすること")
    void shouldThrowExceptionWhenDtoIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> factory.createUseBankAccount(null))
          .as("DTOがnullの場合に例外がスローされること")
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("複数回呼び出した場合、それぞれ異なるIDが生成されること")
    void shouldGenerateDifferentIdsOnMultipleCalls() {
      // Arrange
      SnowflakeId id1 = SnowflakeId.of(1111111111L);
      SnowflakeId id2 = SnowflakeId.of(2222222222L);
      SnowflakeId bankAccountId = SnowflakeId.of(3333333333L);
      SnowflakeId partyId = SnowflakeId.of(4444444444L);

      when(snowflakeIdGenerator.nextIdValue()).thenReturn(id1, id2);

      BankAccountUseDto dto1 =
          new BankAccountUseDto(
              bankAccountId, SnowflakeId.empty(), partyId, BankAccountUseType.UNKNOWN, null);
      BankAccountUseDto dto2 =
          new BankAccountUseDto(
              bankAccountId, SnowflakeId.empty(), partyId, BankAccountUseType.UNKNOWN, null);

      // Act
      BankAccountUse result1 = factory.createUseBankAccount(dto1);
      BankAccountUse result2 = factory.createUseBankAccount(dto2);

      // Assert
      assertThat(result1.getBankAccountUseId())
          .as("1回目の呼び出しでid1が設定されること")
          .isEqualTo(id1);
      assertThat(result2.getBankAccountUseId())
          .as("2回目の呼び出しでid2が設定されること")
          .isEqualTo(id2);
      assertThat(result1.getBankAccountUseId())
          .as("2つのIDは異なること")
          .isNotEqualTo(result2.getBankAccountUseId());
    }

    @Test
    @DisplayName("SnowflakeIdGeneratorが呼び出されること")
    void shouldCallSnowflakeIdGenerator() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(7777777777L);
      SnowflakeId bankAccountId = SnowflakeId.of(8888888888L);
      SnowflakeId partyId = SnowflakeId.of(9999999999L);

      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      BankAccountUseDto dto =
          new BankAccountUseDto(
              bankAccountId, SnowflakeId.empty(), partyId, BankAccountUseType.UNKNOWN, null);

      // Act
      factory.createUseBankAccount(dto);

      // Assert
      verify(snowflakeIdGenerator).nextIdValue();
    }

    @Test
    @DisplayName("WhenOccuredが現在時刻で設定されること")
    void shouldSetWhenOccuredToCurrentTime() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(6666666666L);
      SnowflakeId bankAccountId = SnowflakeId.of(5555555555L);
      SnowflakeId partyId = SnowflakeId.of(4444444444L);

      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      BankAccountUseDto dto =
          new BankAccountUseDto(
              bankAccountId, SnowflakeId.empty(), partyId, BankAccountUseType.UNKNOWN, null);

      // Act
      BankAccountUse result = factory.createUseBankAccount(dto);

      // Assert
      assertThat(result.getWhenOccured())
          .as("発生時刻がnullでないこと")
          .isNotNull();
      assertThat(result.getWhenOccured().value())
          .as("発生時刻の値がnullでないこと")
          .isNotNull();
    }
  }
}
