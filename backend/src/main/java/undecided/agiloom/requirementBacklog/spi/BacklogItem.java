package undecided.agiloom.requirementBacklog.spi;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;
import static com.github.mizo432.shared.common.precondition.StringPrecondition.checkNonEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
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
 * 状態を {@link BacklogItemStatus#ACCEPTED} や {@link BacklogItemStatus#FROZEN} へ変更できるのは、
 * そのチームのSM（Scrum Master）とPO（Product Owner）のみです。
 * {@link BacklogItemStatus#FROZEN} から {@link BacklogItemStatus#SUGGESTED} への復帰は PO のみです。
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
  protected BacklogItemStatus status;

  /** 優先度（並び順の確定は未決定事項 Issue #571）。 */
  @Column protected Integer priority;

  /** 作成者の従業員ID（チームメンバー、またはステークホルダー＝チーム外の従業員）。 */
  @Column(name = "creator_employee_id")
  protected SnowflakeId creatorEmployeeId;

  /** 作成者名。 */
  @Column(name = "creator_name", nullable = false, length = CREATOR_NAME_MAX_LENGTH)
  protected String creatorName;

  /** 関連スプリントのID（未割り当ての場合はnull）。 */
  @Column(name = "sprint_id")
  protected SnowflakeId sprintId;

  /** 所属するスクラムチームのID。他領域のエンティティは参照せず、IDのみを保持する。 */
  @Column(name = "scrum_team_id", nullable = false)
  protected SnowflakeId scrumTeamId;

  /** 分割元のバックログアイテムID。 */
  @Column(name = "parent_item_id")
  protected SnowflakeId parentItemId;

  /** Value（ビジネス価値の定量的評価、0〜100）。null可。 */
  @Column(name = "value")
  protected Integer value;

  /** 品質カテゴリ（狩野式品質モデル）。Valueから自動計算される（ADR-003）。 */
  @Enumerated(EnumType.STRING)
  @Column(name = "quality_category", length = 20)
  protected QualityCategory qualityCategory;

  /** ストーリーポイント（見積もり値）。フィボナッチ数列の値のみ許容。null可。 */
  @Column(name = "story_point")
  protected Integer storyPoint;

  /** 作成日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  protected LocalDateTime createdAt;

  /** 最終更新日時。 */
  @Column(name = "updated_at", nullable = false)
  protected LocalDateTime updatedAt;

  /** 楽観ロック用のバージョン番号。JPAが管理するフィールドであり、ビルダーでは設定しない。 */
  @Version
  @Column(name = "version")
  protected Integer version;

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
      @Nullable Integer value, @NonNull LocalDateTime now) {
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
    this.qualityCategory = calculateQualityCategory(this.value);
    this.updatedAt = now;
  }

  /**
   * スプリントに割り当てます（Issue #525）。割り当ては状態を変えません。
   *
   * @param sprintId 割り当て先スプリントID
   */
  public void assignToSprint(@NonNull SnowflakeId sprintId, @NonNull LocalDateTime now) {
    checkNotNull(sprintId, () -> new IllegalArgumentException("sprintId must not be null"));
    this.sprintId = sprintId;
    this.updatedAt = now;
  }

  /** スプリント割り当てを解除します（Issue #525）。 */
  public void unassignFromSprint(@NonNull LocalDateTime now) {
    this.sprintId = null;
    this.updatedAt = now;
  }

  /**
   * 状態を変更します。
   *
   * <p>許可される遷移は以下の通りです：
   * <ul>
   *   <li>SUGGESTED → ACCEPTED（Value が設定されていること）
   *   <li>SUGGESTED → FROZEN
   *   <li>ACCEPTED → READY（スクラムチームメンバー全員が実行可能）
   *   <li>READY → SUGGESTED（スクラムチームメンバー全員が実行可能）
   *   <li>READY → ACCEPTED（StoryPoint はクリアされる。スクラムチームメンバー全員が実行可能）
   *   <li>READY → ESTIMATED（StoryPoint が設定されていること。スクラムチームメンバー全員が実行可能）
   *   <li>ESTIMATED → READY（StoryPoint は保持。スクラムチームメンバー全員が実行可能）
   *   <li>FROZEN → SUGGESTED（PO のみが実行可能。権限判定はコマンドサービス側で行う）
   * </ul>
   * 変更できる主体の判定は呼び出し側（コマンドサービス）がサブセットの保持する
   * PO／SM／メンバー情報と照合して事前に行います。
   * 状態遷移の履歴はロングタームイベントパターン（{@link BacklogItemStatusChanged} イベント）で
   * 担保するため、専用の監査ログは持ちません（Issue #572 の決定）。
   *
   * @param target 遷移先の状態
   * @throws IllegalStateException 許可されていない遷移の場合
   * @throws IllegalArgumentException ACCEPTED 遷移時に Value が未設定の場合、
   *                                  ESTIMATED 遷移時に StoryPoint が未設定の場合
   */
  public void changeStatus(@NonNull BacklogItemStatus target, @NonNull LocalDateTime now) {
    checkNotNull(target, () -> new IllegalArgumentException("target status must not be null"));
    switch (this.status) {
      case SUGGESTED -> {
        switch (target) {
          case ACCEPTED -> {
            if (this.value == null) {
              throw new IllegalArgumentException("BacklogItem value must be set before accepting");
            }
            validateValue(this.value);
            this.status = target;
            this.updatedAt = now;
          }
          case FROZEN -> {
            this.status = target;
            this.updatedAt = now;
          }
          default -> throw new IllegalStateException(
              "Invalid status transition: " + this.status + " -> " + target);
        }
      }
      case ACCEPTED -> {
        if (target == BacklogItemStatus.READY) {
          this.status = target;
          this.updatedAt = now;
        } else {
          throw new IllegalStateException(
              "Invalid status transition: " + this.status + " -> " + target);
        }
      }
      case READY -> {
        if (target == BacklogItemStatus.SUGGESTED) {
          this.status = target;
          this.updatedAt = now;
        } else if (target == BacklogItemStatus.ACCEPTED) {
          this.status = target;
          this.storyPoint = null;
          this.updatedAt = now;
        } else if (target == BacklogItemStatus.ESTIMATED) {
          if (this.storyPoint == null) {
            throw new IllegalArgumentException("StoryPoint must be set before estimating");
          }
          StoryPoint.of(this.storyPoint);
          this.status = target;
          this.updatedAt = now;
        } else {
          throw new IllegalStateException(
              "Invalid status transition: " + this.status + " -> " + target);
        }
      }
      case ESTIMATED -> {
        if (target == BacklogItemStatus.READY) {
          this.status = target;
          this.updatedAt = now;
        } else {
          throw new IllegalStateException(
              "Invalid status transition: " + this.status + " -> " + target);
        }
      }
      case FROZEN -> {
        if (target == BacklogItemStatus.SUGGESTED) {
          this.status = target;
          this.updatedAt = now;
        } else {
          throw new IllegalStateException(
              "Invalid status transition: " + this.status + " -> " + target);
        }
      }
      default -> {
        throw new IllegalStateException(
            "Invalid status transition: " + this.status + " -> " + target);
      }
    }
  }

  public void completeForSplit(@NonNull LocalDateTime now) {
    if (this.status == BacklogItemStatus.DONE) {
      throw new IllegalStateException("BacklogItem is already done");
    }
    this.status = BacklogItemStatus.DONE;
    this.updatedAt = now;
  }

  public @Nullable SnowflakeId getParentItemId() {
    return parentItemId;
  }

  /**
   * ストーリーポイント（見積もり値）を設定します。
   *
   * <p>ESTIMATED 状態での再評価（値の変更）も許可します（#642で決定）。
   * 設定された値はフィボナッチ数列の値である必要があります。
   *
   * @param storyPoint 設定するストーリーポイント値（null可。nullの場合は変更なし）
   * @throws IllegalArgumentException 値がフィボナッチ数列にない場合
   */
  public void setStoryPoint(@Nullable Integer storyPoint, @NonNull LocalDateTime now) {
    if (storyPoint != null) {
      StoryPoint.of(storyPoint);
    }
    this.storyPoint = storyPoint;
    this.updatedAt = now;
  }

  /**
   * 現在のストーリーポイントを返します。
   *
   * @return ストーリーポイント値。未設定の場合は null
   */
  public @Nullable Integer getStoryPoint() {
    return storyPoint;
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

  /**
   * Value から品質カテゴリを計算します。
   *
   * @param value バックログアイテムの Value
   * @return 対応する品質カテゴリ。Value が null の場合は null
   */
  private static QualityCategory calculateQualityCategory(Integer value) {
    return QualityCategory.fromValue(value);
  }

  /**
   * バックログアイテムをビルダーパターンで構築します。
   *
   * <p>ビルダーの {@code build()} 時に {@code value} から {@code qualityCategory} を自動計算し、
   * {@code status} が未設定の場合は {@link BacklogItemStatus#SUGGESTED} を、
   * {@code createdAt}・{@code updatedAt} が未設定の場合は例外をスローします。
   */
  @Builder(builderMethodName = "builder")
  private static BacklogItem build(
      SnowflakeId id,
      String title,
      String description,
      BacklogItemStatus status,
      Integer priority,
      SnowflakeId creatorEmployeeId,
      String creatorName,
      SnowflakeId sprintId,
      SnowflakeId scrumTeamId,
      SnowflakeId parentItemId,
      Integer value,
      Integer storyPoint,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    BacklogItem item = new BacklogItem();
    item.id = id;
    item.title = title;
    item.description = description;
    item.status = status != null ? status : BacklogItemStatus.SUGGESTED;
    item.priority = priority;
    item.creatorEmployeeId = creatorEmployeeId;
    item.creatorName = creatorName;
    item.sprintId = sprintId;
    item.scrumTeamId = scrumTeamId;
    item.parentItemId = parentItemId;
    item.value = value;
    item.storyPoint = storyPoint;
    if (createdAt == null) {
      throw new IllegalArgumentException("createdAt must not be null");
    }
    if (updatedAt == null) {
      throw new IllegalArgumentException("updatedAt must not be null");
    }
    item.createdAt = createdAt;
    item.updatedAt = updatedAt;
    item.qualityCategory = calculateQualityCategory(value);
    return item;
  }
}
