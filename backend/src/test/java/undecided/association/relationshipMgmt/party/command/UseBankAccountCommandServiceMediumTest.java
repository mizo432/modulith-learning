package undecided.association.relationshipMgmt.party.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import undecided.TestcontainersConfiguration;
import undecided.association.relationshipMgmt.party.repository.BankAccountUseRepository;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUse;
import undecided.association.relationshipMgmt.spi.party.bankAccount.BankAccountUseType;
import undecided.association.relationshipMgmt.spi.party.bankAccount.dto.BankAccountUseDto;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("UseBankAccountCommandServiceの統合テスト")
class UseBankAccountCommandServiceMediumTest {

  @Autowired private UseBankAccountCommandService service;

  @Autowired private BankAccountUseRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Nested
  @DisplayName("useメソッドの正常系テスト")
  class NormalCaseTest {

    @Test
    @DisplayName("有効なDTOが渡された場合、BankAccountUseがデータベースに保存されること")
    void shouldSaveBankAccountUseToDatabase() {
      // Arrange
      SnowflakeId bankAccountId = SnowflakeId.of(1111111111L);
      SnowflakeId partyId = SnowflakeId.of(2222222222L);

      BankAccountUseDto dto =
          new BankAccountUseDto(
              bankAccountId, SnowflakeId.empty(), partyId, BankAccountUseType.UNKNOWN, null);

      // Act
      service.use(dto);

      // Assert
      List<BankAccountUse> saved = repository.findAll();
      assertThat(saved).hasSize(1);

      BankAccountUse savedEntity = saved.get(0);
      assertThat(savedEntity.getBankAccountId().value()).isEqualTo(bankAccountId.value());
      assertThat(savedEntity.getPartyId().value()).isEqualTo(partyId.value());
      assertThat(savedEntity.getType()).isEqualTo(BankAccountUseType.UNKNOWN);
      assertThat(savedEntity.getBankAccountUseId()).isNotNull();
      assertThat(savedEntity.getBankAccountUseId()).isNotEqualTo(SnowflakeId.empty());
      assertThat(savedEntity.getWhenOccured()).isNotNull();
    }

    @Test
    @DisplayName("複数のBankAccountUseが保存されること")
    void shouldSaveMultipleBankAccountUses() {
      // Arrange
      BankAccountUseDto dto1 =
          new BankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.empty(),
              SnowflakeId.of(2222222222L),
              BankAccountUseType.UNKNOWN,
              null);
      BankAccountUseDto dto2 =
          new BankAccountUseDto(
              SnowflakeId.of(3333333333L),
              SnowflakeId.empty(),
              SnowflakeId.of(4444444444L),
              BankAccountUseType.UNKNOWN,
              null);

      // Act
      service.use(dto1);
      service.use(dto2);

      // Assert
      List<BankAccountUse> saved = repository.findAll();
      assertThat(saved).hasSize(2);
    }
  }

  @Nested
  @DisplayName("useメソッドの入力検証テスト")
  class InputValidationTest {

    @Test
    @DisplayName("DTOがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenDtoIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.use(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("BankAccountUseDtoはnullであってはいけません");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
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
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("bankAccountIdは必須です");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
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
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("partyIdは必須です");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
    }
  }

  @Nested
  @DisplayName("トランザクションテスト")
  class TransactionTest {

    @Test
    @DisplayName("例外発生時にデータがロールバックされること")
    void shouldRollbackWhenExceptionOccurs() {
      // Arrange
      BankAccountUseDto validDto =
          new BankAccountUseDto(
              SnowflakeId.of(1111111111L),
              SnowflakeId.empty(),
              SnowflakeId.of(2222222222L),
              BankAccountUseType.UNKNOWN,
              null);

      // Act - First save a valid record
      service.use(validDto);
      assertThat(repository.findAll()).hasSize(1);

      // Then try to save with invalid data (should throw and not affect previous save)
      BankAccountUseDto invalidDto =
          new BankAccountUseDto(
              SnowflakeId.empty(),
              SnowflakeId.empty(),
              SnowflakeId.empty(),
              BankAccountUseType.UNKNOWN,
              null);

      // Assert
      assertThatThrownBy(() -> service.use(invalidDto))
          .isInstanceOf(IllegalArgumentException.class);

      // The valid record should still exist
      List<BankAccountUse> saved = repository.findAll();
      assertThat(saved).hasSize(1);
    }
  }
}
