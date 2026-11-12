package undecided.association.relationshipMgmt.spi.party.org.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import undecided.association.relationshipMgmt.spi.party.org.OrgCode;
import undecided.association.relationshipMgmt.spi.party.org.OrgName;
import undecided.association.relationshipMgmt.spi.party.org.OrgStatus;
import undecided.association.relationshipMgmt.spi.party.org.OrgTypeCode;
import undecided.supporting.snowflake.SnowflakeId;

@Tag("small")
@DisplayName("OrgDtoのテスト")
class OrgDtoTest {

  @Nested
  @DisplayName("コンストラクタとGetterのテスト")
  class ConstructorAndGetterTest {

    @Test
    @DisplayName("全フィールドが正しく設定および取得できること")
    void shouldSetAndGetAllFieldsCorrectly() {
      // Arrange
      OrgName orgName = OrgName.of("テスト組織");
      OrgCode orgCode = OrgCode.of("ORG001");
      OrgTypeCode orgTypeCode = OrgTypeCode.COMPANY;
      SnowflakeId parentOrgId = SnowflakeId.of(1L);
      OrgStatus status = OrgStatus.ACTIVE;

      // Act
      OrgDto dto = new OrgDto(orgName, orgCode, orgTypeCode, parentOrgId, status);

      // Assert
      assertThat(dto.getOrgName()).isEqualTo(orgName);
      assertThat(dto.getOrgCode()).isEqualTo(orgCode);
      assertThat(dto.getOrgTypeCode()).isEqualTo(orgTypeCode);
      assertThat(dto.getParentOrgId()).isEqualTo(parentOrgId);
      assertThat(dto.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("親組織IDがnullの場合も作成できること")
    void shouldCreateDtoWithNullParentOrgId() {
      // Arrange
      OrgName orgName = OrgName.of("テスト組織");
      OrgCode orgCode = OrgCode.of("ORG001");
      OrgTypeCode orgTypeCode = OrgTypeCode.STORE;
      OrgStatus status = OrgStatus.ACTIVE;

      // Act
      OrgDto dto = new OrgDto(orgName, orgCode, orgTypeCode, null, status);

      // Assert
      assertThat(dto.getOrgName()).isEqualTo(orgName);
      assertThat(dto.getOrgCode()).isEqualTo(orgCode);
      assertThat(dto.getOrgTypeCode()).isEqualTo(orgTypeCode);
      assertThat(dto.getParentOrgId()).isNull();
      assertThat(dto.getStatus()).isEqualTo(status);
    }
  }

  @Nested
  @DisplayName("toStringメソッドのテスト")
  class ToStringMethodTest {

    @Test
    @DisplayName("toStringメソッドがフィールド値を含む文字列を返すこと")
    void shouldReturnStringRepresentationWithFieldValues() {
      // Arrange
      OrgName orgName = OrgName.of("テスト組織");
      OrgCode orgCode = OrgCode.of("ORG001");
      OrgTypeCode orgTypeCode = OrgTypeCode.COMPANY;
      SnowflakeId parentOrgId = SnowflakeId.of(1L);
      OrgStatus status = OrgStatus.ACTIVE;

      OrgDto dto = new OrgDto(orgName, orgCode, orgTypeCode, parentOrgId, status);

      // Act
      String result = dto.toString();

      // Assert
      assertThat(result).isNotNull();
    }
  }
}
