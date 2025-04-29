package undecided.erp.scrum.presentation.api;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.application.command.epic.EpicCommand;
import undecided.erp.scrum.domain.model.epic.Epic;
import undecided.erp.scrum.domain.model.sprint.Sprint;
import undecided.erp.scrum.domain.model.task.UserStory;

/**
 * エピックに関連するREST APIエンドポイントを提供するコントローラークラス。
 * <p>
 * このコントローラーは、エピックの作成、取得、更新、削除などの操作と、 複数のスプリントにまたがるエピックの管理に関するエンドポイントを提供します。
 */
@RestController
@RequestMapping("/api/epics")
@RequiredArgsConstructor
public class EpicApi {

  private final EpicCommand epicCommand;

  /**
   * すべてのエピックを取得します。
   *
   * @return すべてのエピックのリスト
   */
  @GetMapping
  public ResponseEntity<List<Epic>> getAllEpics() {
    return ResponseEntity.ok(epicCommand.getAllEpics());
  }

  /**
   * 指定されたIDのエピックを取得します。
   *
   * @param epicId 取得するエピックのID
   * @return 指定されたIDのエピック（存在しない場合は404）
   */
  @GetMapping("/{epicId}")
  public ResponseEntity<Epic> getEpicById(@PathVariable String epicId) {
    return epicCommand.getEpicById(SnowflakeId.of(Long.parseLong(epicId)))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * 新しいエピックを作成します。
   *
   * @param epic 作成するエピック
   * @return 作成されたエピック
   */
  @PostMapping
  public ResponseEntity<Epic> createEpic(@RequestBody Epic epic) {
    return ResponseEntity.status(HttpStatus.CREATED).body(epicCommand.createEpic(epic));
  }

  /**
   * 指定されたプロダクトに新しいエピックを作成します。
   *
   * @param productId 作成するエピックが属するプロダクトのID
   * @param epic 作成するエピック
   * @return 作成されたエピック（プロダクトが存在しない場合は404）
   */
  @PostMapping("/products/{productId}")
  public ResponseEntity<Epic> createEpicForProduct(@PathVariable String productId,
      @RequestBody Epic epic) {
    return epicCommand.createEpicForProduct(SnowflakeId.of(Long.parseLong(productId)), epic)
        .map(createdEpic -> ResponseEntity.status(HttpStatus.CREATED).body(createdEpic))
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * 指定されたIDのエピックを更新します。
   *
   * @param epicId 更新するエピックのID
   * @param epic 更新内容を含むエピック
   * @return 更新されたエピック（存在しない場合は404）
   */
  @PutMapping("/{epicId}")
  public ResponseEntity<Epic> updateEpic(@PathVariable String epicId, @RequestBody Epic epic) {
    return epicCommand.updateEpic(SnowflakeId.of(Long.parseLong(epicId)), epic)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * 指定されたIDのエピックを削除します。
   *
   * @param epicId 削除するエピックのID
   * @return 削除成功時は204、存在しない場合は404
   */
  @DeleteMapping("/{epicId}")
  public ResponseEntity<Void> deleteEpic(@PathVariable String epicId) {
    try {
      epicCommand.deleteEpic(SnowflakeId.of(Long.parseLong(epicId)));
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * 指定されたプロダクトに関連するすべてのエピックを取得します。
   *
   * @param productId 検索対象のプロダクトID
   * @return 指定されたプロダクトに関連するエピックのリスト
   */
  @GetMapping("/products/{productId}")
  public ResponseEntity<List<Epic>> getEpicsByProductId(@PathVariable String productId) {
    return ResponseEntity.ok(
        epicCommand.getEpicsByProductId(SnowflakeId.of(Long.parseLong(productId))));
  }

  /**
   * 指定されたステータスを持つエピックを取得します。
   *
   * @param status 検索対象のステータス
   * @return 指定されたステータスを持つエピックのリスト
   */
  @GetMapping("/status/{status}")
  public ResponseEntity<List<Epic>> getEpicsByStatus(@PathVariable Epic.Status status) {
    return ResponseEntity.ok(epicCommand.getEpicsByStatus(status));
  }

  /**
   * 複数のスプリントにまたがるエピックを取得します。
   *
   * @return 複数のスプリントにまたがるエピックのリスト
   */
  @GetMapping("/spanning-multiple-sprints")
  public ResponseEntity<List<Epic>> getEpicsSpanningMultipleSprints() {
    return ResponseEntity.ok(epicCommand.getEpicsSpanningMultipleSprints());
  }

  /**
   * 複数のスプリントにまたがるユーザーストーリーを持つエピックを取得します。
   *
   * @return 複数のスプリントにまたがるユーザーストーリーを持つエピックのリスト
   */
  @GetMapping("/with-stories-spanning-multiple-sprints")
  public ResponseEntity<List<Epic>> getEpicsWithUserStoriesSpanningMultipleSprints() {
    return ResponseEntity.ok(epicCommand.getEpicsWithUserStoriesSpanningMultipleSprints());
  }

  /**
   * 指定されたエピックに関連するスプリントを取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 関連するスプリントのリスト
   */
  @GetMapping("/{epicId}/sprints")
  public ResponseEntity<List<Sprint>> getSprintsByEpicId(@PathVariable String epicId) {
    return ResponseEntity.ok(
        epicCommand.getSprintsByEpicId(SnowflakeId.of(Long.parseLong(epicId))));
  }

  /**
   * 指定されたエピックに関連するユーザーストーリーを取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 関連するユーザーストーリーのリスト
   */
  @GetMapping("/{epicId}/user-stories")
  public ResponseEntity<List<UserStory>> getUserStoriesByEpicId(@PathVariable String epicId) {
    return ResponseEntity.ok(
        epicCommand.getUserStoriesByEpicId(SnowflakeId.of(Long.parseLong(epicId))));
  }

  /**
   * 指定されたエピックに関連するユーザーストーリーの数を取得します。
   *
   * @param epicId 検索対象のエピックID
   * @return 関連するユーザーストーリーの数
   */
  @GetMapping("/{epicId}/user-stories/count")
  public ResponseEntity<Map<String, Long>> countUserStoriesByEpicId(@PathVariable String epicId) {
    long count = epicCommand.countUserStoriesByEpicId(SnowflakeId.of(Long.parseLong(epicId)));
    return ResponseEntity.ok(Map.of("count", count));
  }

  /**
   * ユーザーストーリーをエピックに関連付けます。
   *
   * @param epicId 関連付けるエピックのID
   * @param storyId 関連付けるユーザーストーリーのID
   * @return 更新されたユーザーストーリー（エピックまたはユーザーストーリーが存在しない場合は404）
   */
  @PostMapping("/{epicId}/user-stories/{storyId}")
  public ResponseEntity<UserStory> associateUserStoryWithEpic(@PathVariable String epicId,
      @PathVariable String storyId) {
    return epicCommand.associateUserStoryWithEpic(
            SnowflakeId.of(Long.parseLong(epicId)),
            SnowflakeId.of(Long.parseLong(storyId)))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * ユーザーストーリーとエピックの関連付けを解除します。
   *
   * @param storyId 関連付けを解除するユーザーストーリーのID
   * @return 更新されたユーザーストーリー（存在しない場合は404）
   */
  @DeleteMapping("/user-stories/{storyId}")
  public ResponseEntity<UserStory> disassociateUserStoryFromEpic(@PathVariable String storyId) {
    return epicCommand.disassociateUserStoryFromEpic(SnowflakeId.of(Long.parseLong(storyId)))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * 指定されたエピックが複数のスプリントにまたがっているかを確認します。
   *
   * @param epicId 検索対象のエピックID
   * @return 複数のスプリントにまたがっている場合はtrue、そうでない場合はfalse
   */
  @GetMapping("/{epicId}/is-spanning-multiple-sprints")
  public ResponseEntity<Map<String, Boolean>> isEpicSpanningMultipleSprints(
      @PathVariable String epicId) {
    boolean isSpanning = epicCommand.isEpicSpanningMultipleSprints(
        SnowflakeId.of(Long.parseLong(epicId)));
    return ResponseEntity.ok(Map.of("isSpanningMultipleSprints", isSpanning));
  }
}
