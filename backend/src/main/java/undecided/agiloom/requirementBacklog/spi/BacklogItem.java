package undecided.agiloom.requirementBacklog.spi;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;
import static com.github.mizo432.shared.common.precondition.StringPrecondition.checkNonEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * バックログアイテムエンティティ。
 *
 * <p>スクラムチームのバックログに登録されるアイテムを管理します。
 * チームメンバーとステークホルダー（チーム外の従業員。Issue #570 の決定）が提案でき、
 * 初期状態は {@link BacklogItemStatus#SUGGESTED} です。
 * 状態を {@link BacklogItemStatus#ACCEPTED} や {@link BacklogItemStatus#REJECTED} へ変更できるのは、
 * そのチームのSM（Scrum Master）とPO（Product Owner）のみです。
 *
 * <p>所属チームについては、productDiscovery のエンティティを直接参照せず、チームIDのみを保持します。
 * 状態変更の可否は、イベントで連携された自領域のサブセット（internal の ScrumTeamSubset）が
 * 保持する PO／SM と操作者を照合して、コマンドサービス側で判定します。
 * スクラムチームは組織ユニットとして orgRole で管理する方針であり（Issue #575）、
 * 役割の正本は将来的に orgRole へ寄せます。現行実装ではサブセットの PO／SM で判定します。
 *
 * <p>インスタンスは {@code @Builder} を使用して作成します。
 */
@Entity
@Table(schema = "undecided_ag", name = "backlog_items")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BacklogItem {

  /** タイトルの最大長。項目制約の単一の情報源（ADR-002）。 */
  public static final int TITLE_MAX_LENGTH = 200;

  /** 作成者名の最大長。項目制約の単一の情報源（ADR-002）。 */
  public static final int CREATOR_NAME_MAX_LENGTH = 100;

  /** Valueの最小値。項目制約の単一の情報源（ADR-002）。 */
  public static final int VALUE_MIN_VALUE = 0;

  /** Valueの最大値。項目制約の単一の情報源（ADR-002）。 */
  public static final int VALUE_MAX_VALUE = 100;

  /** バックログアイテムの一意な識別子。 */
  @Id protected SnowflakeId id;

  /** タイトル。 */
  @Column(nullable = false, length = TITLE_MAX_LENGTH)
  protected String title;

  /** 説明。 */
  @Column(columnDefinition = "TEXT")
  protected String description;

  /** 状態（区分値）。DBにはコード値のみを保持する（ADR-003）。 */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  protected BacklogItemStatus status = BacklogItemStatus.SUGGESTED;

  /** 優先度（並び順の確定は未決定事項 Issue #571）。 */
  @Column protected Integer priority;

  /** 作成者の従業員ID（チームメンバー、またはステークホルダー＝チーム外の従業員）。 */
  @Column(name = "creator_employee_id")
  protected UUID creatorEmployeeId;

  /** 作成者名。 */
  @Column(name = "creator_name", nullable = false, length = CREATOR_NAME_MAX_LENGTH)
  protected String creatorName;

  /** 関連スプリントのID（未割り当ての場合はnull）。 */
  @Column(name = "sprint_id")
  protected SnowflakeId sprintId;

  /** 所属するスクラムチームのID。他領域のエンティティは参照せず、IDのみを保持する。 */
  @Column(name = "scrum_team_id", nullable = false)
  protected SnowflakeId scrumTeamId;

  /** Value（ビジネス価値の定量的評価、0〜100）。null可。 */
  @Column(name = "value")
  protected Integer value;

  /** 作成日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** 最終更新日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /**
   * バックログアイテムの内容を更新します。
   *
   * @param title 更新後のタイトル（nullの場合は変更なし）
   * @param description 更新後の説明（nullの場合は変更なし）
   * @param priority 更新後の優先度（nullの場合は変更なし）
   * @param sprintId 更新後の関連スプリントID（nullの場合は変更なし）
   * @param value 更新後のValue（nullの場合は変更なし）
   */
  public void update(
      @Nullable String title,
      @Nullable String description,
      @Nullable Integer priority,
      @Nullable SnowflakeId sprintId,
      @Nullable Integer value) {
    if (title != null) {
      checkNonEmpty(title, () -> new IllegalArgumentException("BacklogItem title must not be empty"));
      this.title = title;
    }
    if (description != null) {
      this.description = description;
    }
    if (priority != null) {
      this.priority = priority;
    }
    if (sprintId != null) {
      this.sprintId = sprintId;
    }
    if (value != null) {
      validateValue(value);
      this.value = value;
    }
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * 状態を変更します。
   *
   * <p>許可される遷移は SUGGESTED → ACCEPTED と SUGGESTED → REJECTED のみです。
   * 変更できる主体（SM／PO）の判定は呼び出し側（コマンドサービス）がサブセットの保持する
   * PO／SM と照合して事前に行います。
   * 状態遷移の履歴はロングタームイベントパターン（{@link BacklogItemStatusChanged} イベント）で
   * 担保するため、専用の監査ログは持ちません（Issue #572 の決定）。
   *
   * @param target 遷移先の状態
   * @throws IllegalStateException 許可されていない遷移の場合
   */
  public void changeStatus(@NonNull BacklogItemStatus target) {
    checkNotNull(target, () -> new IllegalArgumentException("target status must not be null"));
    if (this.status != BacklogItemStatus.SUGGESTED || target == BacklogItemStatus.SUGGESTED) {
      throw new IllegalStateException(
          "Invalid status transition: " + this.status + " -> " + target);
    }
    if (target == BacklogItemStatus.ACCEPTED) {
      validateValue(this.value);
    }
    this.status = target;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Valueの範囲を検証します。
   *
   * <p>nullの場合は通過します（必須チェックは対象外）。
   * 範囲外（0未満または100超過）の場合は {@link IllegalArgumentException} をスローします。
   *
   * @param value 検証対象のValue
   * @throws IllegalArgumentException 範囲外の場合
   */
  private void validateValue(Integer value) {
    if (value == null) {
      return;
    }
    if (value < VALUE_MIN_VALUE || value > VALUE_MAX_VALUE) {
      throw new IllegalArgumentException(
          "BacklogItem value must be between " + VALUE_MIN_VALUE + " and " + VALUE_MAX_VALUE);
    }
  }
}
