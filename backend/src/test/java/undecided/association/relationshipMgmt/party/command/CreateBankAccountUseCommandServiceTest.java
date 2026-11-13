package undecided.association.relationshipMgmt.party.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.association.relationshipMgmt.party.repository.BankAccountRepository;
import undecided.association.relationshipMgmt.party.repository.BankAccountUseRepository;
import undecided.association.relationshipMgmt.spi.party.bankAccount.AccountNumber;
import undecided.association.relationshipMgmt.spi.party.bankAccount.AccountType;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccount;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUse;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUseType;
import undecided.association.relationshipMgmt.spi.party.bankAccount.HoldersName;
import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.CreateBankAccountUseDto;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("CreateBankAccountUseCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreateBankAccountUseCommandServiceTest {

  private final SnowflakeIdGenerator snowflakeIdGenerator = mock(SnowflakeIdGenerator.class);
  @Mock private BankAccountRepository bankAccountRepository;
  @Mock private BankAccountUseRepository bankAccountUseRepository;

  private CreateBankAccountUseCommandService service;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    service =
        new CreateBankAccountUseCommandService(
            snowflakeIdGenerator, bankAccountRepository, bankAccountUseRepository);
  }

  @Nested
  @DisplayName("createAndUseメソッドの入力検証テスト")
  class InputValidationTest {

    @Test
    @DisplayName("DTOがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenDtoIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(null))
          .as("DTOがnullの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("CreateBankAccountUseDtoはnullであってはいけません");

      verify(bankAccountRepository, never()).save((BankAccount) any());
      verify(bankAccountUseRepository, never()).save((BankAccountUse) any());
    }

    @Test
    @DisplayName("bankIdがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenBankIdIsEmpty() {
      // Arrange
      CreateBankAccountUseDto dto =
          new CreateBankAccountUseDto(
              SnowflakeId.empty(),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.of("1234567"),
              HoldersName.of("田中太郎"),
              SnowflakeId.of(3333333333L),
              BankAccountUseType.UNKNOWN);

      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(dto))
          .as("bankIdがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("bankIdは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
      verify(bankAccountUseRepository, never()).save((BankAccountUse) any());
    }

    @Test
    @DisplayName("bankBranchIdがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenBankBranchIdIsEmpty() {
      // Arrange
      CreateBankAccountUseDto dto =
          new CreateBankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.empty(),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.of("1234567"),
              HoldersName.of("田中太郎"),
              SnowflakeId.of(3333333333L),
              BankAccountUseType.UNKNOWN);

      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(dto))
          .as("bankBranchIdがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("bankBranchIdは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
      verify(bankAccountUseRepository, never()).save((BankAccountUse) any());
    }

    @Test
    @DisplayName("accountNumberがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAccountNumberIsNull() {
      // Arrange
      CreateBankAccountUseDto dto =
          new CreateBankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              null,
              HoldersName.of("田中太郎"),
              SnowflakeId.of(3333333333L),
              BankAccountUseType.UNKNOWN);

      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(dto))
          .as("accountNumberがnullの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("accountNumberは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
      verify(bankAccountUseRepository, never()).save((BankAccountUse) any());
    }

    @Test
    @DisplayName("accountNumberがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAccountNumberIsEmpty() {
      // Arrange
      CreateBankAccountUseDto dto =
          new CreateBankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.empty(),
              HoldersName.of("田中太郎"),
              SnowflakeId.of(3333333333L),
              BankAccountUseType.UNKNOWN);

      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(dto))
          .as("accountNumberがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("accountNumberは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
      verify(bankAccountUseRepository, never()).save((BankAccountUse) any());
    }

    @Test
    @DisplayName("holdersNameがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenHoldersNameIsNull() {
      // Arrange
      CreateBankAccountUseDto dto =
          new CreateBankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.of("1234567"),
              null,
              SnowflakeId.of(3333333333L),
              BankAccountUseType.UNKNOWN);

      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(dto))
          .as("holdersNameがnullの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("holdersNameは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
      verify(bankAccountUseRepository, never()).save((BankAccountUse) any());
    }

    @Test
    @DisplayName("holdersNameがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenHoldersNameIsEmpty() {
      // Arrange
      CreateBankAccountUseDto dto =
          new CreateBankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.of("1234567"),
              HoldersName.empty(),
              SnowflakeId.of(3333333333L),
              BankAccountUseType.UNKNOWN);

      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(dto))
          .as("holdersNameがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("holdersNameは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
      verify(bankAccountUseRepository, never()).save((BankAccountUse) any());
    }

    @Test
    @DisplayName("partyIdがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPartyIdIsEmpty() {
      // Arrange
      CreateBankAccountUseDto dto =
          new CreateBankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.of("1234567"),
              HoldersName.of("田中太郎"),
              SnowflakeId.empty(),
              BankAccountUseType.UNKNOWN);

      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(dto))
          .as("partyIdがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("partyIdは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
      verify(bankAccountUseRepository, never()).save((BankAccountUse) any());
    }
  }

  @Nested
  @DisplayName("createAndUseメソッドの正常系テスト")
  class NormalCaseTest {

    @Test
    @DisplayName("有効なDTOが渡された場合、BankAccountとBankAccountUseが保存されること")
    void shouldSaveBankAccountAndUseWhenDtoIsValid() {
      // Arrange
      SnowflakeId bankAccountId = SnowflakeId.of(1234567890L);
      SnowflakeId bankAccountUseId = SnowflakeId.of(1234567891L);
      when(snowflakeIdGenerator.nextIdValue())
          .thenReturn(bankAccountId)
          .thenReturn(bankAccountUseId);

      CreateBankAccountUseDto dto =
          new CreateBankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.of("1234567"),
              HoldersName.of("田中太郎"),
              SnowflakeId.of(3333333333L),
              BankAccountUseType.UNKNOWN);

      // Act
      service.createAndUse(dto);

      // Assert - BankAccountの内容を検証
      org.mockito.ArgumentCaptor<BankAccount> bankAccountCaptor =
          org.mockito.ArgumentCaptor.forClass(BankAccount.class);
      verify(bankAccountRepository).save(bankAccountCaptor.capture());
      BankAccount savedBankAccount = bankAccountCaptor.getValue();
      assertThat(savedBankAccount.getBankAccountId().value()).isEqualTo(bankAccountId.value());
      assertThat(savedBankAccount.getBankId().value()).isEqualTo(SnowflakeId.of(1111111111L).value());
      assertThat(savedBankAccount.getBankBranchId().value()).isEqualTo(SnowflakeId.of(2222222222L).value());
      assertThat(savedBankAccount.getType()).isEqualTo(AccountType.ORDINARY_ACCOUNT);
      assertThat(savedBankAccount.getAccountNumber().value()).isEqualTo("1234567");
      assertThat(savedBankAccount.getHoldersName().value()).isEqualTo("田中太郎");

      // Assert - BankAccountUseの内容を検証
      org.mockito.ArgumentCaptor<BankAccountUse> bankAccountUseCaptor =
          org.mockito.ArgumentCaptor.forClass(BankAccountUse.class);
      verify(bankAccountUseRepository).save(bankAccountUseCaptor.capture());
      BankAccountUse savedBankAccountUse = bankAccountUseCaptor.getValue();
      assertThat(savedBankAccountUse.getBankAccountUseId().value()).isEqualTo(bankAccountUseId.value());
      assertThat(savedBankAccountUse.getBankAccountId().value()).isEqualTo(bankAccountId.value());
      assertThat(savedBankAccountUse.getPartyId().value()).isEqualTo(SnowflakeId.of(3333333333L).value());
      assertThat(savedBankAccountUse.getType()).isEqualTo(BankAccountUseType.UNKNOWN);
    }
  }
}
