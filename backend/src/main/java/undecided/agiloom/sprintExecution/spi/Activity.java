package undecided.agiloom.sprintExecution.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
 * アクティビティエンティティ。
 *
 * <p>Sprint 内のアクティビティを記録します。
 */
@Entity
@Table(name = "activities")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected SnowflakeId id;

    @Column(name = "sprint_id", nullable = false)
    protected SnowflakeId sprintId;

    @Column(name = "actor_id", nullable = false)
    protected UUID actorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false, length = 30)
    protected ActivityType activityType;

    @Column(length = 2000)
    protected String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    protected LocalDateTime createdAt = LocalDateTime.now();

    /**
     * アクティビティを新規作成します。
     *
     * @param id ID
     * @param sprintId Sprint ID
     * @param actorId アクター ID
     * @param activityType アクティビティ種別
     * @param description 説明
     * @param createdAt 作成日時
     * @return 新規作成されたActivityインスタンス
     */
    public static Activity create(
            @NonNull SnowflakeId id,
            @NonNull SnowflakeId sprintId,
            @NonNull UUID actorId,
            @NonNull ActivityType activityType,
            @Nullable String description,
            @NonNull LocalDateTime createdAt) {
        return Activity.builder()
                .id(id)
                .sprintId(sprintId)
                .actorId(actorId)
                .activityType(activityType)
                .description(description)
                .createdAt(createdAt)
                .build();
    }
}
