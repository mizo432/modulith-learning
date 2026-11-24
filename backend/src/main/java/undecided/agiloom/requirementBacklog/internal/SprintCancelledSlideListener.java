package undecided.agiloom.requirementBacklog.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import undecided.agiloom.sprintExecution.spi.SprintCancelled;
import undecided.agiloom.sprintExecution.spi.SprintFacade;

/**
 * スプリントキャンセル時に割り当て済みアイテムを次スプリントへスライドさせるリスナー（Issue #525、決定
 * #653）。
 */
@Component
@RequiredArgsConstructor
class SprintCancelledSlideListener {

  private final BacklogItemRepository backlogItemRepository;
  private final SprintFacade sprintFacade;

  @ApplicationModuleListener
  public void onSprintCancelled(SprintCancelled event) {
    throw new UnsupportedOperationException("not implemented yet");
  }
}
