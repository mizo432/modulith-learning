package undecided.agiloom.requirementBacklog.spi;

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
 * フィーチャーエンティティ。
 *
 * <p>スクラムチームのバックログアイテムを論理的にグループ化するフィーチャーを管理します。
 * フィーチャー名は最大200文字、説明はTEXT型で上限なし（決定1）。
 *
 * <p>インスタンスは {@code @Builder} を使用して作成します。
 */
@Entity
@Table(schema = "undecided_ag", name = "features")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Feature {

  /** フィーチャー名の最大長。項目制約の単一の情報源（ADR-002）。 */
  public static final int NAME_MAX_LENGTH = 200;

  /** フィーチャーの一意な識別子。 */
  @Id protected SnowflakeId id;

  /** フィーチャー名（必須、最大200文字）。 */
  @Column(nullable = false, length = NAME_MAX_LENGTH)
  protected String name;

  /** 説明（任意、TEXT型で上限なし）。 */
  @Column(columnDefinition = "TEXT")
  protected String description;

  /** 状態（区分値）。DBにはコード値のみを保持する（ADR-003）。 */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  protected FeatureStatus status;

  /** 所属するスクラムチームのID。他領域のエンティティは参照せず、IDのみを保持する。 */
  @Column(name = "scrum_team_id", nullable = false)
  protected SnowflakeId scrumTeamId;

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
   * フィーチャーの内容を更新します。
   *
   * @param name 更新後の名前（nullの場合は変更なし）
   * @param description 更新後の説明（nullの場合は変更なし）
   */
  public void update(@Nullable String name, @Nullable String description, @NonNull LocalDateTime now) {
    if (name != null) {
      if (name.isEmpty()) {
        throw new IllegalArgumentException("Feature name must not be empty");
      }
      if (name.length() > NAME_MAX_LENGTH) {
        throw new IllegalArgumentException(
            "Feature name must not exceed " + NAME_MAX_LENGTH + " characters");
      }
      this.name = name;
    }
    if (description != null) {
      this.description = description;
    }
    this.updatedAt = now;
  }

  /**
   * 状態を変更します。
   *
   * <p>許可される遷移（決定7）：
   * <ul>
   *   <li>順方向: {@code Planned → InProgress}, {@code InProgress → Done}
   *   <li>差し戻し: {@code InProgress → Planned}, {@code Done → InProgress}
   * </ul>
   * 許可しない遷移: {@code Planned → Done}, {@code Done → Planned}
   *
   * @param target 遷移先の状態
   * @throws IllegalStateException 許可されていない遷移の場合
   */
  public void changeStatus(@NonNull FeatureStatus target, @NonNull LocalDateTime now) {
    boolean allowed = switch (this.status) {
      case PLANNED -> target == FeatureStatus.IN_PROGRESS;
      case IN_PROGRESS -> target == FeatureStatus.DONE || target == FeatureStatus.PLANNED;
      case DONE -> target == FeatureStatus.IN_PROGRESS;
    };
    if (!allowed) {
      throw new IllegalStateException(
          "Invalid status transition: " + this.status + " -> " + target);
    }
    this.status = target;
    this.updatedAt = now;
  }

  /**
   * フィーチャーをビルダーパターンで構築します。
   *
   * <p>ビルダーの {@code build()} 時に {@code status} が未設定の場合は {@link FeatureStatus#PLANNED} を、
   * {@code createdAt}・{@code updatedAt} が未設定の場合は例外をスローします。
   */
  @Builder(builderMethodName = "builder")
  private static Feature build(
      SnowflakeId id,
      String name,
      String description,
      FeatureStatus status,
      SnowflakeId scrumTeamId,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    Feature feature = new Feature();
    feature.id = id;
    feature.name = name;
    feature.description = description;
    feature.status = status != null ? status : FeatureStatus.PLANNED;
    feature.scrumTeamId = scrumTeamId;
    if (createdAt == null) {
      throw new IllegalArgumentException("createdAt must not be null");
    }
    if (updatedAt == null) {
      throw new IllegalArgumentException("updatedAt must not be null");
    }
    feature.createdAt = createdAt;
    feature.updatedAt = updatedAt;
    return feature;
  }
}
