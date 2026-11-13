package undecided.association.relationshipMgmt.spi.party.orgUnit;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.association.relationshipMgmt.spi.party.Party;
import undecided.association.relationshipMgmt.spi.party.PartyTypeCode;
import undecided.association.relationshipMgmt.spi.party.org.Org;
import undecided.association.relationshipMgmt.spi.party.org.OrgCode;
import undecided.association.relationshipMgmt.spi.party.org.OrgName;
import undecided.association.relationshipMgmt.spi.party.org.OrgStatus;
import undecided.association.relationshipMgmt.spi.party.org.OrgTypeCode;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@code OrgUnitTest} は、OrgUnitエンティティの単体テストクラスです。
 * <p>
 * C3修正で追加された{@code create()}ファクトリメソッドの動作を検証します。
 */
@Tag("small")
@DisplayName("OrgUnitエンティティの単体テスト")
class OrgUnitTest {

  @Nested
  @DisplayName("createメソッドのテスト")
  class CreateMethodTest {

    private Org createMockOrg() {
      return Org.create(
          SnowflakeId.of(1000000001L),
          "ORG001",
          LocalDateTime.of(2024, 1, 1, 0, 0),
          OrgName.of("テスト組織"),
          OrgCode.of("ORG001"),
          OrgTypeCode.of("COMPANY"),
          null,
          OrgStatus.ACTIVE);
    }

    @Test
    @DisplayName("有効な引数でOrgUnitインスタンスが作成されること")
    void shouldCreateOrgUnitWithValidArguments() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(2000000001L);
      String code = "UNIT001";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      OrgUnitName orgUnitName = OrgUnitName.of("開発部");
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEV001");
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of("DEPARTMENT");
      OrgUnitStatus status = OrgUnitStatus.ACTIVE;
      Org org = createMockOrg();

      // Act
      OrgUnit orgUnit = OrgUnit.create(
          partyId, code, validFrom, orgUnitName, orgUnitCode, orgUnitTypeCode, null, status, org);

      // Assert
      assertThat(orgUnit).as("OrgUnitインスタンスが作成されること").isNotNull();
      assertThat(orgUnit.getPartyId()).isEqualTo(partyId);
      assertThat(orgUnit.getPartyTypeCode()).isEqualTo(PartyTypeCode.ORG_UNIT);
      assertThat(orgUnit.getCode()).isEqualTo(code);
      assertThat(orgUnit.getValidFrom()).isEqualTo(validFrom);
      assertThat(orgUnit.getValidTo()).as("validToはnullであること").isNull();
      assertThat(orgUnit.getOrgUnitName()).isEqualTo(orgUnitName);
      assertThat(orgUnit.getOrgUnitCode()).isEqualTo(orgUnitCode);
      assertThat(orgUnit.getOrgUnitTypeCode()).isEqualTo(orgUnitTypeCode);
      assertThat(orgUnit.getStatus()).isEqualTo(status);
      assertThat(orgUnit.getOrg()).isEqualTo(org);
      assertThat(orgUnit.getParentOrgUnitId()).as("親組織単位IDはnullであること").isNull();
    }

    @Test
    @DisplayName("親組織単位IDを設定して作成できること")
    void shouldCreateOrgUnitWithParentOrgUnitId() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(2000000001L);
      SnowflakeId parentOrgUnitId = SnowflakeId.of(2000000002L);
      String code = "UNIT002";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      OrgUnitName orgUnitName = OrgUnitName.of("開発チームA");
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEV002");
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of("TEAM");
      OrgUnitStatus status = OrgUnitStatus.ACTIVE;
      Org org = createMockOrg();

      // Act
      OrgUnit orgUnit = OrgUnit.create(
          partyId, code, validFrom, orgUnitName, orgUnitCode, orgUnitTypeCode, parentOrgUnitId, status, org);

      // Assert
      assertThat(orgUnit.getParentOrgUnitId()).isEqualTo(parentOrgUnitId);
    }

    @Test
    @DisplayName("日本語の組織単位名で正常に作成されること")
    void shouldCreateOrgUnitWithJapaneseName() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(2000000001L);
      String code = "UNIT001";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      OrgUnitName orgUnitName = OrgUnitName.of("研究開発部");
      OrgUnitCode orgUnitCode = OrgUnitCode.of("RND001");
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of("DEPARTMENT");
      OrgUnitStatus status = OrgUnitStatus.ACTIVE;
      Org org = createMockOrg();

      // Act
      OrgUnit orgUnit = OrgUnit.create(
          partyId, code, validFrom, orgUnitName, orgUnitCode, orgUnitTypeCode, null, status, org);

      // Assert
      assertThat(orgUnit.getOrgUnitName().value()).isEqualTo("研究開発部");
    }

    @Test
    @DisplayName("smallersリストは空のArrayListで初期化されること")
    void shouldHaveEmptySmallersList() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(2000000001L);
      String code = "UNIT001";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      OrgUnitName orgUnitName = OrgUnitName.of("開発部");
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEV001");
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.of("DEPARTMENT");
      OrgUnitStatus status = OrgUnitStatus.ACTIVE;
      Org org = createMockOrg();

      // Act
      OrgUnit orgUnit = OrgUnit.create(
          partyId, code, validFrom, orgUnitName, orgUnitCode, orgUnitTypeCode, null, status, org);

      // Assert
      assertThat(orgUnit.getSmallers()).isNotNull();
      assertThat(orgUnit.getSmallers()).isEmpty();
    }
  }

  @Nested
  @DisplayName("エンティティアノテーションのテスト")
  class EntityAnnotationTest {

    @Test
    @DisplayName("OrgUnitクラスに@Entityアノテーションが付与されていること")
    void shouldHaveEntityAnnotation() {
      // Assert
      assertThat(OrgUnit.class.isAnnotationPresent(jakarta.persistence.Entity.class)).isTrue();
    }

    @Test
    @DisplayName("OrgUnitクラスはPartyを継承していること")
    void shouldExtendParty() {
      // Assert
      assertThat(OrgUnit.class.getSuperclass()).isEqualTo(Party.class);
    }
  }
}
