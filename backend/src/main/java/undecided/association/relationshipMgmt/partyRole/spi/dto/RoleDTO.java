package undecided.association.relationshipMgmt.partyRole.spi.dto;

import java.time.LocalDateTime;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * ロール情報のDTOです。
 * REST APIのパラメータとして使用され、ドメインエンティティの代わりに利用されます。
 */
public record RoleDTO(
    SnowflakeId roleId,
    @NonNull String roleCode,
    @NonNull String roleName,
    @NonNull String roleTypeCode,
    @Nullable String description,
    @NonNull LocalDateTime validFrom,
    @Nullable LocalDateTime validTo) {

  /**
   * ロールDTOを作成します。
   *
   * @param roleId ロールID
   * @param roleCode ロールコード
   * @param roleName ロール名称
   * @param roleTypeCode ロール種類コード
   * @param description 説明
   * @param validFrom 有効開始日
   * @param validTo 有効終了日
   * @return ロールDTO
   */
  public static RoleDTO create(
      SnowflakeId roleId,
      @NonNull String roleCode,
      @NonNull String roleName,
      @NonNull String roleTypeCode,
      @Nullable String description,
      @NonNull LocalDateTime validFrom,
      @Nullable LocalDateTime validTo) {
    if (roleCode == null || roleCode.trim().isEmpty()) {
      throw new IllegalArgumentException("roleCode must not be null or empty");
    }
    if (roleName == null || roleName.trim().isEmpty()) {
      throw new IllegalArgumentException("roleName must not be null or empty");
    }
    if (roleTypeCode == null || roleTypeCode.trim().isEmpty()) {
      throw new IllegalArgumentException("roleTypeCode must not be null or empty");
    }
    if (validFrom == null) {
      throw new IllegalArgumentException("validFrom must not be null");
    }
    return new RoleDTO(
        roleId,
        roleCode,
        roleName,
        roleTypeCode,
        description,
        validFrom,
        validTo);
  }
}
