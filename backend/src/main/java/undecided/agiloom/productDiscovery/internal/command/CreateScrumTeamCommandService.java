package undecided.agiloom.productDiscovery.internal.command;

import static com.github.mizo432.shared.common.precondition.StringPrecondition.checkNonEmpty;

import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.productDiscovery.internal.ProductRepository;
import undecided.agiloom.productDiscovery.internal.ScrumTeamRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.agiloom.sprintExecution.internal.SprintRepository;
import undecided.agiloom.sprintExecution.spi.Sprint;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class CreateScrumTeamCommandService {

  private final ScrumTeamRepository scrumTeamRepository;
  private final ProductRepository productRepository;
  private final SprintRepository sprintRepository;

  public CreateScrumTeamCommandService(
      ScrumTeamRepository scrumTeamRepository,
      ProductRepository productRepository,
      SprintRepository sprintRepository) {
    this.scrumTeamRepository = scrumTeamRepository;
    this.productRepository = productRepository;
    this.sprintRepository = sprintRepository;
  }

  public SnowflakeId execute(@NonNull CreateScrumTeamCommand command) {
    checkNonEmpty(command.getName(), () -> new IllegalArgumentException("ScrumTeam name must not be empty"));
    checkNonEmpty(command.getBusinessDomain(), () -> new IllegalArgumentException("Business domain must not be empty"));

    Product product =
        productRepository
            .findById(command.getProductId())
            .orElseThrow(
                () -> new IllegalArgumentException("Product not found: " + command.getProductId()));

    ScrumTeam scrumTeam =
        ScrumTeam.builder()
            .name(command.getName())
            .businessDomain(command.getBusinessDomain())
            .poEmployeeId(command.getPoEmployeeId())
            .poName(command.getPoName())
            .smEmployeeId(command.getSmEmployeeId())
            .smName(command.getSmName())
            .product(product)
            .build();

    ScrumTeam saved = scrumTeamRepository.save(scrumTeam);

    // スクラムチーム作成時にスプリント0を自動的に生成する
    createSprintZeroForTeam(
        snowflakeIdToUuid(product.getId()),
        snowflakeIdToUuid(saved.getId()),
        saved.getName());

    return saved.getId();
  }

  /**
   * スクラムチーム用にスプリント0を生成します。
   * 既にスプリント0が存在する場合は生成しません。
   *
   * @param productId 製品のID
   * @param teamId スクラムチームのID
   * @param teamName スクラムチームの名前
   */
  private void createSprintZeroForTeam(UUID productId, UUID teamId, String teamName) {
    // 既にスプリント0が存在するか確認
    boolean exists = sprintRepository.findByTeamIdAndIsSprintZero(teamId, true).isPresent();
    if (exists) {
      return;
    }

    Sprint sprintZero = Sprint.createSprintZero(productId, teamId, teamName);
    sprintRepository.save(sprintZero);
  }

  /**
   * SnowflakeIdをUUIDに変換します。
   *
   * @param snowflakeId 変換するSnowflakeId
   * @return 変換されたUUID
   */
  private UUID snowflakeIdToUuid(SnowflakeId snowflakeId) {
    return UUID.nameUUIDFromBytes(("sf:" + snowflakeId.value()).getBytes(java.nio.charset.StandardCharsets.UTF_8));
  }
}
