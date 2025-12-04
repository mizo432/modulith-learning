package undecided.erp.role;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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

  private final int sortOrder;
  private final String code;

  /**
   * 指定されたコードおよびソート順を使用してRoleChangeRequestType列挙型のインスタンスを初期化します。
   *
   * @param code 種類を識別するためのコード
   * @param sortOrder 種類のソート順を示す数値
   */
  RoleChangeRequestType(String code, int sortOrder) {
    this.code = code;
    this.sortOrder = sortOrder;
  }

  /**
   * 指定されたコードに基づいてRoleChangeRequestTypeを評価し、対応する列挙型を返します。
   *
   * <p>コードが無効な場合はUNKNOWNを返します。
   *
   * @param code ロール変更要求の種類を識別するためのコード（必須）
   * @return 指定されたコードに対応するRoleChangeRequestType列挙型。 コードに対応する値が存在しない場合はUNKNOWNを返します。
   * @throws IllegalArgumentException 引数codeがnullの場合
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
   * このメソッドは、RoleChangeRequestTypeを識別するコードを取得します。
   *
   * @return このRoleChangeRequestTypeに関連付けられた識別コード
   */
  @JsonValue
  public String getCode() {
    return code;
  }

  /**
   * このメソッドは、RoleChangeRequestTypeのソート順を取得します。
   *
   * @return このRoleChangeRequestTypeに関連付けられたソート順の数値
   */
  public int getSortOrder() {
    return sortOrder;
  }
}
