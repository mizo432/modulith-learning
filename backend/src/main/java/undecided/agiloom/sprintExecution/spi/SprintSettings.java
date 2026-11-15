package undecided.agiloom.sprintExecution.spi;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * スプリント設定エンティティ。
 *
 * <p>プロダクトごとにスプリント期間（営業日数）と終了曜日を設定します。
 * デフォルト値は10営業日、金曜日です。
 */
@Entity
@Table(
    schema = "undecided_ag",
    name = "sprint_settings",
    uniqueConstraints = {
      @UniqueConstraint(name = "uk_sprint_settings_product", columnNames = {"productId"})
    })
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SprintSettings {

  /** スプリント設定の一意な識別子。 */
  @Id protected SnowflakeId id;

  /** 関連するプロダクトのID。 */
  @Column(name = "product_id", nullable = false)
  protected UUID productId;

  /** スプリント期間（営業日数）。1〜30日の範囲で設定可能です。デフォルトは10日です。 */
  @Column(name = "business_days", nullable = false)
  @Builder.Default
  protected Integer businessDays = 10;

  /** スプリント終了曜日。月曜日〜金曜日のみを設定可能です。デフォルトは金曜日です。 */
  @Enumerated(EnumType.STRING)
  @Column(name = "end_day_of_week", nullable = false, length = 10)
  @Builder.Default
  protected EndDayOfWeek endDayOfWeek = EndDayOfWeek.FRIDAY;

  /** スプリント設定が作成された日時。 */
  @Column(name = "created_at", nullable = false, updatable = false)
  @Builder.Default
  protected LocalDateTime createdAt = LocalDateTime.now();

  /** スプリント設定が最後に更新された日時。 */
  @Column(name = "updated_at", nullable = false)
  @Builder.Default
  protected LocalDateTime updatedAt = LocalDateTime.now();

  /** スプリント設定が論理削除された日時。nullの場合は未削除です。 */
  @Column(name = "deleted_at")
  protected LocalDateTime deletedAt;

  /**
   * スプリント期間（営業日数）と終了曜日を更新します。
   *
   * @param businessDays 営業日数（1〜30）
   * @param endDayOfWeek 終了曜日
   */
  public void update(@NonNull Integer businessDays, @NonNull EndDayOfWeek endDayOfWeek) {
    checkNotNull(businessDays, () -> new IllegalArgumentException("businessDays must not be null"));
    checkNotNull(endDayOfWeek, () -> new IllegalArgumentException("endDayOfWeek must not be null"));

    if (businessDays <= 0) {
      throw new IllegalArgumentException("businessDays must be positive");
    }
    if (businessDays > 30) {
      throw new IllegalArgumentException("businessDays must be 30 or less");
    }

    this.businessDays = businessDays;
    this.endDayOfWeek = endDayOfWeek;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * 開始日から営業日数と終了曜日に基づいてスプリントの終了日を計算します。
   * 祝日は除外されます。
   *
   * @param startDate スプリントの開始日
   * @param holidays 祝日リスト（日付のみ）
   * @return 計算された終了日
   */
  public LocalDate calculateEndDate(@NonNull LocalDate startDate, @NonNull Set<LocalDate> holidays) {
    checkNotNull(startDate, () -> new IllegalArgumentException("startDate must not be null"));
    checkNotNull(holidays, () -> new IllegalArgumentException("holidays must not be null"));

    LocalDate endDate = startDate;
    int countedDays = 0;

    while (countedDays < businessDays) {
      endDate = endDate.plusDays(1);
      // 土日と祝日を除外
      int dayValue = endDate.getDayOfWeek().getValue();
      if (dayValue < 6 && !holidays.contains(endDate)) {
        countedDays++;
      }
    }

    // 終了日が指定された終了曜日かつ営業日になるまで進める
    while (true) {
      int dayValue = endDate.getDayOfWeek().getValue();
      // 土日または祝日の場合は次の日に進める
      if (dayValue >= 6 || holidays.contains(endDate)) {
        endDate = endDate.plusDays(1);
        continue;
      }
      // 現在の終了日が指定された終了曜日なら終了
      if (endDate.getDayOfWeek() == endDayOfWeek.getDayOfWeek()) {
        break;
      }
      endDate = endDate.plusDays(1);
    }

    return endDate;
  }

  /** スプリント設定を論理削除します。 */
  public void delete() {
    this.deletedAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }
}
