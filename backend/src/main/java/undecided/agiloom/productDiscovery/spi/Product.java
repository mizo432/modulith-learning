package undecided.agiloom.productDiscovery.spi;

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
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * Productエンティティ。
 *
 * <p>Scrumプロジェクト管理におけるプロダクトの基本情報を管理します。 プロダクト名、説明、マネージャー等信息を持ち、状態遷移（ACTIVE → ARCHIVED）で管理されます。
 *
 * <p>C7修正により{@code @Data}は{@code @Getter}に置き換えられました。 インスタンスは{@code @Builder}を使用して作成します。
 * {@code @AllArgsConstructor}は{@code @Builder}の動作に必要な内部用コンストラクタです。
 */
@Entity
@Table(schema = "undecided_ag", name = "products")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {

  /** プロダクトの一意な識別子。 */
  @Id protected SnowflakeId id;

  /** プロダクトの名前。 */
  @Column(nullable = false, length = 100)
  protected String name;

  /** プロダクトの説明。Markdown形式で入力します。 */
  @Column(columnDefinition = "TEXT")
  protected String description;

  /** プロダクトマネージャーのID。 */
  @Column(name = "manager_id")
  protected UUID managerId;

  /** プロダクトのステータス（ACTIVE, ARCHIVED）。 */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Builder.Default
  protected ProductStatus status = ProductStatus.ACTIVE;

  /** プロダクトが作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** プロダクトが最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** プロダクトが論理削除された日時。nullの場合は未削除です。 */
  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

  /**
   * プロダクト情報を更新します。
   *
   * @param name 更新後の名前（nullの場合は変更なし）
   * @param description 更新後の説明（nullの場合は変更なし）
   * @param managerId 更新後のマネージャーID（nullの場合は変更なし）
   */
  public void update(
      @Nullable String name, @Nullable String description, @Nullable UUID managerId) {
    if (name != null) {
      checkNotNull(name, () -> new IllegalArgumentException("name must not be null"));
      this.name = name;
    }
    if (description != null) {
      this.description = description;
    }
    if (managerId != null) {
      this.managerId = managerId;
    }
    this.updatedAt = LocalDateTime.now();
  }

  /** プロダクトをアーカイブします。 ACTIVE状態からARCHIVED状態へ遷移します。 */
  public void archive() {
    if (this.status != ProductStatus.ACTIVE) {
      throw new IllegalStateException(
          "Product can only be archived from ACTIVE status, current status: " + this.status);
    }
    this.status = ProductStatus.ARCHIVED;
    this.updatedAt = LocalDateTime.now();
  }
}
