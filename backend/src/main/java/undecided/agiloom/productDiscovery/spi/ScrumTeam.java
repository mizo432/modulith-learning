package undecided.agiloom.productDiscovery.spi;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  protected UUID poEmployeeId;

  /** POの名前（非正規化フィールド）。 */
  @Column(name = "po_name", length = 100)
  protected String poName;

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
   */
  public void update(
      @Nullable String name,
      @Nullable String businessDomain,
      @Nullable UUID poEmployeeId,
      @Nullable String poName) {
    if (name != null) {
      checkNotNull(name, () -> new IllegalArgumentException("name must not be null"));
      this.name = name;
    }
    if (businessDomain != null) {
      checkNotNull(businessDomain, () -> new IllegalArgumentException("businessDomain must not be null"));
      this.businessDomain = businessDomain;
    }
    if (poEmployeeId != null) {
      this.poEmployeeId = poEmployeeId;
    }
    if (poName != null) {
      this.poName = poName;
    }
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * PO（Product Owner）を割り当てます。
   *
   * @param poEmployeeId POの従業員ID
   * @param poName POの名前
   */
  public void assignPo(@NonNull UUID poEmployeeId, @NonNull String poName) {
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
}
