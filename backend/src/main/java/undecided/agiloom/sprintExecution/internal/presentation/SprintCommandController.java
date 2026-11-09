package undecided.agiloom.sprintExecution.internal.presentation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import undecided.agiloom.sprintExecution.spi.SprintDTO;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommand;
import undecided.agiloom.sprintExecution.internal.command.CreateSprintCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.SendNotificationCommand;
import undecided.agiloom.sprintExecution.internal.command.SendNotificationCommandServiceInterface;
import undecided.agiloom.sprintExecution.internal.command.UpdateSprintCommandServiceInterface;
import undecided.supporting.notification.spi.NotificationDTO;
import undecided.supporting.snowflake.SnowflakeId;

@RestController
@RequestMapping("/api/v1/sprints")
public class SprintCommandController {

    private final CreateSprintCommandServiceInterface createSprintCommandService;
    private final UpdateSprintCommandServiceInterface updateSprintCommandService;
    private final SendNotificationCommandServiceInterface sendNotificationCommandService;
    private final SprintResponseAssembler sprintResponseAssembler;

    public SprintCommandController(CreateSprintCommandServiceInterface createSprintCommandService,
                            UpdateSprintCommandServiceInterface updateSprintCommandService,
                            SendNotificationCommandServiceInterface sendNotificationCommandService,
                            SprintResponseAssembler sprintResponseAssembler) {
        this.createSprintCommandService = createSprintCommandService;
        this.updateSprintCommandService = updateSprintCommandService;
        this.sendNotificationCommandService = sendNotificationCommandService;
        this.sprintResponseAssembler = sprintResponseAssembler;
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

        SprintDTO sprint = createSprintCommandService.execute(command);
        return ResponseEntity.ok(sprintResponseAssembler.toResponse(sprint));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<SprintResponse> changeStatus(@PathVariable String id,
                                   @Valid @RequestBody SprintStatus status) {
        SnowflakeId snowflakeId = SprintIdUtils.parseSnowflakeId(id);
        SprintDTO sprint = updateSprintCommandService.changeStatus(snowflakeId, status);
        return ResponseEntity.ok(sprintResponseAssembler.toResponse(sprint));
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
    public ResponseEntity<NotificationDTO> sendNotification(@Valid @RequestBody SendNotificationRequest request) {
        SendNotificationCommand command = SendNotificationCommand.builder()
                .recipientId(request.getRecipientId())
                .sourceId(request.getSourceId())
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .build();

        NotificationDTO notification = sendNotificationCommandService.execute(command);
        return ResponseEntity.status(201).body(notification);
    }
}
