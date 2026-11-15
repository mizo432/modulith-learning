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
import undecided.agiloom.sprintExecution.spi.SprintZeroFactory;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class CreateScrumTeamCommandService {

  private final ScrumTeamRepository scrumTeamRepository;
  private final ProductRepository productRepository;
  private final SprintZeroFactory sprintZeroFactory;

  public CreateScrumTeamCommandService(
      ScrumTeamRepository scrumTeamRepository,
      ProductRepository productRepository,
      SprintZeroFactory sprintZeroFactory) {
    this.scrumTeamRepository = scrumTeamRepository;
    this.productRepository = productRepository;
    this.sprintZeroFactory = sprintZeroFactory;
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
    sprintZeroFactory.createSprintZero(
        snowflakeIdToUuid(product.getId()),
        snowflakeIdToUuid(saved.getId()),
        saved.getName());

    return saved.getId();
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
