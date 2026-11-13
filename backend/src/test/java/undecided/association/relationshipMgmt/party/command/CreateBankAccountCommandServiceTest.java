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
import undecided.association.relationshipMgmt.spi.party.bankAccount.AccountNumber;
import undecided.association.relationshipMgmt.spi.party.bankAccount.AccountType;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccount;
import undecided.association.relationshipMgmt.spi.party.bankAccount.HoldersName;
import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.BankAccountDto;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("CreateBankAccountCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreateBankAccountCommandServiceTest {

  private final SnowflakeIdGenerator snowflakeIdGenerator = mock(SnowflakeIdGenerator.class);
  @Mock private BankAccountRepository bankAccountRepository;

  private CreateBankAccountCommandService service;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    BankAccountFactory bankAccountFactory = new BankAccountFactoryImpl(snowflakeIdGenerator);
    service = new CreateBankAccountCommandService(bankAccountFactory, bankAccountRepository);
  }

  @Nested
  @DisplayName("createメソッドの入力検証テスト")
  class InputValidationTest {

    @Test
    @DisplayName("DTOがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenDtoIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.create(null))
          .as("DTOがnullの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("BankAccountDtoはnullであってはいけません");

      verify(bankAccountRepository, never()).save((BankAccount) any());
    }

    @Test
    @DisplayName("bankIdがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenBankIdIsEmpty() {
      // Arrange
      BankAccountDto dto =
          new BankAccountDto(
              SnowflakeId.empty(),
              SnowflakeId.empty(),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.of("1234567"),
              HoldersName.of("田中太郎"));

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .as("bankIdがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("bankIdは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
    }

    @Test
    @DisplayName("bankBranchIdがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenBankBranchIdIsEmpty() {
      // Arrange
      BankAccountDto dto =
          new BankAccountDto(
              SnowflakeId.empty(),
              SnowflakeId.of(1111111111L),
              SnowflakeId.empty(),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.of("1234567"),
              HoldersName.of("田中太郎"));

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .as("bankBranchIdがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("bankBranchIdは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
    }

    @Test
    @DisplayName("accountNumberがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenAccountNumberIsNull() {
      // Arrange
      BankAccountDto dto =
          new BankAccountDto(
              SnowflakeId.empty(),
              SnowflakeId.of(1111111111L),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              null,
              HoldersName.of("田中太郎"));

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .as("accountNumberがnullの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("accountNumberは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
    }

    @Test
    @DisplayName("holdersNameがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenHoldersNameIsNull() {
      // Arrange
      BankAccountDto dto =
          new BankAccountDto(
              SnowflakeId.empty(),
              SnowflakeId.of(1111111111L),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.of("1234567"),
              null);

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .as("holdersNameがnullの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("holdersNameは必須です");

      verify(bankAccountRepository, never()).save((BankAccount) any());
    }
  }

  @Nested
  @DisplayName("createメソッドの正常系テスト")
  class NormalCaseTest {

    @Test
    @DisplayName("有効なDTOが渡された場合、BankAccountが保存されること")
    void shouldSaveBankAccountWhenDtoIsValid() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(1234567890L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      BankAccountDto dto =
          new BankAccountDto(
              SnowflakeId.empty(),
              SnowflakeId.of(1111111111L),
              SnowflakeId.of(2222222222L),
              AccountType.ORDINARY_ACCOUNT,
              AccountNumber.of("1234567"),
              HoldersName.of("田中太郎"));

      // Act
      service.create(dto);

      // Assert
      org.mockito.ArgumentCaptor<BankAccount> captor =
          org.mockito.ArgumentCaptor.forClass(BankAccount.class);
      verify(bankAccountRepository).save(captor.capture());
      BankAccount saved = captor.getValue();
      assertThat(saved.getBankAccountId().value()).isEqualTo(generatedId.value());
      assertThat(saved.getBankId().value()).isEqualTo(SnowflakeId.of(1111111111L).value());
      assertThat(saved.getBankBranchId().value()).isEqualTo(SnowflakeId.of(2222222222L).value());
      assertThat(saved.getType()).isEqualTo(AccountType.ORDINARY_ACCOUNT);
      assertThat(saved.getAccountNumber().value()).isEqualTo("1234567");
      assertThat(saved.getHoldersName().value()).isEqualTo("田中太郎");
    }
  }
}
