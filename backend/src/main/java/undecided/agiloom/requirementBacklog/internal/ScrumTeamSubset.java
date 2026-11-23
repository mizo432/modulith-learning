package undecided.agiloom.requirementBacklog.internal;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.agiloom.productDiscovery.spi.ScrumTeamChanged;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * スクラムチーム情報のサブセット（射影）。
 *
 * <p>他事業領域（productDiscovery）のエンティティやテーブルを直接参照せず、
 * {@link ScrumTeamChanged} イベントで連携された情報だけを自領域内に保持し、参照します。
 * バックログアイテムの状態変更権限の判定（SM／PO）に必要な項目のみを持ちます。
 * モジュール内部でのみ使用するため、パッケージプライベートとします。
 */
@Entity
@Table(schema = "undecided_ag", name = "scrum_team_subsets")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class ScrumTeamSubset {

  /** スクラムチームID（productDiscovery 側の ScrumTeam のIDと同一）。 */
  @Id protected SnowflakeId id;

  /** チーム名。 */
  @Column(nullable = false, length = 100)
  protected String name;

  /** PO（Product Owner）の従業員ID。 */
  @Column(name = "po_employee_id")
  protected SnowflakeId poEmployeeId;

  /** POの名前。 */
  @Column(name = "po_name", length = 100)
  protected String poName;

  /** SM（Scrum Master）の従業員ID。 */
  @Column(name = "sm_employee_id")
  protected SnowflakeId smEmployeeId;

  /** SMの名前。 */
  @Column(name = "sm_name", length = 100)
  protected String smName;

  /** チームメンバーの従業員IDリスト（PO/SMを含む）。 */
  @Column(name = "member_employee_ids", columnDefinition = "BIGINT[]")
  @Convert(converter = SnowflakeId.SnowflakeIdListConverter.class)
  @Builder.Default
  protected List<SnowflakeId> memberEmployeeIds = new ArrayList<>();

  /** ステークホルダーの従業員IDリスト（組織ユニットに所属しない従業員）。 */
  @Column(name = "stakeholder_employee_ids", columnDefinition = "BIGINT[]")
  @Convert(converter = SnowflakeId.SnowflakeIdListConverter.class)
  @Builder.Default
  protected List<SnowflakeId> stakeholderEmployeeIds = new ArrayList<>();

  /** サブセットが最後にイベントで更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** イベントのスナップショット内容でサブセットを更新します。 */
  void apply(@NonNull ScrumTeamChanged event) {
    this.name = event.name();
    this.poEmployeeId = event.poEmployeeId();
    this.poName = event.poName();
    this.smEmployeeId = event.smEmployeeId();
    this.smName = event.smName();
    this.memberEmployeeIds = event.memberEmployeeIds() != null
        ? new ArrayList<>(event.memberEmployeeIds()) : new ArrayList<>();
    this.stakeholderEmployeeIds = event.stakeholderEmployeeIds() != null
        ? new ArrayList<>(event.stakeholderEmployeeIds()) : new ArrayList<>();
    this.updatedAt = LocalDateTime.now();
  }

  /** イベントから新しいサブセットを作成します。 */
  static ScrumTeamSubset from(@NonNull ScrumTeamChanged event) {
    return ScrumTeamSubset.builder()
        .id(event.scrumTeamId())
        .name(event.name())
        .poEmployeeId(event.poEmployeeId())
        .poName(event.poName())
        .smEmployeeId(event.smEmployeeId())
        .smName(event.smName())
        .memberEmployeeIds(event.memberEmployeeIds() != null
            ? new ArrayList<>(event.memberEmployeeIds()) : new ArrayList<>())
        .stakeholderEmployeeIds(event.stakeholderEmployeeIds() != null
            ? new ArrayList<>(event.stakeholderEmployeeIds()) : new ArrayList<>())
        .build();
  }

  /**
   * 指定した従業員がこのチームのバックログアイテムの状態を変更できるか（SMまたはPOか）を判定します。
   *
   * @param actorEmployeeId 操作者の従業員ID
   * @return SMまたはPOであれば true
   */
  boolean canChangeStatusBy(@Nullable SnowflakeId actorEmployeeId) {
    if (actorEmployeeId == null) {
      return false;
    }
    return actorEmployeeId.equals(this.poEmployeeId) || actorEmployeeId.equals(this.smEmployeeId);
  }

  /**
   * 指定した従業員がこのチームのPO（Product Owner）かを判定します。
   *
   * <p>FROZEN → SUGGESTED（復帰）は PO のみが実行できるため、
   * 遷移先に応じた権限判定に使用します。
   *
   * @param actorEmployeeId 操作者の従業員ID
   * @return PO であれば true
   */
  boolean isPo(@Nullable SnowflakeId actorEmployeeId) {
    if (actorEmployeeId == null) {
      return false;
    }
    return actorEmployeeId.equals(this.poEmployeeId);
  }

  /**
   * 指定した従業員がこのスクラムチームのメンバーかを判定します。
   *
   * <p>PO/SM を含むチームメンバー全員が ACCEPTED→READY および READY→SUGGESTED の
   * 状態遷移を実行できるため、権限判定に使用します。
   *
   * @param actorEmployeeId 操作者の従業員ID
   * @return チームメンバーであれば true
   */
  boolean isTeamMember(@Nullable SnowflakeId actorEmployeeId) {
    if (actorEmployeeId == null) {
      return false;
    }
    // PO/SM は常にメンバーとして扱う
    if (actorEmployeeId.equals(this.poEmployeeId) || actorEmployeeId.equals(this.smEmployeeId)) {
      return true;
    }
    // memberEmployeeIds リストに含まれるか確認
    return this.memberEmployeeIds != null && this.memberEmployeeIds.contains(actorEmployeeId);
  }

  /**
   * 指定した従業員がこのスクラムチームのステークホルダーかを判定します。
   *
   * <p>ステークホルダーは組織ユニットに所属しない従業員で、所属手続きなしに
   * バックログアイテムの閲覧（添付ファイルの一覧・ダウンロード等）ができます。
   *
   * @param actorEmployeeId 操作者の従業員ID
   * @return ステークホルダーであれば true
   */
  boolean isStakeholder(@Nullable SnowflakeId actorEmployeeId) {
    if (actorEmployeeId == null) {
      return false;
    }
    return this.stakeholderEmployeeIds != null
        && this.stakeholderEmployeeIds.contains(actorEmployeeId);
  }
}
