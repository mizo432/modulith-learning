package undecided.agiloom.sprintExecution.internal.presentation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintResponse {
    private SnowflakeId id;
    private UUID productId;
    private Integer sprintNumber;
    private String title;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    private SprintStatus status;
    private UUID teamId;
    private String teamName;
    private Integer velocity;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
