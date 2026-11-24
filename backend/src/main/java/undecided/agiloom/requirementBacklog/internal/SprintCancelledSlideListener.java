package undecided.agiloom.requirementBacklog.internal;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import undecided.agiloom.requirementBacklog.spi.BacklogItem;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintCancelled;
import undecided.agiloom.sprintExecution.spi.SprintFacade;

/**
 * スプリントキャンセル時に割り当て済みアイテムを次スプリントへスライドさせるリスナー（Issue #525、決定
 * #653）。
 *
 * <p>次スプリントが存在しない場合は割り当てを解除します。スライドはシステムによる移動のため、
 * 割り当て変更イベント（通知）は発行しません。
 */
@Component
@RequiredArgsConstructor
class SprintCancelledSlideListener {

  private final BacklogItemRepository backlogItemRepository;
  private final SprintFacade sprintFacade;

  @ApplicationModuleListener
  public void onSprintCancelled(SprintCancelled event) {
    List<BacklogItem> assignedItems = backlogItemRepository.findBySprintId(event.sprintId());
    if (assignedItems.isEmpty()) {
      return;
    }
    Optional<Sprint> nextSprint =
        sprintFacade.findNextSprint(event.scrumTeamId(), event.sprintNumber());
    for (BacklogItem item : assignedItems) {
      if (nextSprint.isPresent()) {
        item.assignToSprint(nextSprint.get().getId());
      } else {
        item.unassignFromSprint();
      }
    }
    backlogItemRepository.saveAll(assignedItems);
  }
}
