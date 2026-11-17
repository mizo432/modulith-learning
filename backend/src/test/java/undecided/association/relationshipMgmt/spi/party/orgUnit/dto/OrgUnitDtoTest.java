package undecided.association.relationshipMgmt.spi.party.orgUnit.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnitCode;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnitName;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnitStatus;
import undecided.association.relationshipMgmt.spi.party.orgUnit.OrgUnitTypeCode;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("OrgUnitDtoのテスト")
class OrgUnitDtoTest {

  @Nested
  @DisplayName("コンストラクタとGetterのテスト")
  class ConstructorAndGetterTest {

    @Test
    @DisplayName("全フィールドが正しく設定および取得できること")
    void shouldSetAndGetAllFieldsCorrectly() {
      // Arrange
      OrgUnitName orgUnitName = OrgUnitName.of("テスト部署");
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEPT001");
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.DEPARTMENT;
      SnowflakeId parentOrgUnitId = SnowflakeId.of(1L);
      OrgUnitStatus status = OrgUnitStatus.ACTIVE;
      SnowflakeId orgId = SnowflakeId.of(2L);

      // Act
      OrgUnitDto dto =
          new OrgUnitDto(
              orgUnitName, orgUnitCode, orgUnitTypeCode, parentOrgUnitId, status, orgId);

      // Assert
      assertThat(dto.getOrgUnitName()).isEqualTo(orgUnitName);
      assertThat(dto.getOrgUnitCode()).isEqualTo(orgUnitCode);
      assertThat(dto.getOrgUnitTypeCode()).isEqualTo(orgUnitTypeCode);
      assertThat(dto.getParentOrgUnitId()).isEqualTo(parentOrgUnitId);
      assertThat(dto.getStatus()).isEqualTo(status);
      assertThat(dto.getOrgId()).isEqualTo(orgId);
    }

    @Test
    @DisplayName("親組織単位IDがnullの場合も作成できること")
    void shouldCreateDtoWithNullParentOrgUnitId() {
      // Arrange
      OrgUnitName orgUnitName = OrgUnitName.of("テスト部署");
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEPT001");
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.TEAM;
      OrgUnitStatus status = OrgUnitStatus.ACTIVE;
      SnowflakeId orgId = SnowflakeId.of(2L);

      // Act
      OrgUnitDto dto =
          new OrgUnitDto(orgUnitName, orgUnitCode, orgUnitTypeCode, null, status, orgId);

      // Assert
      assertThat(dto.getOrgUnitName()).isEqualTo(orgUnitName);
      assertThat(dto.getOrgUnitCode()).isEqualTo(orgUnitCode);
      assertThat(dto.getOrgUnitTypeCode()).isEqualTo(orgUnitTypeCode);
      assertThat(dto.getParentOrgUnitId()).isNull();
      assertThat(dto.getStatus()).isEqualTo(status);
      assertThat(dto.getOrgId()).isEqualTo(orgId);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("toStringメソッドがフィールド値を含む文字列を返すこと")
    void shouldReturnStringRepresentationWithFieldValues() {
      // Arrange
      OrgUnitName orgUnitName = OrgUnitName.of("テスト部署");
      OrgUnitCode orgUnitCode = OrgUnitCode.of("DEPT001");
      OrgUnitTypeCode orgUnitTypeCode = OrgUnitTypeCode.DEPARTMENT;
      SnowflakeId parentOrgUnitId = SnowflakeId.of(1L);
      OrgUnitStatus status = OrgUnitStatus.ACTIVE;
      SnowflakeId orgId = SnowflakeId.of(2L);

      OrgUnitDto dto =
          new OrgUnitDto(
              orgUnitName, orgUnitCode, orgUnitTypeCode, parentOrgUnitId, status, orgId);

      // Act
      String result = dto.toString();

      // Assert
      assertThat(result).isNotNull();
      assertThat(result).contains("テスト部署");
      assertThat(result).contains("DEPT001");
    }
  }
}
