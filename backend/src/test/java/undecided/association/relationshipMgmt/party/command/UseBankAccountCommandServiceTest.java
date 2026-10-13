package undecided.association.relationshipMgmt.party.command;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
import undecided.association.relationshipMgmt.party.repository.BankAccountUseRepository;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUse;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUseType;
import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.BankAccountUseDto;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("UseBankAccountCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class UseBankAccountCommandServiceTest {

  private final SnowflakeIdGenerator snowflakeIdGenerator = mock(SnowflakeIdGenerator.class);
  @Mock private BankAccountUseRepository bankAccountUseRepository;

  private UseBankAccountCommandService service;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    UseBankAccountFactory factory = new UseBankAccountFactoryImpl(snowflakeIdGenerator);
    service = new UseBankAccountCommandService(factory, bankAccountUseRepository);
  }

  @Nested
  @DisplayName("useメソッドの入力検証テスト")
  class InputValidationTest {

    @Test
    @DisplayName("DTOがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenDtoIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.use(null))
          .as("DTOがnullの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("BankAccountUseDtoはnullであってはいけません");

      verify(bankAccountUseRepository, never()).save((BankAccountUse) null);
    }

    @Test
    @DisplayName("bankAccountIdがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenBankAccountIdIsEmpty() {
      // Arrange
      BankAccountUseDto dto =
          new BankAccountUseDto(
              SnowflakeId.empty(),
              SnowflakeId.empty(),
              SnowflakeId.of(2222222222L),
              BankAccountUseType.UNKNOWN,
              null);

      // Act & Assert
      assertThatThrownBy(() -> service.use(dto))
          .as("bankAccountIdがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("bankAccountIdは必須です");

      verify(bankAccountUseRepository, never()).save((BankAccountUse) null);
    }

    @Test
    @DisplayName("partyIdがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPartyIdIsEmpty() {
      // Arrange
      BankAccountUseDto dto =
          new BankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.empty(),
              SnowflakeId.empty(),
              BankAccountUseType.UNKNOWN,
              null);

      // Act & Assert
      assertThatThrownBy(() -> service.use(dto))
          .as("partyIdがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("partyIdは必須です");

      verify(bankAccountUseRepository, never()).save((BankAccountUse) null);
    }
  }

  @Nested
  @DisplayName("useメソッドの正常系テスト")
  class NormalCaseTest {

    @Test
    @DisplayName("有効なDTOが渡された場合、BankAccountUseが保存されること")
    void shouldSaveBankAccountUseWhenDtoIsValid() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(1234567890L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      BankAccountUseDto dto =
          new BankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.empty(),
              SnowflakeId.of(2222222222L),
              BankAccountUseType.UNKNOWN,
              null);

      // Act
      service.use(dto);

      // Assert
      verify(bankAccountUseRepository).save((BankAccountUse) org.mockito.ArgumentMatchers.any());
    }
  }
}
