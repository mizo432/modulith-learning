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
import undecided.association.relationshipMgmt.party.repository.OrgRepository;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.org.Org;
import undecided.association.relationshipMgmt.spi.party.org.OrgCode;
import undecided.association.relationshipMgmt.spi.party.org.OrgName;
import undecided.association.relationshipMgmt.spi.party.org.OrgStatus;
import undecided.association.relationshipMgmt.spi.party.org.OrgTypeCode;
import undecided.association.relationshipMgmt.spi.party.org.dto.OrgDto;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("medium")
@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@DisplayName("CreateOrgCommandServiceの統合テスト")
class CreateOrgCommandServiceMediumTest {

  @Autowired private CreateOrgCommandService service;

  @Autowired private OrgRepository repository;

  @BeforeEach
  void setUp() {
    repository.deleteAll();
  }

  @Nested
  @DisplayName("createメソッドの正常系テスト")
  class NormalCaseTest {

    @Test
    @DisplayName("有効なDTOが渡された場合、Orgがデータベースに保存されること")
    void shouldSaveOrgToDatabase() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.of("テスト組織"),
              OrgCode.of("ORG001"),
              OrgTypeCode.COMPANY,
              null,
              OrgStatus.ACTIVE);

      // Act
      service.create(dto);

      // Assert
      List<Org> saved = repository.findAll();
      assertThat(saved).hasSize(1);

      Org savedEntity = saved.get(0);
      assertThat(savedEntity.getPartyId()).isNotNull();
      assertThat(savedEntity.getPartyId()).isNotEqualTo(SnowflakeId.empty());
      assertThat(savedEntity.getPartyTypeCode()).isEqualTo(PartyTypeCode.ORG);
      assertThat(savedEntity.getCode()).isEqualTo("ORG001");
      assertThat(savedEntity.getOrgName()).isEqualTo(OrgName.of("テスト組織"));
      assertThat(savedEntity.getOrgCode()).isEqualTo(OrgCode.of("ORG001"));
      assertThat(savedEntity.getOrgTypeCode()).isEqualTo(OrgTypeCode.COMPANY);
      assertThat(savedEntity.getStatus()).isEqualTo(OrgStatus.ACTIVE);
      assertThat(savedEntity.getParentOrgId().isEmpty()).isTrue();
      assertThat(savedEntity.getValidFrom()).isNotNull();
      assertThat(savedEntity.getValidTo()).isNull();
    }

    @Test
    @DisplayName("親組織を持つOrgが保存されること")
    void shouldSaveOrgWithParentOrg() {
      // Arrange - 親組織を作成
      Org parentOrg = Org.createNew();
      parentOrg.setPartyId(SnowflakeId.of(1111111111L));
      parentOrg.setPartyTypeCode(PartyTypeCode.ORG);
      parentOrg.setCode("PARENT001");
      parentOrg.setValidFrom(java.time.LocalDateTime.now());
      parentOrg.setOrgName(OrgName.of("親組織"));
      parentOrg.setOrgCode(OrgCode.of("PARENT001"));
      parentOrg.setOrgTypeCode(OrgTypeCode.COMPANY);
      parentOrg.setStatus(OrgStatus.ACTIVE);
      parentOrg = repository.save(parentOrg);

      // 子組織のDTOを作成
      OrgDto dto =
          new OrgDto(
              OrgName.of("子組織"),
              OrgCode.of("ORG002"),
              OrgTypeCode.STORE,
              parentOrg.getPartyId(),
              OrgStatus.ACTIVE);

      // Act
      service.create(dto);

      // Assert
      List<Org> saved = repository.findAll();
      assertThat(saved).hasSize(2);

      Org childOrg = saved.stream()
          .filter(o -> o.getOrgCode().value().equals("ORG002"))
          .findFirst()
          .orElseThrow();

      assertThat(childOrg.getParentOrgId().value()).isEqualTo(parentOrg.getPartyId().value());
    }

    @Test
    @DisplayName("複数のOrgが保存されること")
    void shouldSaveMultipleOrgs() {
      // Arrange
      OrgDto dto1 =
          new OrgDto(
              OrgName.of("組織1"),
              OrgCode.of("ORG001"),
              OrgTypeCode.COMPANY,
              null,
              OrgStatus.ACTIVE);

      OrgDto dto2 =
          new OrgDto(
              OrgName.of("組織2"),
              OrgCode.of("ORG002"),
              OrgTypeCode.STORE,
              null,
              OrgStatus.ACTIVE);

      // Act
      service.create(dto1);
      service.create(dto2);

      // Assert
      List<Org> saved = repository.findAll();
      assertThat(saved).hasSize(2);
    }
  }

  @Nested
  @DisplayName("createメソッドの入力検証テスト")
  class InputValidationTest {

    @Test
    @DisplayName("DTOがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenDtoIsNull() {
      // Act & Assert
      assertThatThrownBy(() -> service.create(null))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("OrgDtoはnullであってはいけません");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("orgNameがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenOrgNameIsNull() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              null,
              OrgCode.of("ORG001"),
              OrgTypeCode.COMPANY,
              null,
              OrgStatus.ACTIVE);

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("orgNameは必須です");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("orgNameがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenOrgNameIsEmpty() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.empty(),
              OrgCode.of("ORG001"),
              OrgTypeCode.COMPANY,
              null,
              OrgStatus.ACTIVE);

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("orgNameは必須です");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("orgCodeがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenOrgCodeIsNull() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.of("テスト組織"),
              null,
              OrgTypeCode.COMPANY,
              null,
              OrgStatus.ACTIVE);

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("orgCodeは必須です");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("orgCodeがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenOrgCodeIsEmpty() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.of("テスト組織"),
              OrgCode.empty(),
              OrgTypeCode.COMPANY,
              null,
              OrgStatus.ACTIVE);

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("orgCodeは必須です");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("orgTypeCodeがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenOrgTypeCodeIsNull() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.of("テスト組織"),
              OrgCode.of("ORG001"),
              null,
              null,
              OrgStatus.ACTIVE);

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("orgTypeCodeは必須です");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("orgTypeCodeがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenOrgTypeCodeIsEmpty() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.of("テスト組織"),
              OrgCode.of("ORG001"),
              OrgTypeCode.empty(),
              null,
              OrgStatus.ACTIVE);

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("orgTypeCodeは必須です");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("statusがnullの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenStatusIsNull() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.of("テスト組織"),
              OrgCode.of("ORG001"),
              OrgTypeCode.COMPANY,
              null,
              null);

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("statusは必須です");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("statusがemptyの場合、IllegalArgumentExceptionをスローすること")
    void shouldThrowExceptionWhenStatusIsEmpty() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.of("テスト組織"),
              OrgCode.of("ORG001"),
              OrgTypeCode.COMPANY,
              null,
              OrgStatus.empty());

      // Act & Assert
      assertThatThrownBy(() -> service.create(dto))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("statusは必須です");

      // Verify nothing is saved
      assertThat(repository.findAll()).isEmpty();
    }
  }

  @Nested
  @DisplayName("OrgTypeCodeバリエーションテスト")
  class OrgTypeCodeVariationTest {

    @Test
    @DisplayName("COMPANYタイプのOrgが保存されること")
    void shouldSaveCompanyTypeOrg() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.of("株式会社テスト"),
              OrgCode.of("COMPANY001"),
              OrgTypeCode.COMPANY,
              null,
              OrgStatus.ACTIVE);

      // Act
      service.create(dto);

      // Assert
      List<Org> saved = repository.findAll();
      assertThat(saved).hasSize(1);
      assertThat(saved.get(0).getOrgTypeCode()).isEqualTo(OrgTypeCode.COMPANY);
    }

    @Test
    @DisplayName("STOREタイプのOrgが保存されること")
    void shouldSaveStoreTypeOrg() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.of("テスト店舗"),
              OrgCode.of("STORE001"),
              OrgTypeCode.STORE,
              null,
              OrgStatus.ACTIVE);

      // Act
      service.create(dto);

      // Assert
      List<Org> saved = repository.findAll();
      assertThat(saved).hasSize(1);
      assertThat(saved.get(0).getOrgTypeCode()).isEqualTo(OrgTypeCode.STORE);
    }

    @Test
    @DisplayName("DEPARTMENTタイプのOrgが保存されること")
    void shouldSaveDepartmentTypeOrg() {
      // Arrange
      OrgDto dto =
          new OrgDto(
              OrgName.of("テスト部署"),
              OrgCode.of("DEPT001"),
              OrgTypeCode.DEPARTMENT,
              null,
              OrgStatus.ACTIVE);

      // Act
      service.create(dto);

      // Assert
      List<Org> saved = repository.findAll();
      assertThat(saved).hasSize(1);
      assertThat(saved.get(0).getOrgTypeCode()).isEqualTo(OrgTypeCode.DEPARTMENT);
    }
  }
}
