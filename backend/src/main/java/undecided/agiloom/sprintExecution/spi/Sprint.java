package undecided.agiloom.sprintExecution.spi;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import com.github.mizo432.shared.common.dateProvider.DateProvider;
import com.github.mizo432.shared.common.exception.BusinessException;
import com.github.mizo432.shared.common.message.ResultMessage;
import com.github.mizo432.shared.common.message.ResultMessages;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

@Entity
@Table(
    name = "sprints",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "uk_sprint_product_number",
          columnNames = {"productId", "sprintNumber"})
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Sprint {
  @Id private SnowflakeId id;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @Column(name = "sprint_number", nullable = false)
  private Integer sprintNumber;

  @Column(nullable = false, length = 100)
  private String title;

  @Column(name = "goal", length = 1000)
  private String goal;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SprintStatus status = SprintStatus.PLANNED;

  @Column(name = "team_id", nullable = false)
  private UUID teamId;

  @Column(name = "team_name", length = 100)
  private String teamName;

  @Column(length = 10)
  private Integer velocity;

  @Column(length = 2000)
  private String notes;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = DateProvider.currentLocalDateTime();
    this.updatedAt = DateProvider.currentLocalDateTime();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = DateProvider.currentLocalDateTime();
  }

  @PostLoad
  protected void onLoad() {
    // @Builder.DefaultはJPAロード時に機能しないため、nullの場合はデフォルト値を設定する
    if (this.status == null) {
      this.status = SprintStatus.PLANNED;
    }
  }

  /**
   * 新しいSprintを作成する静的ファクトリーメソッドです。
   *
   * @param id SprintのID
   * @param productId 製品ID
   * @param sprintNumber スプリント番号
   * @param title タイトル
   * @param goal 目標
   * @param startDate 開始日
   * @param endDate 終了日
   * @param teamId チームID
   * @param teamName チーム名
   * @param velocity ベロシティ
   * @param status ステータス（nullの場合PLANNED）
   * @param createdAt 作成日時（nullの場合自動設定）
   * @param updatedAt 更新日時（nullの場合自動設定）
   * @return 新しく作成されたSprintインスタンス
   */
  public static Sprint of(
      SnowflakeId id,
      UUID productId,
      Integer sprintNumber,
      String title,
      String goal,
      LocalDate startDate,
      LocalDate endDate,
      UUID teamId,
      String teamName,
      Integer velocity,
      SprintStatus status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    checkNotNull(id, () -> new IllegalArgumentException("id must not be null"));
    checkNotNull(productId, () -> new IllegalArgumentException("productId must not be null"));
    checkNotNull(sprintNumber, () -> new IllegalArgumentException("sprintNumber must not be null"));
    checkNotNull(title, () -> new IllegalArgumentException("title must not be null"));
    checkNotNull(startDate, () -> new IllegalArgumentException("startDate must not be null"));
    checkNotNull(endDate, () -> new IllegalArgumentException("endDate must not be null"));
    checkNotNull(teamId, () -> new IllegalArgumentException("teamId must not be null"));
    
    Sprint sprint = new Sprint();
    sprint.id = id;
    sprint.productId = productId;
    sprint.sprintNumber = sprintNumber;
    sprint.title = title;
    sprint.goal = goal;
    sprint.startDate = startDate;
    sprint.endDate = endDate;
    sprint.teamId = teamId;
    sprint.teamName = teamName;
    sprint.velocity = velocity;
    sprint.status = (status != null) ? status : SprintStatus.PLANNED;
    sprint.createdAt = createdAt;
    sprint.updatedAt = updatedAt;
    return sprint;
  }

  public void start() {
    if (this.deletedAt != null) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText("Sprint is already deleted")));
    }
    if (this.status != SprintStatus.PLANNED) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText(
              "Sprint can only be started from PLANNED status, current status: " + this.status)));
    }
    this.status = SprintStatus.ACTIVE;
  }

  public void complete() {
    if (this.deletedAt != null) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText("Sprint is already deleted")));
    }
    if (this.status != SprintStatus.ACTIVE) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText(
              "Sprint can only be completed from ACTIVE status, current status: " + this.status)));
    }
    this.status = SprintStatus.COMPLETED;
  }

  public void cancel() {
    if (this.deletedAt != null) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText("Sprint is already deleted")));
    }
    if (this.status != SprintStatus.ACTIVE && this.status != SprintStatus.PLANNED) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText(
              "Sprint can only be cancelled from ACTIVE or PLANNED status, current status: "
                  + this.status)));
    }
    this.status = SprintStatus.CANCELLED;
  }

  public void update(
      @Nullable String title,
      @Nullable String goal,
      @Nullable LocalDate endDate,
      @Nullable String notes) {
    if (this.deletedAt != null) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText("Sprint is already deleted")));
    }
    String newTitle = (title != null) ? title : this.title;
    String newGoal = (goal != null) ? goal : this.goal;
    checkNotNull(newTitle, () -> new BusinessException(
        ResultMessages.error().add(ResultMessage.fromText("title must not be null"))));
    checkNotNull(newGoal, () -> new BusinessException(
        ResultMessages.error().add(ResultMessage.fromText("goal must not be null"))));
    if (newTitle.trim().isEmpty()) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText("title must not be empty")));
    }
    if (newTitle.trim().length() > 100) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText("title must not exceed 100 characters")));
    }
    if (newGoal.trim().isEmpty()) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText("goal must not be empty")));
    }
    if (newGoal.trim().length() > 1000) {
      throw new BusinessException(
          ResultMessages.error().add(ResultMessage.fromText("goal must not exceed 1000 characters")));
    }
    this.title = newTitle;
    this.goal = newGoal;
    if (endDate != null) {
      if (!endDate.isAfter(this.startDate)) {
        throw new BusinessException(
            ResultMessages.error().add(ResultMessage.fromText("endDate must be after startDate")));
      }
      this.endDate = endDate;
    }
    this.notes = notes;
  }

  void setStatus(SprintStatus status) {
    this.status = status;
  }

  @jakarta.annotation.Generated("Test")
  void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sprint sprint = (Sprint) o;
    return Objects.equals(id, sprint.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
