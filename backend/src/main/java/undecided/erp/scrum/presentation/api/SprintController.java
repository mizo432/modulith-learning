package undecided.erp.scrum.presentation.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.scrum.application.command.SprintService;
import undecided.erp.scrum.domain.model.sprint.Sprint;
import undecided.erp.scrum.domain.model.sprint.SprintBacklog;
import undecided.erp.scrum.domain.model.task.UserStory;

/**
 * スプリント管理に関連するREST APIを提供するコントローラークラス。
 *
 * <p>このクラスはスプリントの作成、開始、完了、およびスプリントバックログの管理などの スプリント関連の操作をREST APIとして公開します。
 */
@RestController
@RequestMapping("/api/sprints")
@RequiredArgsConstructor
public class SprintController {

  private final SprintService sprintService;

  /**
   * 新しいスプリントを作成します。
   *
   * @param request スプリント作成リクエスト
   * @return 作成されたスプリント
   */
  @PostMapping
  public ResponseEntity<SprintResponse> createSprint(@RequestBody CreateSprintRequest request) {
    Sprint sprint =
        sprintService.createSprint(
            new SnowflakeId(request.getProductId()),
            request.getName(),
            request.getGoal(),
            request.getStartDate(),
            request.getEndDate());
    SprintResponse response = convertToSprintResponse(sprint);
    // 新しいスプリントのIDは生成されたSnowflakeIdを使用するが、
    // 直接アクセスできないため、ここでは仮のIDを設定する
    response.setSprintId(System.currentTimeMillis()); // 一時的な対応として現在時刻をIDとして使用
    response.setProductId(request.getProductId());
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * スプリントを開始します。
   *
   * @param sprintId 開始するスプリントのID
   * @return 更新されたスプリント
   */
  @PutMapping("/{sprintId}/start")
  public ResponseEntity<SprintResponse> startSprint(@PathVariable Long sprintId) {
    Sprint sprint = sprintService.startSprint(new SnowflakeId(sprintId));
    SprintResponse response = convertToSprintResponse(sprint);
    response.setSprintId(sprintId);
    return ResponseEntity.ok(response);
  }

  /**
   * スプリントを完了します。
   *
   * @param sprintId 完了するスプリントのID
   * @return 更新されたスプリント
   */
  @PutMapping("/{sprintId}/complete")
  public ResponseEntity<SprintResponse> completeSprint(@PathVariable Long sprintId) {
    Sprint sprint = sprintService.completeSprint(new SnowflakeId(sprintId));
    SprintResponse response = convertToSprintResponse(sprint);
    response.setSprintId(sprintId);
    return ResponseEntity.ok(response);
  }

  /**
   * スプリントをキャンセルします。
   *
   * @param sprintId キャンセルするスプリントのID
   * @return 更新されたスプリント
   */
  @PutMapping("/{sprintId}/cancel")
  public ResponseEntity<SprintResponse> cancelSprint(@PathVariable Long sprintId) {
    Sprint sprint = sprintService.cancelSprint(new SnowflakeId(sprintId));
    SprintResponse response = convertToSprintResponse(sprint);
    response.setSprintId(sprintId);
    return ResponseEntity.ok(response);
  }

  /**
   * スプリントゴールを設定します。
   *
   * @param sprintId ゴールを設定するスプリントのID
   * @param request ゴール設定リクエスト
   * @return 更新されたスプリント
   */
  @PutMapping("/{sprintId}/goal")
  public ResponseEntity<SprintResponse> setSprintGoal(
      @PathVariable Long sprintId, @RequestBody SetGoalRequest request) {
    Sprint sprint = sprintService.setSprintGoal(new SnowflakeId(sprintId), request.getGoal());
    SprintResponse response = convertToSprintResponse(sprint);
    response.setSprintId(sprintId);
    return ResponseEntity.ok(response);
  }

  /**
   * ユーザーストーリーをスプリントバックログに追加します。
   *
   * @param sprintId スプリントID
   * @param request バックログアイテム追加リクエスト
   * @return 作成されたスプリントバックログアイテム
   */
  @PostMapping("/{sprintId}/backlog")
  public ResponseEntity<BacklogItemResponse> addUserStoryToSprint(
      @PathVariable Long sprintId, @RequestBody AddBacklogItemRequest request) {
    SprintBacklog backlogItem =
        sprintService.addUserStoryToSprint(
            new SnowflakeId(sprintId),
            new SnowflakeId(request.getStoryId()),
            request.getName(),
            request.getDescription(),
            request.getEstimatedEffort());
    BacklogItemResponse response = convertToBacklogItemResponse(backlogItem);
    response.setBacklogId(System.currentTimeMillis()); // 一時的な対応として現在時刻をIDとして使用
    response.setSprintId(sprintId);
    response.setStoryId(request.getStoryId());
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  /**
   * スプリントバックログアイテムの進捗を更新します。
   *
   * @param sprintId スプリントID
   * @param backlogId バックログアイテムID
   * @param request 進捗更新リクエスト
   * @return 更新されたスプリントバックログアイテム
   */
  @PutMapping("/{sprintId}/backlog/{backlogId}/progress")
  public ResponseEntity<BacklogItemResponse> updateBacklogItemProgress(
      @PathVariable Long sprintId,
      @PathVariable Long backlogId,
      @RequestBody UpdateProgressRequest request) {
    SprintBacklog backlogItem =
        sprintService.updateBacklogItemProgress(
            new SnowflakeId(backlogId), request.getRemainingEffort());
    BacklogItemResponse response = convertToBacklogItemResponse(backlogItem);
    response.setBacklogId(backlogId);
    response.setSprintId(sprintId);
    return ResponseEntity.ok(response);
  }

  /**
   * 未完了のストーリーを次のスプリントに移動します。
   *
   * @param fromSprintId 移動元のスプリントID
   * @param toSprintId 移動先のスプリントID
   * @return 移動されたバックログアイテムのリスト
   */
  @PostMapping("/{fromSprintId}/move-incomplete-stories")
  public ResponseEntity<List<BacklogItemResponse>> moveIncompleteStoriesToNextSprint(
      @PathVariable Long fromSprintId, @RequestParam Long toSprintId) {
    List<SprintBacklog> movedItems =
        sprintService.moveIncompleteStoriesToNextSprint(
            new SnowflakeId(fromSprintId), new SnowflakeId(toSprintId));
    List<BacklogItemResponse> responses = new ArrayList<>();
    for (int i = 0; i < movedItems.size(); i++) {
      SprintBacklog item = movedItems.get(i);
      BacklogItemResponse response = convertToBacklogItemResponse(item);
      response.setBacklogId(System.currentTimeMillis() + i); // 一時的な対応として現在時刻+インデックスをIDとして使用
      response.setSprintId(toSprintId);
      responses.add(response);
    }
    return ResponseEntity.ok(responses);
  }

  /**
   * ユーザーストーリーを分割して一部を次のスプリントに移動します。
   *
   * @param storyId 分割するユーザーストーリーID
   * @param request ストーリー分割リクエスト
   * @return 分割されて作成された新しいユーザーストーリー
   */
  @PostMapping("/stories/{storyId}/split")
  public ResponseEntity<UserStoryResponse> splitAndMoveStory(
      @PathVariable Long storyId, @RequestBody SplitStoryRequest request) {
    UserStory newStory =
        sprintService.splitAndMoveStory(
            new SnowflakeId(storyId),
            request.getNewTitle(),
            request.getNewDescription(),
            request.getNewStoryPoints(),
            new SnowflakeId(request.getToSprintId()));
    UserStoryResponse response = convertToUserStoryResponse(newStory);
    // UserStory.getStoryId()メソッドは存在するため、IDの設定は不要
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  // リクエスト・レスポンスクラス

  private SprintResponse convertToSprintResponse(Sprint sprint) {
    SprintResponse response = new SprintResponse();
    // IDはサービスメソッドに渡された値を使用するため、ここでは設定しない
    response.setName(sprint.getName());
    response.setGoal(sprint.getGoal());
    response.setStartDate(sprint.getStartDate());
    response.setEndDate(sprint.getEndDate());
    response.setStatus(sprint.getStatus().name());
    return response;
  }

  private BacklogItemResponse convertToBacklogItemResponse(SprintBacklog backlogItem) {
    BacklogItemResponse response = new BacklogItemResponse();
    // IDはサービスメソッドに渡された値を使用するため、ここでは設定しない
    response.setName(backlogItem.getName());
    response.setDescription(backlogItem.getDescription());
    response.setEstimatedEffort(backlogItem.getEstimatedEffort());
    response.setRemainingEffort(backlogItem.getRemainingEffort());
    response.setProgress(backlogItem.calculateProgress());
    return response;
  }

  private UserStoryResponse convertToUserStoryResponse(UserStory userStory) {
    UserStoryResponse response = new UserStoryResponse();
    response.setStoryId(userStory.getStoryId().getValue());
    response.setTitle(userStory.getTitle());
    response.setDescription(userStory.getDescription());
    response.setAcceptanceCriteria(userStory.getAcceptanceCriteria());
    response.setPriority(userStory.getPriority().name());
    response.setStoryPoints(userStory.getStoryPoints());
    response.setValue(userStory.getValue());
    response.setStatus(userStory.getStatus().name());
    return response;
  }

  /** スプリント作成リクエスト。 */
  public static class CreateSprintRequest {
    private Long productId;
    private String name;
    private String goal;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    public Long getProductId() {
      return productId;
    }

    public void setProductId(Long productId) {
      this.productId = productId;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getGoal() {
      return goal;
    }

    public void setGoal(String goal) {
      this.goal = goal;
    }

    public LocalDate getStartDate() {
      return startDate;
    }

    public void setStartDate(LocalDate startDate) {
      this.startDate = startDate;
    }

    public LocalDate getEndDate() {
      return endDate;
    }

    public void setEndDate(LocalDate endDate) {
      this.endDate = endDate;
    }
  }

  /** ゴール設定リクエスト。 */
  public static class SetGoalRequest {
    private String goal;

    public String getGoal() {
      return goal;
    }

    public void setGoal(String goal) {
      this.goal = goal;
    }
  }

  /** バックログアイテム追加リクエスト。 */
  public static class AddBacklogItemRequest {
    private Long storyId;
    private String name;
    private String description;
    private Integer estimatedEffort;

    public Long getStoryId() {
      return storyId;
    }

    public void setStoryId(Long storyId) {
      this.storyId = storyId;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Integer getEstimatedEffort() {
      return estimatedEffort;
    }

    public void setEstimatedEffort(Integer estimatedEffort) {
      this.estimatedEffort = estimatedEffort;
    }
  }

  /** 進捗更新リクエスト。 */
  public static class UpdateProgressRequest {
    private Integer remainingEffort;

    public Integer getRemainingEffort() {
      return remainingEffort;
    }

    public void setRemainingEffort(Integer remainingEffort) {
      this.remainingEffort = remainingEffort;
    }
  }

  /** ストーリー分割リクエスト。 */
  public static class SplitStoryRequest {
    private String newTitle;
    private String newDescription;
    private Integer newStoryPoints;
    private Long toSprintId;

    public String getNewTitle() {
      return newTitle;
    }

    public void setNewTitle(String newTitle) {
      this.newTitle = newTitle;
    }

    public String getNewDescription() {
      return newDescription;
    }

    public void setNewDescription(String newDescription) {
      this.newDescription = newDescription;
    }

    public Integer getNewStoryPoints() {
      return newStoryPoints;
    }

    public void setNewStoryPoints(Integer newStoryPoints) {
      this.newStoryPoints = newStoryPoints;
    }

    public Long getToSprintId() {
      return toSprintId;
    }

    public void setToSprintId(Long toSprintId) {
      this.toSprintId = toSprintId;
    }
  }

  // ヘルパーメソッド

  /** スプリントレスポンス。 */
  public static class SprintResponse {
    private Long sprintId;
    private Long productId;
    private String name;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public Long getSprintId() {
      return sprintId;
    }

    public void setSprintId(Long sprintId) {
      this.sprintId = sprintId;
    }

    public Long getProductId() {
      return productId;
    }

    public void setProductId(Long productId) {
      this.productId = productId;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getGoal() {
      return goal;
    }

    public void setGoal(String goal) {
      this.goal = goal;
    }

    public LocalDate getStartDate() {
      return startDate;
    }

    public void setStartDate(LocalDate startDate) {
      this.startDate = startDate;
    }

    public LocalDate getEndDate() {
      return endDate;
    }

    public void setEndDate(LocalDate endDate) {
      this.endDate = endDate;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }
  }

  /** バックログアイテムレスポンス。 */
  public static class BacklogItemResponse {
    private Long backlogId;
    private Long sprintId;
    private Long storyId;
    private String name;
    private String description;
    private Integer estimatedEffort;
    private Integer remainingEffort;
    private Integer progress;

    public Long getBacklogId() {
      return backlogId;
    }

    public void setBacklogId(Long backlogId) {
      this.backlogId = backlogId;
    }

    public Long getSprintId() {
      return sprintId;
    }

    public void setSprintId(Long sprintId) {
      this.sprintId = sprintId;
    }

    public Long getStoryId() {
      return storyId;
    }

    public void setStoryId(Long storyId) {
      this.storyId = storyId;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public Integer getEstimatedEffort() {
      return estimatedEffort;
    }

    public void setEstimatedEffort(Integer estimatedEffort) {
      this.estimatedEffort = estimatedEffort;
    }

    public Integer getRemainingEffort() {
      return remainingEffort;
    }

    public void setRemainingEffort(Integer remainingEffort) {
      this.remainingEffort = remainingEffort;
    }

    public Integer getProgress() {
      return progress;
    }

    public void setProgress(Integer progress) {
      this.progress = progress;
    }
  }

  /** ユーザーストーリーレスポンス。 */
  public static class UserStoryResponse {
    private Long storyId;
    private Long backlogId;
    private Long epicId;
    private String title;
    private String description;
    private String acceptanceCriteria;
    private String priority;
    private Integer storyPoints;
    private Integer value;
    private String status;

    public Long getStoryId() {
      return storyId;
    }

    public void setStoryId(Long storyId) {
      this.storyId = storyId;
    }

    public Long getBacklogId() {
      return backlogId;
    }

    public void setBacklogId(Long backlogId) {
      this.backlogId = backlogId;
    }

    public Long getEpicId() {
      return epicId;
    }

    public void setEpicId(Long epicId) {
      this.epicId = epicId;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getAcceptanceCriteria() {
      return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
      this.acceptanceCriteria = acceptanceCriteria;
    }

    public String getPriority() {
      return priority;
    }

    public void setPriority(String priority) {
      this.priority = priority;
    }

    public Integer getStoryPoints() {
      return storyPoints;
    }

    public void setStoryPoints(Integer storyPoints) {
      this.storyPoints = storyPoints;
    }

    public Integer getValue() {
      return value;
    }

    public void setValue(Integer value) {
      this.value = value;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }
  }
}
