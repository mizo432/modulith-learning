package undecided.erp.role;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import undecided.erp.common.primitive.Strings2;

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
   * RoleChangeRequestTypeコンストラクタは、指定されたコードおよびソート順を使用して ロール変更要求の種類を初期化します。
   *
   * @param code ロール変更要求の種類を識別する文字列コード
   * @param sortOrder ロール変更要求の種類に対応するソート順を表す数値
   */
  RoleChangeRequestType(String code, int sortOrder) {
    this.code = code;
    this.sortOrder = sortOrder;
  }

  /**
   * 指定されたコードに対応するRoleChangeRequestType列挙型を取得します。
   *
   * @param code 対応するRoleChangeRequestTypeを決定する文字列コード この値がnullの場合、IllegalArgumentExceptionがスローされます。
   * @return 指定されたコードに対応するRoleChangeRequestTypeの列挙値 不正または未知のコードが渡された場合はUNKNOWNを返します。
   * @throws IllegalArgumentException 引数のコードがnullの場合にスローされます。
   */
  @JsonCreator
  public static RoleChangeRequestType valueOfCode(@NonNull String code) {
    checkNotNull(code, () -> new IllegalArgumentException("code must not be null."));
    if (Strings2.equal(code, "null")) return UNKNOWN;
    return switch (code) {
      case "00" -> CREATE;
      case "10" -> UPDATE;
      case "20" -> DELETE;
      default -> UNKNOWN;
    };
  }

  /**
   * このメソッドは、現在の列挙型インスタンスに関連付けられているコードを取得します。
   *
   * @return ガイドラインや特定のタイプを識別するための文字列コード
   */
  @JsonValue
  public String getCode() {
    return code;
  }
}
