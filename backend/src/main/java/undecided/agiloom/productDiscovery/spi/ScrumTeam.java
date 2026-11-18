package undecided.agiloom.productDiscovery.spi;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;
import static com.github.mizo432.shared.common.precondition.StringPrecondition.checkNonEmpty;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
import undecided.supporting.snowflake.SnowflakeId;

/**
 * スクラムチームエンティティ。
 *
 * <p>Productに属するスクラムチームの基本情報を管理します。
 * チーム名、事業領域、PO（Product Owner）の情報を保持し、1つのチームには1人のPOが割り当てられます。
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

  /** チーム名。 */
  @Column(nullable = false, length = 100)
  protected String name;

  /** 事業領域。 */
  @Column(name = "business_domain", nullable = false, length = 100)
  protected String businessDomain;

  /** PO（Product Owner）の従業員ID。 */
  @Column(name = "po_employee_id")
  protected SnowflakeId poEmployeeId;

  /** POの名前（非正規化フィールド）。 */
  @Column(name = "po_name", length = 100)
  protected String poName;

  /** SM（Scrum Master）の従業員ID。 */
  @Column(name = "sm_employee_id")
  protected SnowflakeId smEmployeeId;

  /** SMの名前（非正規化フィールド）。 */
  @Column(name = "sm_name", length = 100)
  protected String smName;

  /** チームメンバーの従業員IDリスト（PO/SMを含む）。 */
  @Column(name = "member_employee_ids", columnDefinition = "BIGINT[]")
  @Convert(converter = SnowflakeId.SnowflakeIdListConverter.class)
  @Builder.Default
  protected List<SnowflakeId> memberEmployeeIds = new ArrayList<>();

  /** 関連するProductへの参照。 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  protected Product product;

  /** スクラムチームが作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** スクラムチームが最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /**
   * スクラムチーム情報を更新します。
   *
   * @param name 更新後のチーム名（nullの場合は変更なし）
   * @param businessDomain 更新後の事業領域（nullの場合は変更なし）
   * @param poEmployeeId 更新後のPO従業員ID（nullの場合は変更なし）
   * @param poName 更新後のPO名前（nullの場合は変更なし）
   * @param smEmployeeId 更新後のSM従業員ID（nullの場合は変更なし）
   * @param smName 更新後のSM名前（nullの場合は変更なし）
   */
  public void update(
      @Nullable String name,
      @Nullable String businessDomain,
      @Nullable SnowflakeId poEmployeeId,
      @Nullable String poName,
      @Nullable SnowflakeId smEmployeeId,
      @Nullable String smName) {
    if (name != null) {
      checkNonEmpty(name, () -> new IllegalArgumentException("ScrumTeam name must not be empty"));
      this.name = name;
    }
    if (businessDomain != null) {
      checkNonEmpty(
          businessDomain, () -> new IllegalArgumentException("Business domain must not be empty"));
      this.businessDomain = businessDomain;
    }
    if (poEmployeeId != null) {
      this.poEmployeeId = poEmployeeId;
    }
    if (poName != null) {
      this.poName = poName;
    }
    if (smEmployeeId != null) {
      this.smEmployeeId = smEmployeeId;
    }
    if (smName != null) {
      this.smName = smName;
    }
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * PO（Product Owner）を割り当てます。
   *
   * @param poEmployeeId POの従業員ID
   * @param poName POの名前
   */
  public void assignPo(@NonNull SnowflakeId poEmployeeId, @NonNull String poName) {
    checkNotNull(poEmployeeId, () -> new IllegalArgumentException("poEmployeeId must not be null"));
    checkNotNull(poName, () -> new IllegalArgumentException("poName must not be null"));
    this.poEmployeeId = poEmployeeId;
    this.poName = poName;
    this.updatedAt = LocalDateTime.now();
  }

  /** PO（Product Owner）の割り当てを解除します。 */
  public void unassignPo() {
    this.poEmployeeId = null;
    this.poName = null;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * SM（Scrum Master）を割り当てます。
   *
   * @param smEmployeeId SMの従業員ID
   * @param smName SMの名前
   */
  public void assignSm(@NonNull SnowflakeId smEmployeeId, @NonNull String smName) {
    checkNotNull(smEmployeeId, () -> new IllegalArgumentException("smEmployeeId must not be null"));
    checkNotNull(smName, () -> new IllegalArgumentException("smName must not be null"));
    this.smEmployeeId = smEmployeeId;
    this.smName = smName;
    this.updatedAt = LocalDateTime.now();
  }

  /** SM（Scrum Master）の割り当てを解除します。 */
  public void unassignSm() {
    this.smEmployeeId = null;
    this.smName = null;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * チームメンバーの従業員IDリストを設定します。
   *
   * @param memberEmployeeIds メンバーの従業員IDリスト（PO/SMを含む）
   */
  public void setMemberEmployeeIds(@NonNull List<SnowflakeId> memberEmployeeIds) {
    checkNotNull(memberEmployeeIds, () -> new IllegalArgumentException("memberEmployeeIds must not be null"));
    this.memberEmployeeIds = new ArrayList<>(memberEmployeeIds);
    this.updatedAt = LocalDateTime.now();
  }
}
