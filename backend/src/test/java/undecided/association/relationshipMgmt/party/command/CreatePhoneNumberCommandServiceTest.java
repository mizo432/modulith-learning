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
import undecided.association.relationshipMgmt.party.repository.PhoneNumberRepository;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.PhoneNumber;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.PhoneNumberEntity;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.PhoneNumberType;
import undecided.association.relationshipMgmt.spi.party.phoneNumber.dto.PhoneNumberDto;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("CreatePhoneNumberCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreatePhoneNumberCommandServiceTest {

  private final SnowflakeIdGenerator snowflakeIdGenerator = mock(SnowflakeIdGenerator.class);
  @Mock private PhoneNumberRepository phoneNumberRepository;

  private CreatePhoneNumberCommandService service;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    service =
        new CreatePhoneNumberCommandService(snowflakeIdGenerator, phoneNumberRepository);
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
          .hasMessageContaining("PhoneNumberDtoはnullであってはいけません");

      verify(phoneNumberRepository, never()).save((PhoneNumberEntity) any());
    }

    @Test
    @DisplayName("numberがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenNumberIsNull() {
      // Arrange
      PhoneNumberDto dto = new PhoneNumberDto(SnowflakeId.empty(), null, PhoneNumberType.MOBILE);

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .as("numberがnullの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("numberは必須です");

      verify(phoneNumberRepository, never()).save((PhoneNumberEntity) any());
    }

    @Test
    @DisplayName("numberがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenNumberIsEmpty() {
      // Arrange
      PhoneNumberDto dto =
          new PhoneNumberDto(
              SnowflakeId.empty(), PhoneNumber.empty(), PhoneNumberType.MOBILE);

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .as("numberがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("numberは必須です");

      verify(phoneNumberRepository, never()).save((PhoneNumberEntity) any());
    }
  }

  @Nested
  @DisplayName("createメソッドの正常系テスト")
  class NormalCaseTest {

    @Test
    @DisplayName("有効なDTOが渡された場合、PhoneNumberEntityが保存されること")
    void shouldSavePhoneNumberEntityWhenDtoIsValid() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(1234567890L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      PhoneNumberDto dto =
          new PhoneNumberDto(
              SnowflakeId.empty(),
              PhoneNumber.of("090-1234-5678"),
              PhoneNumberType.MOBILE);

      // Act
      service.create(dto);

      // Assert
      org.mockito.ArgumentCaptor<PhoneNumberEntity> captor =
          org.mockito.ArgumentCaptor.forClass(PhoneNumberEntity.class);
      verify(phoneNumberRepository).save(captor.capture());
      PhoneNumberEntity saved = captor.getValue();
      assertThat(saved.getPhoneNumberId()).isEqualTo(generatedId);
      assertThat(saved.getNumber().value()).isEqualTo("090-1234-5678");
      assertThat(saved.getType()).isEqualTo(PhoneNumberType.MOBILE);
    }

    @Test
    @DisplayName("typeがnullの場合も正常に保存されること")
    void shouldSavePhoneNumberEntityWhenTypeIsNull() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(1234567890L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);

      PhoneNumberDto dto =
          new PhoneNumberDto(
              SnowflakeId.empty(), PhoneNumber.of("090-1234-5678"), null);

      // Act
      service.create(dto);

      // Assert
      verify(phoneNumberRepository).save(any(PhoneNumberEntity.class));
    }
  }
}
