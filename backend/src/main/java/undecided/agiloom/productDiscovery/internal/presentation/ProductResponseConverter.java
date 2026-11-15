package undecided.agiloom.productDiscovery.internal.presentation;

import java.util.List;
import org.springframework.stereotype.Component;
import undecided.agiloom.productDiscovery.spi.Product;

/**
 * ProductエンティティをResponse DTOに変換するコンバーターです。
 */
@Component
public class ProductResponseConverter {

  private final ScrumTeamResponseConverter scrumTeamResponseConverter;

  public ProductResponseConverter(ScrumTeamResponseConverter scrumTeamResponseConverter) {
    this.scrumTeamResponseConverter = scrumTeamResponseConverter;
  }

  public ProductResponse toProductResponse(Product product) {
    List<ScrumTeamResponse> scrumTeamResponses =
        product.getScrumTeams().stream().map(scrumTeamResponseConverter::toScrumTeamResponse).toList();

    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .managerId(product.getManagerId())
        .status(product.getStatus())
        .createdAt(product.getCreatedAt())
        .updatedAt(product.getUpdatedAt())
        .scrumTeams(scrumTeamResponses)
        .build();
  }
}
