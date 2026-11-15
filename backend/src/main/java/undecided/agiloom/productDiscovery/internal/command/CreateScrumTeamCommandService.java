package undecided.agiloom.productDiscovery.internal.command;

import static com.github.mizo432.shared.common.precondition.StringPrecondition.checkNonEmpty;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import undecided.agiloom.productDiscovery.internal.ProductRepository;
import undecided.agiloom.productDiscovery.internal.ScrumTeamRepository;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;
import undecided.supporting.snowflake.SnowflakeId;

@Service
@Transactional
public class CreateScrumTeamCommandService {

  private final ScrumTeamRepository scrumTeamRepository;
  private final ProductRepository productRepository;

  public CreateScrumTeamCommandService(
      ScrumTeamRepository scrumTeamRepository, ProductRepository productRepository) {
    this.scrumTeamRepository = scrumTeamRepository;
    this.productRepository = productRepository;
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
            .product(product)
            .build();

    ScrumTeam saved = scrumTeamRepository.save(scrumTeam);
    return saved.getId();
  }
}
