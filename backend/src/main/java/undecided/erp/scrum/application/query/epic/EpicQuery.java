package undecided.erp.scrum.application.query.epic;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.epic.Epic;
import undecided.erp.scrum.domain.model.epic.EpicRepository;
import undecided.erp.scrum.domain.model.sprint.Sprint;
import undecided.erp.scrum.domain.model.sprint.SprintRepository;
import undecided.erp.scrum.domain.model.task.UserStory;
import undecided.erp.scrum.domain.model.task.UserStoryRepository;

/**
 * エピックに関連するビジネスロジックを提供するサービスクラス。
 * <p>
 * このサービスは、エピックの作成、取得、更新、削除などの操作と、 複数のスプリントにまたがるエピックの管理に関する機能を提供します。
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EpicQuery {

  private final EpicRepository epicRepository;
  private final UserStoryRepository userStoryRepository;
  private final SprintRepository sprintRepository;

  /**
   * 指定されたIDのエピックを取得します。
   *
   * @param epicId 取得するエピックのID
   * @return 指定されたIDのエピック（存在しない場合はEmpty）
   */
  public Optional<Epic> findEpicById(SnowflakeId epicId) {
    return epicRepository.findById(epicId);
  }

  /**
   * すべてのエピックを取得します。
   *
   * @return すべてのエピックのリスト
   */
  public List<Epic> selectAllEpics() {
    return epicRepository.findAll();
  }

  /**
   * 指定されたプロダクトに関連するすべてのエピックを取得します。
   *
   * @param productId 検索対象のプロダクトID
   * @return 指定されたプロダクトに関連するエピックのリスト
   */
  public List<Epic> selectEpicsByProductId(SnowflakeId productId) {
    return epicRepository.findByProductProductId(productId);
  }

  /**
   * 複数のスプリントにまたがるエピックを取得します。
   *
   * @return 複数のスプリントにまたがるエピックのリスト
   */
  public List<Epic> selectEpicsSpanningMultipleSprints() {
    return epicRepository.findEpicsSpanningMultipleSprints();
  }

  /**
   * 指定されたステータスを持つエピックを取得します。
   *
   * @param status 検索対象のステータス
   * @return 指定されたステータスを持つエピックのリスト
   */
  public List<Epic> selectEpicsByStatus(Epic.Status status) {
    return epicRepository.findByStatus(status);
  }

  /**
   * 指定されたエピックに関連するスプリントを取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 関連するスプリントのリスト
   */
  public List<Sprint> selectSprintsByEpicId(SnowflakeId epicId) {
    List<SnowflakeId> sprintIds = epicRepository.findSprintIdsByEpicId(epicId);
    return sprintIds.stream()
        .map(sprintId -> sprintRepository.findById(sprintId))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  /**
   * 指定されたエピックに関連するユーザーストーリーを取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 関連するユーザーストーリーのリスト
   */
  public List<UserStory> selectUserStoriesByEpicId(SnowflakeId epicId) {
    return userStoryRepository.findByEpicEpicId(epicId);
  }

  /**
   * 指定されたエピックに関連するユーザーストーリーの数を取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 関連するユーザーストーリーの数
   */
  public long countUserStoriesByEpicId(SnowflakeId epicId) {
    return epicRepository.countUserStoriesByEpicId(epicId);
  }

  /**
   * 複数のスプリントにまたがるユーザーストーリーを持つエピックを取得します。
   *
   * @return 複数のスプリントにまたがるユーザーストーリーを持つエピックのリスト
   */
  public List<Epic> selectEpicsWithUserStoriesSpanningMultipleSprints() {
    List<UserStory> userStories = userStoryRepository.findUserStoriesSpanningMultipleSprints();
    return userStories.stream()
        .map(UserStory::getEpic)
        .filter(epic -> epic != null)
        .distinct()
        .collect(Collectors.toList());
  }

  /**
   * 指定されたエピックに関連するユーザーストーリーが複数のスプリントにまたがっているかを確認します。
   *
   * @param epicId 検索対象のエピックID
   * @return 複数のスプリントにまたがっている場合はtrue、そうでない場合はfalse
   */
  public boolean isEpicSpanningMultipleSprints(SnowflakeId epicId) {
    List<UserStory> userStories = userStoryRepository.findUserStoriesInEpicSpanningMultipleSprints(
        epicId);
    return !userStories.isEmpty();
  }
}
