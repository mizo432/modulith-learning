package undecided.association.relationshipMgmt.party.internal.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.mizo432.shared.common.exception.BusinessException;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import undecided.association.relationshipMgmt.spi.party.GovtAssignedId;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.SearchName;
import undecided.association.relationshipMgmt.spi.party.person.DateOfBirth;
import undecided.association.relationshipMgmt.spi.party.person.LegalName;
import undecided.association.relationshipMgmt.spi.party.person.Person;
import undecided.supporting.snowflake.SnowflakeId;
import undecided.supporting.snowflake.SnowflakeIdGenerator;

@Tag("small")
@DisplayName("CreatePersonCommandServiceの単体テスト")
@ExtendWith(MockitoExtension.class)
class CreatePersonCommandServiceTest {

  private final SnowflakeIdGenerator snowflakeIdGenerator = mock(SnowflakeIdGenerator.class);
  @Mock private PersonRepository personRepository;
  @Mock private PersonFactory personFactory;

  private CreatePersonCommandService service;

  @BeforeEach
  void setUp() {
    service = new CreatePersonCommandService(personRepository, personFactory);
  }

  // ヘルパーメソッド
  private Person createPerson() {
    LegalName legalName = LegalName.create("田中", "太郎");
    DateOfBirth dateOfBirth = DateOfBirth.of(LocalDate.of(1990, 5, 15));
    GovtAssignedId govtAssignedId = GovtAssignedId.of("1234567890123");
    SearchName searchName = SearchName.of("田中太郎");

    return new Person(
        SnowflakeId.of(1L),
        PartyTypeCode.PERSON,
        "Tanaka",
        "T001",
        govtAssignedId,
        searchName,
        legalName,
        dateOfBirth,
        Collections.emptyList());
  }

  private Person createPersonWithNullCode() {
    return new Person(
        SnowflakeId.of(1L),
        PartyTypeCode.PERSON,
        "Smith",
        null,
        null,
        null,
        LegalName.empty(),
        DateOfBirth.empty(),
        Collections.emptyList());
  }

  private Person createFactoryPerson(SnowflakeId id) {
    LegalName legalName = LegalName.create("田中", "太郎");
    DateOfBirth dateOfBirth = DateOfBirth.of(LocalDate.of(1990, 5, 15));
    GovtAssignedId govtAssignedId = GovtAssignedId.of("1234567890123");
    SearchName searchName = SearchName.of("田中太郎");

    return new Person(
        id,
        PartyTypeCode.PERSON,
        "Tanaka",
        "T001",
        govtAssignedId,
        searchName,
        legalName,
        dateOfBirth,
        Collections.emptyList());
  }

  @Nested
  @DisplayName("createメソッドの入力検証テスト")
  class InputValidationTest {

    @Test
    @DisplayName("Personがnullの場合、BusinessExceptionをスローすること")
    void shouldThrowExceptionWhenPersonIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.create(null))
          .as("Personがnullの場合にBusinessExceptionがスローされること")
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("個人情報はnullにできません");

      verify(personFactory, never()).create((Person) null);
      verify(personRepository, never()).save((Person) null);
    }
  }

  @Nested
  @DisplayName("createメソッドの重複キーテスト")
  class DuplicateKeyTest {

    @Test
    @DisplayName("重複するcodeで保存しようとした場合、DataIntegrityViolationExceptionをBusinessExceptionに変換すること")
    void shouldConvertDataIntegrityViolationExceptionToBusinessException() {
      // Arrange
      Person input = createPerson();
      Person factoryPerson = createFactoryPerson(SnowflakeId.of(1234567890L));
      when(personFactory.create(input)).thenReturn(factoryPerson);
      when(personRepository.save(any(Person.class)))
          .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

      // Act & Assert
      assertThatThrownBy(() -> service.create(input))
          .as("重複キーでBusinessExceptionがスローされること")
          .isInstanceOf(BusinessException.class)
          .hasMessageContaining("指定されたコードを持つ個人は既に存在します")
          .hasMessageContaining("T001")
          .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }
  }

  @Nested
  @DisplayName("createメソッドの正常系テスト")
  class NormalCaseTest {

    @Test
    @DisplayName("有効なPersonが渡された場合、FactoryとRepositoryが呼び出され、SnowflakeIdが返されること")
    void shouldCreatePersonAndReturnId() {
      // Arrange
      SnowflakeId expectedId = SnowflakeId.of(1234567890L);
      Person input = createPerson();
      Person factoryPerson = createFactoryPerson(expectedId);
      Person savedPerson = createFactoryPerson(expectedId);

      when(personFactory.create(input)).thenReturn(factoryPerson);
      when(personRepository.save(factoryPerson)).thenReturn(savedPerson);

      // Act
      SnowflakeId result = service.create(input);

      // Assert
      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(expectedId);
      verify(personFactory).create(input);
      verify(personRepository).save(factoryPerson);
    }

    @Test
    @DisplayName("codeがnullのPersonでも正常に作成されること")
    void shouldCreatePersonWithNullCode() {
      // Arrange
      SnowflakeId expectedId = SnowflakeId.of(9876543210L);
      Person input = createPersonWithNullCode();
      Person factoryPerson = createFactoryPerson(expectedId);
      Person savedPerson = createFactoryPerson(expectedId);

      when(personFactory.create(input)).thenReturn(factoryPerson);
      when(personRepository.save(factoryPerson)).thenReturn(savedPerson);

      // Act
      SnowflakeId result = service.create(input);

      // Assert
      assertThat(result).isEqualTo(expectedId);
      verify(personFactory).create(input);
      verify(personRepository).save(factoryPerson);
    }

    @Test
    @DisplayName("作成されたPersonのIDが返されること")
    void shouldReturnCreatedPersonId() {
      // Arrange
      SnowflakeId expectedId = SnowflakeId.of(5555555555L);
      Person input = createPerson();
      Person factoryPerson = createFactoryPerson(expectedId);
      Person savedPerson = createFactoryPerson(expectedId);

      when(personFactory.create(input)).thenReturn(factoryPerson);
      when(personRepository.save(factoryPerson)).thenReturn(savedPerson);

      // Act
      SnowflakeId result = service.create(input);

      // Assert
      assertThat(result).isEqualTo(expectedId);
    }
  }
}
