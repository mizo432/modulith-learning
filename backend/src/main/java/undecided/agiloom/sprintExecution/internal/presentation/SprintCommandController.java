package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommand;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.SendNotificationCommand;
import undecided.agiloom.sprintExecution.internal.command.SendNotificationCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.query.SprintQueryServiceInterface;
import undecided.supporting.notification.spi.Notification;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/sprints")
public class SprintCommandController {

    private final CreateSprintCommandServiceInterface createSprintCommandService;
    private final UpdateSprintCommandServiceInterface updateSprintCommandService;
    private final SendNotificationCommandServiceInterface sendNotificationCommandService;
    private final SprintQueryServiceInterface sprintQueryService;
    private final SprintQueryController sprintQueryController;

    public SprintCommandController(CreateSprintCommandServiceInterface createSprintCommandService,
                            UpdateSprintCommandServiceInterface updateSprintCommandService,
                            SendNotificationCommandServiceInterface sendNotificationCommandService,
                            SprintQueryServiceInterface sprintQueryService,
                            SprintQueryController sprintQueryController) {
        this.createSprintCommandService = createSprintCommandService;
        this.updateSprintCommandService = updateSprintCommandService;
        this.sendNotificationCommandService = sendNotificationCommandService;
        this.sprintQueryService = sprintQueryService;
        this.sprintQueryController = sprintQueryController;
    }

    @PostMapping
    public ResponseEntity<SprintResponse> createSprint(@Valid @RequestBody CreateSprintRequest request) {
        CreateSprintCommand command = CreateSprintCommand.builder()
                .productId(request.getProductId())
                .sprintNumber(request.getSprintNumber())
                .title(request.getTitle())
                .goal(request.getGoal())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .teamId(request.getTeamId())
                .teamName(request.getTeamName())
                .build();

        SnowflakeId id = createSprintCommandService.execute(command);
        Sprint sprint = sprintQueryService.findById(id).orElseThrow();
        return ResponseEntity.ok(sprintQueryController.toResponse(sprint));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SprintResponse> changeStatus(@PathVariable @NotNull String id,
                                       @Valid @RequestBody @NotNull SprintStatus status) {
        SnowflakeId snowflakeId = parseSnowflakeId(id);
        updateSprintCommandService.changeStatus(snowflakeId, status);
        Sprint sprint = sprintQueryService.findById(snowflakeId).orElseThrow();
        return ResponseEntity.ok(sprintQueryController.toResponse(sprint));
    }

    /**
     * 通知を送信します。
     * <p>
     * UC-SP-013 通知を送信する
     * </p>
     *
     * @param request 通知送信リクエスト
     * @return 送信された通知
     */
    @PostMapping("/notifications")
    public ResponseEntity<Notification> sendNotification(@Valid @RequestBody SendNotificationRequest request) {
        SendNotificationCommand command = SendNotificationCommand.builder()
                .recipientId(request.getRecipientId())
                .sourceId(request.getSourceId())
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .build();

        Notification notification = sendNotificationCommandService.execute(command);
        return ResponseEntity.ok(notification);
    }

    // IDをSnowflakeIdに変換し、不正な形式の場合はIllegalArgumentExceptionをスローする
    private SnowflakeId parseSnowflakeId(String id) {
        try {
            long value = Long.parseLong(id);
            return SnowflakeId.of(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Sprint IDは数値である必要があります: " + id, e);
        }
    }
}
