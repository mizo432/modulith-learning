package undecided.erp.scrum.application.command.epic;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.epic.Epic;
import undecided.erp.scrum.domain.model.epic.EpicRepository;
import undecided.erp.scrum.domain.model.product.ProductRepository;
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
public class EpicCommand {

  private final EpicRepository epicRepository;
  private final ProductRepository productRepository;
  private final UserStoryRepository userStoryRepository;
  private final SprintRepository sprintRepository;

  /**
   * 新しいエピックを作成します。
   *
   * @param epic 作成するエピック
   * @return 作成されたエピック
   */
  @Transactional
  public Epic createEpic(Epic epic) {
    // エピックIDが設定されていない場合は新しいIDを生成
    if (epic.getEpicId() == null) {
      epic.setEpicId(SnowflakeId.newInstance());
    }
    return epicRepository.save(epic);
  }

  /**
   * 指定されたIDのエピックを取得します。
   *
   * @param epicId 取得するエピックのID
   * @return 指定されたIDのエピック（存在しない場合はEmpty）
   */
  public Optional<Epic> getEpicById(SnowflakeId epicId) {
    return epicRepository.findById(epicId);
  }

  /**
   * すべてのエピックを取得します。
   *
   * @return すべてのエピックのリスト
   */
  public List<Epic> getAllEpics() {
    return epicRepository.findAll();
  }

  /**
   * 指定されたプロダクトに関連するすべてのエピックを取得します。
   *
   * @param productId 検索対象のプロダクトID
   * @return 指定されたプロダクトに関連するエピックのリスト
   */
  public List<Epic> getEpicsByProductId(SnowflakeId productId) {
    return epicRepository.findByProductProductId(productId);
  }

  /**
   * 指定されたステータスを持つエピックを取得します。
   *
   * @param status 検索対象のステータス
   * @return 指定されたステータスを持つエピックのリスト
   */
  public List<Epic> getEpicsByStatus(Epic.Status status) {
    return epicRepository.findByStatus(status);
  }

  /**
   * エピックを更新します。
   *
   * @param epicId 更新するエピックのID
   * @param updatedEpic 更新内容を含むエピック
   * @return 更新されたエピック（存在しない場合はEmpty）
   */
  @Transactional
  public Optional<Epic> updateEpic(SnowflakeId epicId, Epic updatedEpic) {
    return epicRepository.findById(epicId)
        .map(existingEpic -> {
          // 更新可能なフィールドを設定
          if (updatedEpic.getTitle() != null) {
            existingEpic.setTitle(updatedEpic.getTitle());
          }
          if (updatedEpic.getDescription() != null) {
            existingEpic.setDescription(updatedEpic.getDescription());
          }
          if (updatedEpic.getStatus() != null) {
            existingEpic.setStatus(updatedEpic.getStatus());
          }
          if (updatedEpic.getPriority() != null) {
            existingEpic.setPriority(updatedEpic.getPriority());
          }
          if (updatedEpic.getStartDate() != null) {
            existingEpic.setStartDate(updatedEpic.getStartDate());
          }
          if (updatedEpic.getEndDate() != null) {
            existingEpic.setEndDate(updatedEpic.getEndDate());
          }
          return epicRepository.save(existingEpic);
        });
  }

  /**
   * 指定されたIDのエピックを削除します。
   *
   * @param epicId 削除するエピックのID
   */
  @Transactional
  public void deleteEpic(SnowflakeId epicId) {
    epicRepository.deleteById(epicId);
  }

  /**
   * 複数のスプリントにまたがるエピックを取得します。
   *
   * @return 複数のスプリントにまたがるエピックのリスト
   */
  public List<Epic> getEpicsSpanningMultipleSprints() {
    return epicRepository.findEpicsSpanningMultipleSprints();
  }

  /**
   * 指定されたエピックに関連するスプリントを取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 関連するスプリントのリスト
   */
  public List<Sprint> getSprintsByEpicId(SnowflakeId epicId) {
    List<SnowflakeId> sprintIds = epicRepository.findSprintIdsByEpicId(epicId);
    return sprintIds.stream()
        .map(sprintId -> sprintRepository.findById(sprintId))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  /**
   * ユーザーストーリーをエピックに関連付けます。
   *
   * @param epicId 関連付けるエピックのID
   * @param storyId 関連付けるユーザーストーリーのID
   * @return 更新されたユーザーストーリー（エピックまたはユーザーストーリーが存在しない場合はEmpty）
   */
  @Transactional
  public Optional<UserStory> associateUserStoryWithEpic(SnowflakeId epicId, SnowflakeId storyId) {
    Optional<Epic> epicOpt = epicRepository.findById(epicId);
    Optional<UserStory> storyOpt = userStoryRepository.findById(storyId);

    if (epicOpt.isPresent() && storyOpt.isPresent()) {
      UserStory userStory = storyOpt.get();
      userStory.setEpic(epicOpt.get());
      return Optional.of(userStoryRepository.save(userStory));
    }
    return Optional.empty();
  }

  /**
   * ユーザーストーリーとエピックの関連付けを解除します。
   *
   * @param storyId 関連付けを解除するユーザーストーリーのID
   * @return 更新されたユーザーストーリー（存在しない場合はEmpty）
   */
  @Transactional
  public Optional<UserStory> disassociateUserStoryFromEpic(SnowflakeId storyId) {
    return userStoryRepository.findById(storyId)
        .map(userStory -> {
          userStory.setEpic(null);
          return userStoryRepository.save(userStory);
        });
  }

  /**
   * 指定されたエピックに関連するユーザーストーリーを取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 関連するユーザーストーリーのリスト
   */
  public List<UserStory> getUserStoriesByEpicId(SnowflakeId epicId) {
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
   * 指定されたプロダクトに新しいエピックを作成します。
   *
   * @param productId エピックを作成するプロダクトのID
   * @param epic 作成するエピックの詳細
   * @return 作成されたエピック（プロダクトが存在しない場合はEmpty）
   */
  @Transactional
  public Optional<Epic> createEpicForProduct(SnowflakeId productId, Epic epic) {
    return productRepository.findById(productId)
        .map(product -> {
          epic.setProduct(product);
          if (epic.getEpicId() == null) {
            epic.setEpicId(SnowflakeId.newInstance());
          }
          return epicRepository.save(epic);
        });
  }

  /**
   * 複数のスプリントにまたがるユーザーストーリーを持つエピックを取得します。
   *
   * @return 複数のスプリントにまたがるユーザーストーリーを持つエピックのリスト
   */
  public List<Epic> getEpicsWithUserStoriesSpanningMultipleSprints() {
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
