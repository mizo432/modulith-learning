package undecided.agiloom.productDiscovery.internal.presentation;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import undecided.agiloom.productDiscovery.spi.Product;
import undecided.agiloom.productDiscovery.spi.ScrumTeam;

/**
 * ProductとScrumTeamのエンティティをResponse DTOに変換するコンバーターです。
 */
@Component
public class ProductResponseConverter {

  public ProductResponse toProductResponse(Product product) {
    List<ScrumTeamResponse> scrumTeamResponses =
        product.getScrumTeams().stream().map(this::toScrumTeamResponse).toList();

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

  public ScrumTeamResponse toScrumTeamResponse(ScrumTeam scrumTeam) {
    return ScrumTeamResponse.builder()
        .id(scrumTeam.getId())
        .name(scrumTeam.getName())
        .businessDomain(scrumTeam.getBusinessDomain())
        .poEmployeeId(scrumTeam.getPoEmployeeId())
        .poName(scrumTeam.getPoName())
        .productId(
            scrumTeam.getProduct() != null ? String.valueOf(scrumTeam.getProduct().getId()) : null)
        .createdAt(scrumTeam.getCreatedAt())
        .updatedAt(scrumTeam.getUpdatedAt())
        .build();
  }
}
