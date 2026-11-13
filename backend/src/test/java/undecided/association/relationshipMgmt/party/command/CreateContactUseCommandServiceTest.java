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
import undecided.association.relationshipMgmt.party.repository.ContactUseRepository;
import undecided.association.relationshipMgmt.party.repository.OrgRepository;
import undecided.association.relationshipMgmt.party.repository.OrgUnitRepository;
import undecided.association.relationshipMgmt.party.repository.PersonRepository;
import undecided.association.relationshipMgmt.spi.party.contact.Address;
import undecided.association.relationshipMgmt.spi.party.contact.Contact;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.association.relationshipMgmt.spi.party.contact.dto.CreateContactUseDto;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("CreateContactUseCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreateContactUseCommandServiceTest {

  private final SnowflakeIdGenerator snowflakeIdGenerator = mock(SnowflakeIdGenerator.class);
  @Mock private ContactUseRepository contactUseRepository;
  @Mock private PersonRepository personRepository;
  @Mock private OrgRepository orgRepository;
  @Mock private OrgUnitRepository orgUnitRepository;

  private CreateContactUseCommandService service;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    service =
        new CreateContactUseCommandService(
            snowflakeIdGenerator,
            contactUseRepository,
            personRepository,
            orgRepository,
            orgUnitRepository);
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
          .hasMessageContaining("CreateContactUseDtoはnullであってはいけません");

      verify(contactUseRepository, never()).save((ContactUse) any());
    }

    @Test
    @DisplayName("partyIdがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenPartyIdIsEmpty() {
      // Arrange
      CreateContactUseDto dto =
          new CreateContactUseDto(
              SnowflakeId.empty(),
              Contact.of("user@example.com"),
              PhoneNo.of("090-1234-5678"),
              Address.empty(),
              ContactUseType.HOME);

      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(dto))
          .as("partyIdがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("partyIdは必須です");

      verify(contactUseRepository, never()).save((ContactUse) any());
    }

    @Test
    @DisplayName("contactがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenContactIsNull() {
      // Arrange
      CreateContactUseDto dto =
          new CreateContactUseDto(
              SnowflakeId.of(1111111111L),
              null,
              PhoneNo.of("090-1234-5678"),
              Address.empty(),
              ContactUseType.HOME);

      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(dto))
          .as("contactがnullの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("contactは必須です");

      verify(contactUseRepository, never()).save((ContactUse) any());
    }

    @Test
    @DisplayName("contactがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenContactIsEmpty() {
      // Arrange
      CreateContactUseDto dto =
          new CreateContactUseDto(
              SnowflakeId.of(1111111111L),
              Contact.empty(),
              PhoneNo.of("090-1234-5678"),
              Address.empty(),
              ContactUseType.HOME);

      // Act & Assert
      assertThatThrownBy(() -> service.createAndUse(dto))
          .as("contactがemptyの場合に例外がスローされること")
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("contactは必須です");

      verify(contactUseRepository, never()).save((ContactUse) any());
    }
  }

  @Nested
  @DisplayName("createAndUseメソッドの正常系テスト")
  class NormalCaseTest {

    @Test
    @DisplayName("有効なDTOが渡された場合、ContactUseが保存されること")
    void shouldSaveContactUseWhenDtoIsValid() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(1234567890L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);
      when(personRepository.existsById(any())).thenReturn(true);

      CreateContactUseDto dto =
          new CreateContactUseDto(
              SnowflakeId.of(1111111111L),
              Contact.of("user@example.com"),
              PhoneNo.of("090-1234-5678"),
              Address.of("東京都"),
              ContactUseType.HOME);

      // Act
      service.createAndUse(dto);

      // Assert
      org.mockito.ArgumentCaptor<ContactUse> captor =
          org.mockito.ArgumentCaptor.forClass(ContactUse.class);
      verify(contactUseRepository).save(captor.capture());
      ContactUse saved = captor.getValue();
      assertThat(saved.getContactUseId().value()).isEqualTo(generatedId.value());
      assertThat(saved.getPartyId().value()).isEqualTo(SnowflakeId.of(1111111111L).value());
      assertThat(saved.getContact().value()).isEqualTo("user@example.com");
      assertThat(saved.getPhoneNo().value()).isEqualTo("090-1234-5678");
      assertThat(saved.getAddress().value()).isEqualTo("東京都");
      assertThat(saved.getType()).isEqualTo(ContactUseType.HOME);
    }

    @Test
    @DisplayName("phoneNoとtypeがnullの場合も正常に保存されること")
    void shouldSaveContactUseWhenPhoneNoAndTypeAreNull() {
      // Arrange
      SnowflakeId generatedId = SnowflakeId.of(1234567890L);
      when(snowflakeIdGenerator.nextIdValue()).thenReturn(generatedId);
      when(personRepository.existsById(any())).thenReturn(true);

      CreateContactUseDto dto =
          new CreateContactUseDto(
              SnowflakeId.of(1111111111L),
              Contact.of("user@example.com"),
              null,
              null,
              null);

      // Act
      service.createAndUse(dto);

      // Assert
      verify(contactUseRepository).save(any(ContactUse.class));
    }
  }
}
