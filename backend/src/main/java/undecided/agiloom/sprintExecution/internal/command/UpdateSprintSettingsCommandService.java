package undecided.agiloom.sprintExecution.internal.command;

import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;

import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.internal.SprintSettingsRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.agiloom.sprintExecution.spi.SprintSettings;
import undecided.agiloom.sprintExecution.spi.SprintStatus;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * スプリント設定更新コマンドサービス。
 *
 * <p>スプリント0中にのみスプリント設定を変更できる様に制約します。
 */
@Service
@Transactional
public class UpdateSprintSettingsCommandService {

  private final SprintSettingsRepository sprintSettingsRepository;
  private final SprintRepository sprintRepository;

  public UpdateSprintSettingsCommandService(
      SprintSettingsRepository sprintSettingsRepository, SprintRepository sprintRepository) {
    this.sprintSettingsRepository = sprintSettingsRepository;
    this.sprintRepository = sprintRepository;
  }

  /**
   * スプリント設定を更新します。
   *
   * <p>スプリント0が存在し、PLANNED状態である場合にのみ更新可能です。
   *
   * @param command 更新コマンド
   * @return 更新されたスプリント設定のID
   */
  public SnowflakeId execute(@NonNull UpdateSprintSettingsCommand command) {
    checkNotNull(command, () -> new IllegalArgumentException("command must not be null"));

    SnowflakeId productId = command.getProductId();

    // スプリント0が存在するか確認
    Optional<Sprint> sprintZero = sprintRepository.findByProductIdAndSprintNumber(productId, 0);
    if (sprintZero.isPresent()) {
      Sprint sprint = sprintZero.get();
      // スプリント0がPLANNED状態でない場合は更新不可
      if (sprint.getStatus() != SprintStatus.PLANNED) {
        throw new IllegalStateException(
            "Sprint settings can only be modified during Sprint 0 in PLANNED status. Current status: "
                + sprint.getStatus());
      }
    }

    // 既存の設定を更新、なければ新規作成
    Optional<SprintSettings> existing = sprintSettingsRepository.findByProductId(productId);
    if (existing.isPresent()) {
      SprintSettings settings = existing.get();
      settings.update(command.getBusinessDays(), command.getEndDayOfWeek());
      SprintSettings saved = sprintSettingsRepository.save(settings);
      return saved.getId();
    } else {
      SprintSettings settings = SprintSettings.builder()
          .productId(productId)
          .businessDays(command.getBusinessDays())
          .endDayOfWeek(command.getEndDayOfWeek())
          .build();
      SprintSettings saved = sprintSettingsRepository.save(settings);
      return saved.getId();
    }
  }
}
