package undecided.agiloom.sprintExecution.spi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * Sprintの値オブジェクトです。
 * <p>
 * SPIとして外部に公開される不変のSprintデータモデルです。
 * JPAエンティティの詳細を外部に漏洩させず、モジュール間連携に使用します。
 * </p>
 *
 * @see SprintStatus
 */
@Getter
public class SprintDTO {

    @NonNull
    private final SnowflakeId id;

    @NonNull
    private final UUID productId;

    @NonNull
    private final Integer sprintNumber;

    @NonNull
    private final String title;

    @Nullable
    private final String goal;

    @NonNull
    private final LocalDate startDate;

    @NonNull
    private final LocalDate endDate;

    @NonNull
    private final SprintStatus status;

    @NonNull
    private final UUID teamId;

    @Nullable
    private final String teamName;

    @Nullable
    private final Integer velocity;

    @Nullable
    private final String notes;

    @NonNull
    private final LocalDateTime createdAt;

    @NonNull
    private final LocalDateTime updatedAt;

    @Builder
    public SprintDTO(
        @NonNull SnowflakeId id,
        @NonNull UUID productId,
        @NonNull Integer sprintNumber,
        @NonNull String title,
        @Nullable String goal,
        @NonNull LocalDate startDate,
        @NonNull LocalDate endDate,
        @NonNull SprintStatus status,
        @NonNull UUID teamId,
        @Nullable String teamName,
        @Nullable Integer velocity,
        @Nullable String notes,
        @NonNull LocalDateTime createdAt,
        @NonNull LocalDateTime updatedAt
    ) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(sprintNumber, "sprintNumber must not be null");
        Objects.requireNonNull(title, "title must not be null");
        Objects.requireNonNull(startDate, "startDate must not be null");
        Objects.requireNonNull(endDate, "endDate must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(teamId, "teamId must not be null");
        Objects.requireNonNull(createdAt, "createdAt must not be null");
        Objects.requireNonNull(updatedAt, "updatedAt must not be null");
        this.id = id;
        this.productId = productId;
        this.sprintNumber = sprintNumber;
        this.title = title;
        this.goal = goal;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.teamId = teamId;
        this.teamName = teamName;
        this.velocity = velocity;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
