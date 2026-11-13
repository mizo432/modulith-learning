package undecided.association.relationshipMgmt.party.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import undecided.association.relationshipMgmt.party.repository.ContactUseRepository;
import undecided.association.relationshipMgmt.spi.party.contact.Contact;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUse;
import undecided.association.relationshipMgmt.spi.party.contact.ContactUseType;
import undecided.association.relationshipMgmt.spi.party.contact.PhoneNo;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("ContactQueryServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class ContactQueryServiceTest {

  @Mock private ContactUseRepository contactUseRepository;

  private ContactQueryService service;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    service = new ContactQueryService(contactUseRepository);
  }

  @Nested
  @DisplayName("findAllメソッドのテスト")
  class FindAllMethodTest {

    @Test
    @DisplayName("全ての連絡先情報が取得されること")
    void shouldReturnAllContactUses() {
      // Arrange
      ContactUse contactUse1 =
          new ContactUse(
              SnowflakeId.of(1L),
              SnowflakeId.of(100L),
              Contact.of("user1@example.com"),
              PhoneNo.of("090-1111-1111"),
              ContactUseType.HOME,
              null);
      ContactUse contactUse2 =
          new ContactUse(
              SnowflakeId.of(2L),
              SnowflakeId.of(100L),
              Contact.of("user2@example.com"),
              PhoneNo.of("090-2222-2222"),
              ContactUseType.WORK,
              null);

      when(contactUseRepository.findAll()).thenReturn(List.of(contactUse1, contactUse2));

      // Act
      List<ContactUse> result = service.findAll();

      // Assert
      assertThat(result).as("全ての連絡先情報が取得されること").hasSize(2);
      assertThat(result.get(0).getContact().value()).isEqualTo("user1@example.com");
      assertThat(result.get(1).getContact().value()).isEqualTo("user2@example.com");
    }

    @Test
    @DisplayName("データが存在しない場合は空のリストが返されること")
    void shouldReturnEmptyListWhenNoData() {
      // Arrange
      when(contactUseRepository.findAll()).thenReturn(List.of());

      // Act
      List<ContactUse> result = service.findAll();

      // Assert
      assertThat(result).as("空のリストが返されること").isEmpty();
    }
  }

  @Nested
  @DisplayName("findByIdメソッドのテスト")
  class FindByIdMethodTest {

    @Test
    @DisplayName("存在するIDで連絡先情報が取得されること")
    void shouldReturnContactUseWhenIdExists() {
      // Arrange
      SnowflakeId id = SnowflakeId.of(1L);
      ContactUse contactUse =
          new ContactUse(
              id,
              SnowflakeId.of(100L),
              Contact.of("user@example.com"),
              PhoneNo.of("090-1234-5678"),
              ContactUseType.HOME,
              null);

      when(contactUseRepository.findById(id)).thenReturn(Optional.of(contactUse));

      // Act
      Optional<ContactUse> result = service.findById(id);

      // Assert
      assertThat(result).as("連絡先情報が取得されること").isPresent();
      assertThat(result.get().getContact().value()).isEqualTo("user@example.com");
    }

    @Test
    @DisplayName("存在しないIDで検索するとOptional.emptyが返されること")
    void shouldReturnEmptyWhenIdNotFound() {
      // Arrange
      SnowflakeId id = SnowflakeId.of(999L);
      when(contactUseRepository.findById(id)).thenReturn(Optional.empty());

      // Act
      Optional<ContactUse> result = service.findById(id);

      // Assert
      assertThat(result).as("Optional.emptyが返されること").isEmpty();
    }
  }

  @Nested
  @DisplayName("findByPartyIdメソッドのテスト")
  class FindByPartyIdMethodTest {

    @Test
    @DisplayName("パーティIDに関連する連絡先情報が取得されること")
    void shouldReturnContactUsesByPartyId() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(100L);
      ContactUse contactUse1 =
          new ContactUse(
              SnowflakeId.of(1L),
              partyId,
              Contact.of("user1@example.com"),
              PhoneNo.of("090-1111-1111"),
              ContactUseType.HOME,
              null);
      ContactUse contactUse2 =
          new ContactUse(
              SnowflakeId.of(2L),
              partyId,
              Contact.of("user2@example.com"),
              PhoneNo.of("090-2222-2222"),
              ContactUseType.WORK,
              null);

      when(contactUseRepository.findByPartyId(partyId)).thenReturn(List.of(contactUse1, contactUse2));

      // Act
      List<ContactUse> result = service.findByPartyId(partyId);

      // Assert
      assertThat(result).as("パーティに関連する連絡先情報が取得されること").hasSize(2);
      assertThat(result.get(0).getPartyId()).isEqualTo(partyId);
      assertThat(result.get(1).getPartyId()).isEqualTo(partyId);
    }

    @Test
    @DisplayName("関連する連絡先がない場合は空のリストが返されること")
    void shouldReturnEmptyListWhenNoContactUsesForParty() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(999L);
      when(contactUseRepository.findByPartyId(partyId)).thenReturn(List.of());

      // Act
      List<ContactUse> result = service.findByPartyId(partyId);

      // Assert
      assertThat(result).as("空のリストが返されること").isEmpty();
    }
  }
}
