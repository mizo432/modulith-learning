package undecided.agiloom.sprintExecution.internal.presentation;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String productId;
    private Integer sprintNumber;
    private String title;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    private SprintStatus status;
    private String teamId;
    private String teamName;
    private Integer velocity;
    private String notes;
    private Boolean isSprintZero;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
