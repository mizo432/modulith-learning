package undecided.association.relationshipMgmt.orgRole.spi.dto;

import org.jspecify.annotations.NonNull;
import undecided.association.relationshipMgmt.orgRole.spi.DepartmentCode;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 部署情報のDTOです。
 * REST APIのパラメータとして使用され、ドメインエンティティの代わりに利用されます。
 */
public record DepartmentDTO(
    SnowflakeId id,
    @NonNull DepartmentCode code) {

  /**
   * 部署DTOを作成します。
   *
   * @param id 部署ID
   * @param code 部署コード
   * @return 部署DTO
   */
  public static DepartmentDTO create(SnowflakeId id, @NonNull DepartmentCode code) {
    if (code == null) {
      throw new IllegalArgumentException("code must not be null");
    }
    return new DepartmentDTO(id, code);
  }
}
