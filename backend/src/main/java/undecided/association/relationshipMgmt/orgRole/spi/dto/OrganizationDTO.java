package undecided.association.relationshipMgmt.orgRole.spi.dto;

import java.time.LocalDateTime;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 組織情報のDTOです。
 * REST APIのパラメータとして使用され、ドメインエンティティの代わりに利用されます。
 */
public record OrganizationDTO(
    SnowflakeId organizationId,
    SnowflakeId partyId,
    @NonNull String organizationCode,
    @NonNull String name,
    @NonNull String organizationTypeCode,
    @Nullable SnowflakeId parentOrganizationId,
    @NonNull String status,
    @NonNull LocalDateTime validFrom,
    @Nullable LocalDateTime validTo) {

  /**
   * 組織DTOを作成します。
   *
   * @param organizationId 組織ID
   * @param partyId パーティーID
   * @param organizationCode 組織コード
   * @param name 組織名
   * @param organizationTypeCode 組織種類コード
   * @param parentOrganizationId 親組織ID
   * @param status 状態
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   * @return 組織DTO
   */
  public static OrganizationDTO create(
      SnowflakeId organizationId,
      SnowflakeId partyId,
      @NonNull String organizationCode,
      @NonNull String name,
      @NonNull String organizationTypeCode,
      @Nullable SnowflakeId parentOrganizationId,
      @NonNull String status,
      @NonNull LocalDateTime validFrom,
      @Nullable LocalDateTime validTo) {
    if (organizationCode == null || organizationCode.trim().isEmpty()) {
      throw new IllegalArgumentException("organizationCode must not be null or empty");
    }
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("name must not be null or empty");
    }
    if (organizationTypeCode == null || organizationTypeCode.trim().isEmpty()) {
      throw new IllegalArgumentException("organizationTypeCode must not be null or empty");
    }
    if (status == null || status.trim().isEmpty()) {
      throw new IllegalArgumentException("status must not be null or empty");
    }
    if (validFrom == null) {
      throw new IllegalArgumentException("validFrom must not be null");
    }
    return new OrganizationDTO(
        organizationId,
        partyId,
        organizationCode,
        name,
        organizationTypeCode,
        parentOrganizationId,
        status,
        validFrom,
        validTo);
  }
}
