package undecided.association.relationshipMgmt.spi.party.org;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * {@code OrgTest} は、Orgエンティティの単体テストクラスです。
 * <p>
 * C3修正で追加された{@code create()}ファクトリメソッドの動作を検証します。
 */
@Tag("small")
@DisplayName("Orgエンティティの単体テスト")
class OrgTest {

  @Nested
  @DisplayName("createメソッドのテスト")
  class CreateMethodTest {

    @Test
    @DisplayName("有効な引数でOrgインスタンスが作成されること")
    void shouldCreateOrgWithValidArguments() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(1000000001L);
      String code = "ORG001";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      OrgName orgName = OrgName.of("テスト組織");
      OrgCode orgCode = OrgCode.of("ORG001");
      OrgTypeCode orgTypeCode = OrgTypeCode.of("COMPANY");
      OrgStatus status = OrgStatus.ACTIVE;

      // Act
      Org org = Org.create(partyId, code, validFrom, orgName, orgCode, orgTypeCode, null, status);

      // Assert
      assertThat(org).as("Orgインスタンスが作成されること").isNotNull();
      assertThat(org.getPartyId()).isEqualTo(partyId);
      assertThat(org.getCode()).isEqualTo(code);
      assertThat(org.getOrgName()).isEqualTo(orgName);
      assertThat(org.getOrgCode()).isEqualTo(orgCode);
      assertThat(org.getOrgTypeCode()).isEqualTo(orgTypeCode);
      assertThat(org.getValidFrom()).isEqualTo(validFrom);
      assertThat(org.getValidTo()).as("validToはnullであること").isNull();
      assertThat(org.getGovtAssignedId()).as("govtAssignedIdはnullであること").isNull();
      assertThat(org.getSearchName()).as("searchNameはnullであること").isNull();
      assertThat(org.getStatus()).isEqualTo(status);
      assertThat(org.getParentOrgId()).as("親組織IDはnullであること").isNull();
    }

    @Test
    @DisplayName("親組織IDを設定して作成できること")
    void shouldCreateOrgWithParentOrgId() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(1000000001L);
      SnowflakeId parentOrgId = SnowflakeId.of(1000000002L);
      String code = "ORG001";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      OrgName orgName = OrgName.of("テスト組織");
      OrgCode orgCode = OrgCode.of("ORG001");
      OrgTypeCode orgTypeCode = OrgTypeCode.of("COMPANY");
      OrgStatus status = OrgStatus.ACTIVE;

      // Act
      Org org = Org.create(partyId, code, validFrom, orgName, orgCode, orgTypeCode, parentOrgId, status);

      // Assert
      assertThat(org.getParentOrgId()).isEqualTo(parentOrgId);
    }

    @Test
    @DisplayName("日本語の組織名で正常に作成されること")
    void shouldCreateOrgWithJapaneseName() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(1000000001L);
      String code = "ORG001";
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      OrgName orgName = OrgName.of("株式会社テスト");
      OrgCode orgCode = OrgCode.of("ORG001");
      OrgTypeCode orgTypeCode = OrgTypeCode.of("COMPANY");
      OrgStatus status = OrgStatus.ACTIVE;

      // Act
      Org org = Org.create(partyId, code, validFrom, orgName, orgCode, orgTypeCode, null, status);

      // Assert
      assertThat(org.getOrgName().value()).isEqualTo("株式会社テスト");
    }

    @Test
    @DisplayName("最大文字数のコードで正常に作成されること")
    void shouldCreateOrgWithMaxCodeLength() {
      // Arrange
      SnowflakeId partyId = SnowflakeId.of(1000000001L);
      String code = "123456789012345678901234567890"; // 30文字
      LocalDateTime validFrom = LocalDateTime.of(2024, 1, 1, 0, 0);
      OrgName orgName = OrgName.of("テスト組織");
      OrgCode orgCode = OrgCode.of("ORG001");
      OrgTypeCode orgTypeCode = OrgTypeCode.of("COMPANY");
      OrgStatus status = OrgStatus.ACTIVE;

      // Act
      Org org = Org.create(partyId, code, validFrom, orgName, orgCode, orgTypeCode, null, status);

      // Assert
      assertThat(org.getCode()).isEqualTo(code);
    }
  }

  @Nested
  @DisplayName("エンティティアノテーションのテスト")
  class EntityAnnotationTest {

    @Test
    @DisplayName("Orgクラスに@Entityアノテーションが付与されていること")
    void shouldHaveEntityAnnotation() {
      // Assert
      assertThat(Org.class.isAnnotationPresent(jakarta.persistence.Entity.class)).isTrue();
    }

    @Test
    @DisplayName("OrgクラスはPartyを継承していること")
    void shouldExtendParty() {
      // Assert
      assertThat(Org.class.getSuperclass()).isEqualTo(undecided.association.relationshipMgmt.spi.party.Party.class);
    }
  }
}
