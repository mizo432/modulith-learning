package undecided.erp.role;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

/**
 * RoleChangeRequestTypeは、ロール変更要求の種類を表す列挙型です。 この列挙型は、コードおよびソート順に基づいて異なるタイプのロール変更を識別します。
 *
 * <p>各種類には以下のフィールドが設定されています: - code: 種類を識別するコード - sortOrder: 種類のソート順を表す数値
 *
 * <p>使用される種類: - C: 作成（コード: "00"、ソート順: 20） - UPDATE: 更新（コード: "10"、ソート順: 10） - DELETE: 削除（コード:
 * "20"、ソート順: 30） - UNKNOWN: 未定義または不明（コード: "00"、ソート順: 999）
 */
public enum RoleChangeRequestType {
  /** CREATEは、新しいロール変更要求の作成を表します。 この種類は、コード"00"およびソート順20として識別されます。 */
  CREATE("00", 20),
  /** UPDATEは、既存のロール変更要求の更新を表します。 この種類は、コード"10"およびソート順10として識別されます。 */
  UPDATE("10", 10),
  /** DELETEは、既存のロール変更要求の削除を表します。 この種類は、コード"20"およびソート順30として識別されます。 */
  DELETE("20", 30),
  /** UNKNOWNは、未定義または不明なロール変更要求の種類を表します。 この種類は、コード"00"およびソート順999として識別されます。 */
  UNKNOWN("00", 999);

  /** -- GETTER -- The numeric sort order associated with this role change request type. */
  @Getter private final int sortOrder;

  private final String code;

  /**
   * Initialize this enum constant with its external code and display order.
   *
   * @param code the string code that identifies the request type
   * @param sortOrder the numeric sort order used when ordering request types
   */
  RoleChangeRequestType(String code, int sortOrder) {
    this.code = code;
    this.sortOrder = sortOrder;
  }

  /**
   * Map a code string to its corresponding RoleChangeRequestType.
   *
   * @param code the code identifying the role change request type
   * @return the matching RoleChangeRequestType; `UNKNOWN` if the code is not recognized
   * @throws IllegalArgumentException if `code` is null
   */
  @JsonCreator
  public static RoleChangeRequestType valueOfCode(@NonNull String code) {
    checkNotNull(code, () -> new IllegalArgumentException("code must not be null."));
    return switch (code) {
      case "10" -> CREATE;
      case "20" -> UPDATE;
      case "30" -> DELETE;
      default -> UNKNOWN;
    };
  }

  /**
   * Provides the identifier code for this role change request type.
   *
   * @return the identifier code associated with this enum constant
   */
  @JsonValue
  public String getCode() {
    return code;
  }
}
