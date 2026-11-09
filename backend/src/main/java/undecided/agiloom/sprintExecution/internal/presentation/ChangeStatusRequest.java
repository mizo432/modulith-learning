package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import undecided.agiloom.sprintExecution.spi.SprintStatus;

@Getter
@Builder
public class ChangeStatusRequest {
    @NotNull
    private final SprintStatus status;
}
