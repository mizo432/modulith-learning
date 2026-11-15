package undecided.agiloom.scrumTeamManagement.spi;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

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
 * スクラムチームエンティティ。
 *
 * <p>Productに属するスクラムチームの基本情報を管理します。
 * チーム名、事業領域、PO（Product Owner）等信息を持ち、状態遷移（ACTIVE → ARCHIVED）で管理されます。
 * 1つのスクラムチームには1人のPOのみ割り当てられる制約があります。
 *
 * <p>C7修正により{@code @Data}は{@code @Getter}に置き換えられました。
 * インスタンスは{@code @Builder}を使用して作成します。
 * {@code @AllArgsConstructor}は{@code @Builder}の動作に必要な内部用コンストラクタです。
 */
@Entity
@Table(schema = "undecided_ag", name = "scrum_teams")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScrumTeam {

  /** スクラムチームの一意な識別子。 */
  @Id protected SnowflakeId id;

  /** 関連するProductのID。 */
  @Column(name = "product_id", nullable = false)
  protected UUID productId;

  /** チーム名。 */
  @Column(nullable = false, length = 100)
  protected String teamName;

  /** 事業領域。 */
  @Column(name = "business_domain", nullable = false, length = 100)
  protected String businessDomain;

  /** チームの説明。 */
  @Column(columnDefinition = "TEXT")
  protected String description;

  /** PO（Product Owner）の従業員ID。1人のPOのみ割り当て可能。 */
  @Column(name = "po_employee_id")
  protected UUID poEmployeeId;

  /** チームのステータス（ACTIVE, ARCHIVED）。 */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  protected ScrumTeamStatus status = ScrumTeamStatus.ACTIVE;

  /** チームが作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** チームが最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** チームが論理削除された日時。nullの場合は未削除です。 */
  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

  /**
   * スクラムチーム情報を更新します。
   *
   * @param teamName 更新後のチーム名（nullの場合は変更なし）
   * @param businessDomain 更新後の事業領域（nullの場合は変更なし）
   * @param description 更新後の説明（nullの場合は変更なし）
   */
  public void update(
      @Nullable String teamName, @Nullable String businessDomain, @Nullable String description) {
    if (teamName != null) {
      checkNotNull(teamName, () -> new IllegalArgumentException("teamName must not be null"));
      this.teamName = teamName;
    }
    if (businessDomain != null) {
      checkNotNull(businessDomain, () -> new IllegalArgumentException("businessDomain must not be null"));
      this.businessDomain = businessDomain;
    }
    if (description != null) {
      this.description = description;
    }
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * PO（Product Owner）を割り当てます。
   *
   * @param poEmployeeId 割り当てる従業員のID
   */
  public void assignPo(@NonNull UUID poEmployeeId) {
    checkNotNull(poEmployeeId, () -> new IllegalArgumentException("poEmployeeId must not be null"));
    this.poEmployeeId = poEmployeeId;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * PO（Product Owner）を解除します。
   */
  public void unassignPo() {
    this.poEmployeeId = null;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * チームをアーカイブします。ACTIVE状態からARCHIVED状態へ遷移します。
   */
  public void archive() {
    if (this.status != ScrumTeamStatus.ACTIVE) {
      throw new IllegalStateException(
          "ScrumTeam can only be archived from ACTIVE status, current status: " + this.status);
    }
    this.status = ScrumTeamStatus.ARCHIVED;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * チームを論理削除します。deleted_atに現在時刻を設定します。
   */
  public void delete() {
    this.deletedAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }
}
