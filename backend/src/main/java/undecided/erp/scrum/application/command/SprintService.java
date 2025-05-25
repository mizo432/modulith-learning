package undecided.erp.scrum.application.command;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.product.Product;
import undecided.erp.scrum.domain.model.product.ProductRepository;
import undecided.erp.scrum.domain.model.sprint.Sprint;
import undecided.erp.scrum.domain.model.sprint.SprintBacklog;
import undecided.erp.scrum.domain.model.sprint.SprintBacklogRepository;
import undecided.erp.scrum.domain.model.sprint.SprintRepository;
import undecided.erp.scrum.domain.model.task.UserStory;
import undecided.erp.scrum.domain.model.task.UserStoryRepository;

/**
 * スプリント管理に関連する操作を提供するサービスクラス。
 *
 * <p>このクラスはスプリントの作成、開始、完了、およびスプリントバックログの管理などの スプリント関連の操作を提供します。
 */
@Service
@RequiredArgsConstructor
public class SprintService {

  private final SprintRepository sprintRepository;
  private final SprintBacklogRepository sprintBacklogRepository;
  private final ProductRepository productRepository;
  private final UserStoryRepository userStoryRepository;

  /**
   * 新しいスプリントを作成します。
   *
   * @param productId 関連するプロダクトのID
   * @param name スプリント名
   * @param goal スプリントゴール
   * @param startDate 開始日
   * @param endDate 終了日
   * @return 作成されたスプリント
   */
  @Transactional
  public Sprint createSprint(
      SnowflakeId productId, String name, String goal, LocalDate startDate, LocalDate endDate) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("指定されたプロダクトが見つかりません: " + productId));

    Sprint sprint =
        Sprint.builder()
            .sprintId(new SnowflakeId())
            .product(product)
            .name(name)
            .goal(goal)
            .startDate(startDate)
            .endDate(endDate)
            .status(Sprint.SprintStatus.PLANNED)
            .build();

    return sprintRepository.save(sprint);
  }

  /**
   * スプリントを開始します。
   *
   * @param sprintId 開始するスプリントのID
   * @return 更新されたスプリント
   */
  @Transactional
  public Sprint startSprint(SnowflakeId sprintId) {
    Sprint sprint =
        sprintRepository
            .findById(sprintId)
            .orElseThrow(() -> new IllegalArgumentException("指定されたスプリントが見つかりません: " + sprintId));

    sprint.start();
    return sprintRepository.save(sprint);
  }

  /**
   * スプリントを完了します。
   *
   * @param sprintId 完了するスプリントのID
   * @return 更新されたスプリント
   */
  @Transactional
  public Sprint completeSprint(SnowflakeId sprintId) {
    Sprint sprint =
        sprintRepository
            .findById(sprintId)
            .orElseThrow(() -> new IllegalArgumentException("指定されたスプリントが見つかりません: " + sprintId));

    sprint.complete();
    return sprintRepository.save(sprint);
  }

  /**
   * スプリントをキャンセルします。
   *
   * @param sprintId キャンセルするスプリントのID
   * @return 更新されたスプリント
   */
  @Transactional
  public Sprint cancelSprint(SnowflakeId sprintId) {
    Sprint sprint =
        sprintRepository
            .findById(sprintId)
            .orElseThrow(() -> new IllegalArgumentException("指定されたスプリントが見つかりません: " + sprintId));

    sprint.cancel();
    return sprintRepository.save(sprint);
  }

  /**
   * スプリントゴールを設定します。
   *
   * @param sprintId ゴールを設定するスプリントのID
   * @param goal 設定するゴール
   * @return 更新されたスプリント
   */
  @Transactional
  public Sprint setSprintGoal(SnowflakeId sprintId, String goal) {
    Sprint sprint =
        sprintRepository
            .findById(sprintId)
            .orElseThrow(() -> new IllegalArgumentException("指定されたスプリントが見つかりません: " + sprintId));

    sprint.setGoal(goal);
    return sprintRepository.save(sprint);
  }

  /**
   * ユーザーストーリーをスプリントバックログに追加します。
   *
   * @param sprintId スプリントID
   * @param storyId ユーザーストーリーID
   * @param name バックログアイテム名
   * @param description バックログアイテムの説明
   * @param estimatedEffort 見積もり工数
   * @return 作成されたスプリントバックログアイテム
   */
  @Transactional
  public SprintBacklog addUserStoryToSprint(
      SnowflakeId sprintId,
      SnowflakeId storyId,
      String name,
      String description,
      Integer estimatedEffort) {
    Sprint sprint =
        sprintRepository
            .findById(sprintId)
            .orElseThrow(() -> new IllegalArgumentException("指定されたスプリントが見つかりません: " + sprintId));

    UserStory userStory =
        userStoryRepository
            .findById(storyId)
            .orElseThrow(() -> new IllegalArgumentException("指定されたユーザーストーリーが見つかりません: " + storyId));

    SprintBacklog sprintBacklog =
        SprintBacklog.builder()
            .backlogId(new SnowflakeId())
            .sprint(sprint)
            .userStory(userStory)
            .name(name)
            .description(description)
            .estimatedEffort(estimatedEffort)
            .remainingEffort(estimatedEffort)
            .build();

    return sprintBacklogRepository.save(sprintBacklog);
  }

  /**
   * スプリントバックログアイテムの進捗を更新します。
   *
   * @param backlogId バックログアイテムID
   * @param remainingEffort 残り工数
   * @return 更新されたスプリントバックログアイテム
   */
  @Transactional
  public SprintBacklog updateBacklogItemProgress(SnowflakeId backlogId, Integer remainingEffort) {
    SprintBacklog backlogItem =
        sprintBacklogRepository
            .findById(backlogId)
            .orElseThrow(
                () -> new IllegalArgumentException("指定されたバックログアイテムが見つかりません: " + backlogId));

    backlogItem.updateRemainingEffort(remainingEffort);
    return sprintBacklogRepository.save(backlogItem);
  }

  /**
   * 未完了のストーリーを次のスプリントに移動します。
   *
   * @param fromSprintId 移動元のスプリントID
   * @param toSprintId 移動先のスプリントID
   * @return 移動されたバックログアイテムのリスト
   */
  @Transactional
  public List<SprintBacklog> moveIncompleteStoriesToNextSprint(
      SnowflakeId fromSprintId, SnowflakeId toSprintId) {
    Sprint fromSprint =
        sprintRepository
            .findById(fromSprintId)
            .orElseThrow(
                () -> new IllegalArgumentException("指定された移動元のスプリントが見つかりません: " + fromSprintId));

    Sprint toSprint =
        sprintRepository
            .findById(toSprintId)
            .orElseThrow(
                () -> new IllegalArgumentException("指定された移動先のスプリントが見つかりません: " + toSprintId));

    // 残り工数が0より大きい（未完了の）バックログアイテムを取得
    List<SprintBacklog> incompleteItems =
        sprintBacklogRepository.findBySprintSprintIdAndRemainingEffortGreaterThan(fromSprintId, 0);

    // 新しいスプリントに移動
    List<SprintBacklog> movedItems =
        incompleteItems.stream()
            .map(
                item -> {
                  SprintBacklog newItem =
                      SprintBacklog.builder()
                          .backlogId(new SnowflakeId())
                          .sprint(toSprint)
                          .userStory(item.getUserStory())
                          .name(item.getName())
                          .description(item.getDescription())
                          .estimatedEffort(item.getRemainingEffort()) // 残り工数を新しい見積もり工数として設定
                          .remainingEffort(item.getRemainingEffort())
                          .build();
                  return sprintBacklogRepository.save(newItem);
                })
            .collect(Collectors.toList());

    return movedItems;
  }

  /**
   * ユーザーストーリーを分割して一部を次のスプリントに移動します。
   *
   * @param storyId 分割するユーザーストーリーID
   * @param newTitle 新しいユーザーストーリーのタイトル
   * @param newDescription 新しいユーザーストーリーの説明
   * @param newStoryPoints 新しいユーザーストーリーのストーリーポイント
   * @param toSprintId 移動先のスプリントID
   * @return 分割されて作成された新しいユーザーストーリー
   */
  @Transactional
  public UserStory splitAndMoveStory(
      SnowflakeId storyId,
      String newTitle,
      String newDescription,
      Integer newStoryPoints,
      SnowflakeId toSprintId) {
    UserStory originalStory =
        userStoryRepository
            .findById(storyId)
            .orElseThrow(() -> new IllegalArgumentException("指定されたユーザーストーリーが見つかりません: " + storyId));

    Sprint toSprint =
        sprintRepository
            .findById(toSprintId)
            .orElseThrow(
                () -> new IllegalArgumentException("指定された移動先のスプリントが見つかりません: " + toSprintId));

    // ユーザーストーリーを分割
    UserStory newStory = originalStory.split(newTitle, newDescription, newStoryPoints);
    newStory.setStoryId(new SnowflakeId());
    newStory = userStoryRepository.save(newStory);

    // 新しいスプリントバックログアイテムを作成
    SprintBacklog newBacklogItem =
        SprintBacklog.builder()
            .backlogId(new SnowflakeId())
            .sprint(toSprint)
            .userStory(newStory)
            .name(newTitle)
            .description(newDescription)
            .estimatedEffort(newStoryPoints)
            .remainingEffort(newStoryPoints)
            .build();
    sprintBacklogRepository.save(newBacklogItem);

    return newStory;
  }
}
