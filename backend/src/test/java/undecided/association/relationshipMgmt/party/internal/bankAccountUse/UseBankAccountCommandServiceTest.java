package undecided.association.relationshipMgmt.party.internal.bankAccountUse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.mizo432.shared.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
  @Captor private ArgumentCaptor<BankAccountUse> bankAccountUseCaptor;

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
          .isInstanceOf(BusinessException.class)
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
          .isInstanceOf(BusinessException.class)
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
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("partyIdは必須です");

      verify(bankAccountUseRepository, never()).save((BankAccountUse) null);
    }

    @Test
    @DisplayName("bankAccountIdとpartyIdの両方がemptyの場合、bankAccountIdの検証が先に行われること")
    void shouldThrowExceptionForBankAccountIdWhenBothAreEmpty() {
      // Arrange
      BankAccountUseDto dto =
          new BankAccountUseDto(
              SnowflakeId.empty(),
              SnowflakeId.empty(),
              SnowflakeId.empty(),
              BankAccountUseType.UNKNOWN,
              null);

      // Act & Assert
      assertThatThrownBy(() -> service.use(dto))
          .as("bankAccountIdとpartyIdの両方がemptyの場合、bankAccountIdの検証が先に行われること")
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("bankAccountIdは必須です");

      verify(bankAccountUseRepository, never()).save(any(BankAccountUse.class));
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
      verify(bankAccountUseRepository).save(any(BankAccountUse.class));
    }

    @Test
    @DisplayName("保存されたBankAccountUseの内容が正しいこと")
    void shouldSaveBankAccountUseWithCorrectContent() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(1234567890L);
      SnowflakeId bankAccountId = SnowflakeId.of(1111111111L);
      SnowflakeId partyId = SnowflakeId.of(2222222222L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      BankAccountUseDto dto =
          new BankAccountUseDto(
              bankAccountId, SnowflakeId.empty(), partyId, BankAccountUseType.UNKNOWN, null);

      // Act
      service.use(dto);

      // Assert
      verify(bankAccountUseRepository).save(bankAccountUseCaptor.capture());
      BankAccountUse savedEntity = bankAccountUseCaptor.getValue();

      assertThat(savedEntity.getBankAccountUseId()).as("生成されたIDが設定されること").isEqualTo(generatedId);
      assertThat(savedEntity.getBankAccountId()).as("銀行口座IDが設定されること").isEqualTo(bankAccountId);
      assertThat(savedEntity.getPartyId()).as("パーティIDが設定されること").isEqualTo(partyId);
      assertThat(savedEntity.getType()).as("用途種別が設定されること").isEqualTo(BankAccountUseType.UNKNOWN);
      assertThat(savedEntity.getWhenOccured()).as("発生時刻が設定されること").isNotNull();
    }

    @Test
    @DisplayName("複数のBankAccountUseが順に保存されること")
    void shouldSaveMultipleBankAccountUses() {
      // Arrange
      SnowflakeId id1 = SnowflakeId.of(1111111111L);
      SnowflakeId id2 = SnowflakeId.of(2222222222L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(id1, id2);

      BankAccountUseDto dto1 =
          new BankAccountUseDto(
              SnowflakeId.of(3333333333L),
              SnowflakeId.empty(),
              SnowflakeId.of(4444444444L),
              BankAccountUseType.UNKNOWN,
              null);
      BankAccountUseDto dto2 =
          new BankAccountUseDto(
              SnowflakeId.of(5555555555L),
              SnowflakeId.empty(),
              SnowflakeId.of(6666666666L),
              BankAccountUseType.UNKNOWN,
              null);

      // Act
      service.use(dto1);
      service.use(dto2);

      // Assert
      verify(bankAccountUseRepository, org.mockito.Mockito.times(2))
          .save(any(BankAccountUse.class));
    }
  }
}
