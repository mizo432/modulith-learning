package undecided.erp.scrum.application.command.epic;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.domain.model.epic.Epic;
import undecided.erp.scrum.domain.model.epic.EpicRepository;
import undecided.erp.scrum.domain.model.product.ProductRepository;
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

}
